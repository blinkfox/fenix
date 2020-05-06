package com.blinkfox.fenix.config.scanner;

import com.blinkfox.fenix.config.FenixConfig;
import com.blinkfox.fenix.config.annotation.Tagger;
import com.blinkfox.fenix.config.annotation.Taggers;
import com.blinkfox.fenix.config.entity.TagHandler;
import com.blinkfox.fenix.consts.Const;
import com.blinkfox.fenix.core.FenixHandler;
import com.blinkfox.fenix.helper.StringHelper;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.extern.slf4j.Slf4j;

/**
 * Fenix {@link TagHandler} 实现类上标注的 XML 标签注解 {@link Tagger} 扫描类.
 *
 * <p>本类会将扫描到的类添加到 {@link FenixConfig} 的 `tagHandlerMap` 中，供后续配置使用.</p>
 *
 * @author blinkfox on 2019-08-04.
 */
@Slf4j
public final class TaggerScanner {

    /**
     * 文件协议标识符.
     */
    private static final String FILE_PROTOCOL = "file";

    /**
     * 存放所有扫描位置下的 class 对象的 Set 集合.
     */
    private Set<Class<?>> classSet;

    /**
     * 构造方法，初始化 classSet 的 HashSet 实例.
     */
    public TaggerScanner() {
        this.classSet = new HashSet<>();
    }

    /**
     * 扫描配置的 Fenix handler 包下所有的 class.
     * <p>并将含有 {@link Tagger} 和 {@link Taggers} 的注解的 Class 解析出来，存储到 tagHandlerMap 配置中.</p>
     *
     * @param handlerLocations {@link TagHandler} 实现类所在的位置，即可以是目录也可以是文件，多个用逗号隔开
     */
    public void scan(String handlerLocations) {
        if (StringHelper.isBlank(handlerLocations)) {
            return;
        }

        // 对配置的 XML 路径按逗号分割的规则来解析，如果是 XML 文件则直接将该 XML 文件存放到 classSet 的 Set 集合中.
        // 否则就代表是 XML 资源目录，并解析目录下所有的 XML 文件，将这些 XML 文件存放到 xmlPaths 的 Set 集合中.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String[] locationArr = handlerLocations.split(Const.COMMA);
        for (String location : locationArr) {
            if (StringHelper.isBlank(location)) {
                continue;
            }

            // 判断文件如果是具体的 Java 文件或 class 文件，如果是的话，就将文件解析成 Class 对象.
            // 如果都不是，则视其为包,然后解析该包及子包下面的所有 class 文件.
            String handlerLocation = location.trim();
            if (StringHelper.isJavaFile(handlerLocation) || StringHelper.isClassFile(handlerLocation)) {
                this.addClassByName(classLoader, handlerLocation.substring(0, handlerLocation.lastIndexOf(Const.DOT)));
            } else {
                this.addClassByPackage(classLoader, handlerLocation);
            }
        }

        this.addTagHanderInMap();
    }

    /**
     * 根据 classLoader 和 className 找到对应的 class 对象.
     *
     * @param classLoader ClassLoader 实例
     * @param className class 全路径名
     */
    private void addClassByName(ClassLoader classLoader, String className) {
        try {
            classSet.add(classLoader.loadClass(className));
        } catch (ClassNotFoundException expected) {
            // 由于是扫描 package 下的 class，即时出现异常，也忽略掉.
            log.warn("【Fenix 警告】未找到 class 类:【" + className + "】，将忽略不解析此类.");
        }
    }

    /**
     * 根据包名和 Classloader 实例，将该包下的所有 Class 存放到 classSet 集合中.
     *
     * @param classLoader ClassLoader 实例
     * @param packageName 包名
     */
    private void addClassByPackage(ClassLoader classLoader, String packageName) {
        // 根据包名和 Classloader 实例，得到该包的 URL Enumeration.
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> urlEnum = this.getUrlsByPackge(classLoader, packageDirName);
        if (urlEnum == null) {
            return;
        }

        while (urlEnum.hasMoreElements()) {
            URL url = urlEnum.nextElement();
            String protocol = url.getProtocol();
            if (FILE_PROTOCOL.equals(protocol)) {
                try {
                    this.addClassesByFile(classLoader, packageName,
                            URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException expected) {
                    // 此处不打印异常堆栈.
                    log.warn("【Fenix 警告】该包结构无法转换成 UTF-8 的编码格式.");
                }
            } else if ("jar".equals(protocol)) {
                this.addClassByJar(classLoader, url, packageName, packageDirName);
            }
        }
    }

    /**
     * 根据包名和 Classloader 实例，得到该包的 URL Enumeration.
     *
     * @param classLoader ClassLoader实例
     * @param packageName 包全路径名
     * @return URL 枚举
     */
    private Enumeration<URL> getUrlsByPackge(ClassLoader classLoader, String packageName) {
        try {
            return classLoader.getResources(packageName);
        } catch (IOException e) {
            // 由于是扫描 package 下的 class，即时出现异常，也忽略掉，不打印堆栈信息.
            log.warn("【Fenix 警告】未找到包:【" + packageName + "】下的 URL.");
            return null;
        }
    }

    /**
     * 以文件的形式来获取包下的所有 Class.
     *
     * @param classLoader 类加载器
     * @param packageName 包名
     * @param packagePath 包路径
     */
    private void addClassesByFile(ClassLoader classLoader, String packageName, String packagePath) {
        // 获取此包的目录，建立一个File，如果不存在或者也不是目录就直接返回
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        // 如果存在，就获取包下的所有文件和目录，筛选出目录和 '.class' 文件.
        File[] dirFiles = dir.listFiles(file -> (file.isDirectory()) || StringHelper.isClassFile(file.getName()));
        if (dirFiles == null || dirFiles.length == 0) {
            return;
        }

        // 循环所有文件,如果是目录，则继续扫描
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                this.addClassesByFile(classLoader, packageName + "." + file.getName(), file.getAbsolutePath());
                continue;
            }

            // 如果是java类文件 去掉后面的.class,只留下类名，添加到集合中去.
            this.addClassByName(classLoader, packageName + '.'
                    + file.getName().substring(0, file.getName().lastIndexOf('.')));
        }
    }

    /**
     * 通过识别 jar 的形式将其下的所有 class 添加到 classSet 集合中.
     *
     * @param classLoader 类加载器
     * @param url URL实例
     * @param packageName 包名
     * @param packageDirName 包路径名
     */
    private void addClassByJar(ClassLoader classLoader, URL url, String packageName, String packageDirName) {
        // 从 url 中获取 jar，然后从此 jar 包得到一个枚举类，然后进行迭代.
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                // 获取 jar 里的一个实体，可以是目录和一些 jar 包里的其他文件，如 META-INF 等文件.
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以 "/" 开头的，则获取后面的字符串.
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }

                // 如果前半部分和定义的包名相同.
                if (name.startsWith(packageDirName)) {
                    int index = name.lastIndexOf('/');
                    // 如果以 "/" 结尾，则是一个包，获取包名并把 "/" 替换成 "."
                    if (index != -1) {
                        packageName = name.substring(0, index).replace('/', '.');
                    }

                    // 如果是一个 ".class" 文件，且不是目录
                    if (index != -1 && name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的 ".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        this.addClassByName(classLoader, packageName + '.' + className);
                    }
                }
            }
        } catch (IOException expected) {
            // 此处不打印堆栈信息.
            log.warn("【Fenix 警告】从 jar 文件中读取 class 出错.");
        }
    }

    /**
     * 将扫描到的所有 class 进行再解析.如果其含有 {@link Tagger} 和 {@link Taggers} 注解，则将其进行解析添加到 tagHandlerMap 中.
     *
     * <p>循环遍历所有 class，如果其不是 {@link FenixHandler} 的实现类，则表明其是正确的 class，继续下次循环.</p>
     */
    @SuppressWarnings("unchecked")
    private void addTagHanderInMap() {
        for (Class<?> cls : classSet) {
            // 如果是 Tagger 注解，则将其 Tagger 信息存放到 Map 中.
            if (cls.isAnnotationPresent(Tagger.class) && isImplFenixHandlerClass(cls)) {
                Class<? extends FenixHandler> taggerCls = (Class<? extends FenixHandler>) cls;
                log.debug("【Fenix 提示】扫描到实现了 FenixHandler 接口，且含 @Tagger 注解的类：【{}】", taggerCls.getName());
                this.addTagHandlerInMapByTagger(taggerCls, taggerCls.getAnnotation(Tagger.class));
            }

            // 如果是 Taggers 注解，则解析出其下所有的 Tagger 来存放到 Map 中.
            if (cls.isAnnotationPresent(Taggers.class) && isImplFenixHandlerClass(cls)) {
                log.debug("【Fenix 提示】扫描到实现了 FenixHandler 接口，且含多个 @Tagger 注解的类：【{}】", cls.getName());
                Tagger[] taggerArr = cls.getAnnotation(Taggers.class).value();
                for (Tagger tagger : taggerArr) {
                    this.addTagHandlerInMapByTagger((Class<? extends FenixHandler>) cls, tagger);
                }
            }
        }
    }

    /**
     * 判断给定的 class 所对应的类是否是 {@link FenixHandler} 类的实现类.
     *
     * <p>由于通过 'isAssignableFrom()' 判断'实现'关系时是 false，所以这里采用获取 'getInterfaces()' 方法来判断.</p>
     *
     * @param implCls 待判断的 class
     * @return 布尔值
     */
    private boolean isImplFenixHandlerClass(Class<?> implCls) {
        Class<?>[] classes = implCls.getInterfaces();
        if (classes == null) {
            return false;
        }

        // 循环判断其接口是否含有 'FenixHandler' 接口.
        for (Class<?> cls : classes) {
            if (FenixHandler.class.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加单个的 {@link Tagger} 注解相关信息到 tagHandlerMap 中.
     *
     * @param cls FenixHandler 实现类的 class
     */
    private void addTagHandlerInMapByTagger(Class<? extends FenixHandler> cls, Tagger tagger) {
        FenixConfig.getTagHandlerMap()
                .put(tagger.value(), new TagHandler(tagger.prefix(), cls, tagger.symbol()));
    }

}
