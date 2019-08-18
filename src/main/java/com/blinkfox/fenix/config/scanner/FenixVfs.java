package com.blinkfox.fenix.config.scanner;

import com.blinkfox.fenix.exception.FenixException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 本类库使用的虚拟文件系统，继承自 {@link AbstractVfs}.
 *
 * @author blinkfox on 2019-08-05.
 * @see AbstractVfs
 */
@Slf4j
public class FenixVfs extends AbstractVfs {

    /**
     * 路径分割符.
     */
    private static final String SLASH = "/";

    /**
     * for 循环最大执行时限.
     */
    private static final long MAX_LIMIT = 30000;

    /**
     * 文件协议标识符.
     */
    static final String FILE_PROTOCOL = "file";

    /**
     * JAR (ZIP) 文件头标识的字节数组常量.
     */
    private static final byte[] JAR_MAGIC = {'P', 'K', 3, 4};

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * 根据资源路径和 URL 查找该路径下的所有文件资源.
     *
     * @param url url.
     * @param path 资源路径
     * @return 资源文件集合
     * @throws IOException IO 异常
     */
    @Override
    public List<String> list(URL url, String path) throws IOException {
        // 先根据 url 获取到 jar 文件的 URL ，如果 jarUrl 不为空，就直接返回其中的资源.
        URL jarUrl = this.findJarForResource(url);
        if (jarUrl != null) {
            try (InputStream is = jarUrl.openStream()) {
                return this.listResources(new JarInputStream(is), path);
            }
        }

        List<String> children = this.getResourceUrls(url, path, new ArrayList<>());

        // 递归列出子资源时使用的 URL 前缀.
        String prefix = url.toExternalForm();
        if (!prefix.endsWith(SLASH)) {
            prefix = prefix + SLASH;
        }

        // 将各 children 子资源，添加到 resources 集合中返回.
        List<String> resources = new ArrayList<>();
        for (String child : children) {
            String resourcePath = path + SLASH + child;
            resources.add(resourcePath);
            resources.addAll(list(new URL(prefix + child), resourcePath));
        }
        return resources;
    }

    /**
     * 获取资源中的子资源集合.
     *
     * @param url url
     * @param path 路径
     * @param children 子集合
     * @return children 集合
     * @throws IOException IO 异常
     */
    private List<String> getResourceUrls(URL url, String path, List<String> children) throws IOException {
        try {
            if (this.isJar(url)) {
                // 某些版本的 JBoss VFS 可能会提供 JAR 流，即使URL引用的资源实际上不是 JAR.
                try (InputStream is = url.openStream();
                        JarInputStream jarInput = new JarInputStream(is)) {
                    for (JarEntry entry; (entry = jarInput.getNextJarEntry()) != null;) {
                        children.add(entry.getName());
                    }
                }
            } else {
                /*
                 * 某些 servlet 容器允许从目录资源（如文本文件）中读取，每行列出一个子资源。但是，只能通过读取它们来区分目录和文件资源.
                 * 要解决这个问题，在读取每一行时，尝试通过类加载器将其作为当前资源的子项进行查找。如果任何行失败，那么我们假设当前资源不是目录.
                 */
                List<String> lines = new ArrayList<>();
                try (InputStream is = url.openStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isr)) {
                    for (String line; (line = reader.readLine()) != null;) {
                        lines.add(line);
                        if (getResources(path + SLASH + line).isEmpty()) {
                            lines.clear();
                            break;
                        }
                    }
                }

                // 如果行不为空，就将其添加到 children 集合中.
                if (!lines.isEmpty()) {
                    children.addAll(lines);
                }
            }
        } catch (FileNotFoundException e) {
            /*
             * 对于文件型的 URL，openStream() 调用可能会失败，具体取决于 servlet 容器，因为无法打开目录进行读取.
             * 如果发生这种情况，请直接列出目录.
             */
            children = this.getUrls(url, children, e);
        }
        return children;
    }

    /**
     * 对于文件型的 URL，openStream() 调用可能会失败，具体取决于 servlet 容器，因为无法打开目录进行读取.
     * 如果发生这种情况，请直接列出目录.
     *
     * @param url URL
     * @param children 子目录资源中的集合
     * @param e 异常
     * @return children 集合
     * @throws IOException IO 异常
     */
    private List<String> getUrls(URL url, List<String> children, FileNotFoundException e) throws IOException {
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            File file = new File(url.getFile());
            if (file.isDirectory()) {
                String[] files = file.list();
                if (files != null && files.length > 0) {
                    children = Arrays.asList(files);
                }
            }
        } else {
            throw e;
        }
        return children;
    }

    /**
     * 列出给定 {@link JarInputStream} 中以指定的 {@code path} 开头的条目的名称，条目将匹配带或不带斜杠的.
     *
     * @param jar jar 输入流
     * @param path 匹配的路径
     * @return 匹配到的名称
     * @throws IOException IO 异常
     */
    private List<String> listResources(JarInputStream jar, String path) throws IOException {
        // 匹配时要处理前缀或后缀都有 "/" 的情况.
        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }
        if (!path.endsWith(SLASH)) {
            path = path + SLASH;
        }

        // 循环判断以指定 path 开头的资源.
        List<String> resources = new ArrayList<>();
        for (JarEntry entry; (entry = jar.getNextJarEntry()) != null;) {
            if (!entry.isDirectory()) {
                // 如果路径最前面没有 "/"，就添加 "/".
                String name = entry.getName();
                name = name.startsWith(SLASH) ? name : SLASH + name;

                // 判断名称是否匹配，如果匹配就去除掉前面的 "/"，并添加到 resources 集合中.
                if (name.startsWith(path)) {
                    resources.add(name.substring(1));
                }
            }
        }
        return resources;
    }

    /**
     * 尝试解构给定的 URL 以查找包含 URL 引用的资源的 JAR 文件。
     * 也就是说，假设 URL 引用了 JAR 文件，此方法将返回引用包含该条目的 JAR 文件的 URL。如果找不到 JAR，则此方法返回 null.
     *
     * @param url jar URL.
     * @return jar 文件的 URL
     */
    private URL findJarForResource(URL url) {
        // 如果 URL 的文件部分本身是一个 URL，那么该URL可能指向 JAR.
        try {
            long startTime = System.currentTimeMillis();
            for (;;) {
                url = new URL(url.getFile());
                if (System.currentTimeMillis() - startTime > MAX_LIMIT) {
                    break;
                }
            }
        } catch (MalformedURLException expected) {
            // 将在某一时间发生此异常.
        }

        // 查找 .jar 扩展名并在此之后删除所有内容.
        StringBuilder jarUrl = new StringBuilder(url.toExternalForm());
        int index = jarUrl.lastIndexOf(".jar");
        if (index < 0) {
            return null;
        }

        // 尝试打开并测试它.
        jarUrl.setLength(index + 4);
        try {
            URL testUrl = new URL(jarUrl.toString());
            if (this.isJar(testUrl)) {
                return testUrl;
            }

            // 修复 WebLogic：检查文件系统中是否存在 URL 文件.
            if (log.isDebugEnabled()) {
                log.debug("【Fenix 提示】这不是一个 jar，jarUrl 为：【" + jarUrl + "】.");
            }

            jarUrl.replace(0, jarUrl.length(), testUrl.getFile());
            File file = new File(jarUrl.toString());

            // 文件名可能是 URL 编码的.
            if (!file.exists()) {
                file = new File(URLEncoder.encode(jarUrl.toString(), StandardCharsets.UTF_8.toString()));
            }

            if (file.exists()) {
                testUrl = file.toURI().toURL();
                if (this.isJar(testUrl)) {
                    return testUrl;
                }
            }
        } catch (MalformedURLException e) {
            log.warn("【Fenix 警示】无效的 jar URL:【" + jarUrl + "】.");
        } catch (UnsupportedEncodingException e) {
            throw new FenixException("【Fenix 异常】不支持的 UTF-8 编码.", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("【Fenix 提示】这不是一个 JAR，jar URL：【" + jarUrl + "】.");
        }
        return null;
    }

    /**
     * 如果位于给定 URL 的资源是 JAR 文件，则返回true.
     *
     * @param url 要测试的资源的 URL.
     */
    private boolean isJar(URL url) {
        return isJar(url, new byte[JAR_MAGIC.length]);
    }

    /**
     * 如果位于给定 URL 的资源是 JAR 文件，则返回 true.
     *
     * @param url 要测试的资源的 URL
     * @param buffer 读取资源的前几个字节的缓冲区。缓冲区的大小必须至少为 {@link FenixVfs#JAR_MAGIC}.
     */
    private boolean isJar(URL url, byte[] buffer) {
        try (InputStream is = url.openStream()) {
            int index = is.read(buffer, 0, JAR_MAGIC.length);
            if (index >= 0 && Arrays.equals(buffer, JAR_MAGIC)) {
                return true;
            }
        } catch (Exception expected) {
            // 如果发生异常，则表明不是 jar.
        }
        return false;
    }

}
