package com.blinkfox.fenix.config.scanner;

import com.blinkfox.fenix.exception.FenixException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 一个抽象的虚拟文件系统，提供了非常简单的一些 API，用于访问应用程序服务器中的资源.
 *
 * <p>注：该类是从 Mybatis 中参考而来.</p>
 *
 * @author blinkfox on 2019-08-05.
 */
@Slf4j
public abstract class AbstractVfs {

    /**
     * 内置的默认虚拟文件系统实现.
     */
    private static final Class<?>[] IMPLEMENTATIONS = {FenixVfs.class};

    /**
     * 通过 {@link #addImplClass(Class)} 来添加实现到集合中.
     */
    private static final List<Class<? extends AbstractVfs>> USER_IMPLEMENTATIONS = new ArrayList<>();

    /**
     * VFS 的单例持有类.
     */
    private static class VfsHolder {

        static final AbstractVfs INSTANCE = createVfs();

        @SuppressWarnings("unchecked")
        static AbstractVfs createVfs() {
            // 首先尝试用户实现，然后尝试内置插件.
            List<Class<? extends AbstractVfs>> impls = new ArrayList<>();
            impls.addAll(USER_IMPLEMENTATIONS);
            impls.addAll(Arrays.asList((Class<? extends AbstractVfs>[]) IMPLEMENTATIONS));

            // 尝试每个实现类，直到找到有效的实现类.
            AbstractVfs vfs = null;
            for (int i = 0; vfs == null || !vfs.isValid(); i++) {
                Class<? extends AbstractVfs> impl = impls.get(i);
                try {
                    vfs = impl.newInstance();
                    if (vfs == null || !vfs.isValid()) {
                        log.debug("【Fenix 提示】VFS 的实现类【" + impl.getName() + "】在此环境中无效.");
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("【Fenix 错误警示】无法实例化【" + impl + "】.", e);
                    return null;
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("【Fenix 提示】使用 VFS 适配器：【" + vfs.getClass().getName() + "】.");
            }
            return vfs;
        }
    }

    /**
     * 获取 {@link AbstractVfs} 单例实现. 如果在当前环境找不到 {@link AbstractVfs} 的实现，就返回 null.
     */
    public static AbstractVfs getInstance() {
        return VfsHolder.INSTANCE;
    }

    /**
     * 将指定的类添加到{@link AbstractVfs} 的实现列表中，以这种方式添加的类按照添加的顺序和任何内置实现之前进行尝试.
     *
     * @param clazz {@link AbstractVfs} 实现类的 class.
     */
    public static void addImplClass(Class<? extends AbstractVfs> clazz) {
        if (clazz != null) {
            USER_IMPLEMENTATIONS.add(clazz);
        }
    }

    /**
     * 按名称获取 class，如果找不到该类，则返回 null.
     */
    protected static Class<?> getClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("【Fenix 提示】Class 未找到:【" + className + "】.");
            }
            return null;
        }
    }

    /**
     * 按名称和参数类型获取方法，如果未找到该方法，则返回 null.
     *
     * @param clazz 方法所属的 class.
     * @param methodName 方法名称
     * @param parameterTypes 方法接受的参数类型.
     */
    protected static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null) {
            return null;
        }

        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            log.error("【Fenix 错误警示】查找方法安全异常，class:【" + clazz.getName() + "】，method:【" + methodName + "】.", e);
            return null;
        } catch (NoSuchMethodException e) {
            log.error("【Fenix 错误警示】方法未找到，class:【" + clazz.getName() + "】，method:【" + methodName + "】.", e);
            return null;
        }
    }

    /**
     * 在对象上调用一个方法并返回它返回的内容.
     *
     * @param method 调用的方法.
     * @param object 要调用该方法的实例或类（用于静态方法）.
     * @param parameters 传递给方法的参数.
     * @return Whatever 返回值.
     * @throws IOException IO 异常
     */
    @SuppressWarnings("unchecked")
    protected static <T> T invoke(Method method, Object object, Object... parameters) throws IOException {
        try {
            return (T) method.invoke(object, parameters);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new FenixException("IllegalArgumentException", e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IOException) {
                throw (IOException) e.getTargetException();
            } else {
                throw new FenixException("InvocationTargetException", e);
            }
        }
    }

    /**
     * 从上下文类加载器中获取指定路径中找到的所有资源的 {@link URL} 列表.
     *
     * @param path 资源路径.
     * @return {@link URL} 的集合，由 {@link ClassLoader#getResources(String)} 返回.
     * @throws IOException IO 异常
     */
    protected static List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    /**
     * 如果 {@link AbstractVfs} 的实现类对当前环境有效，则返回 true.
     *
     * @return 布尔值
     */
    public abstract boolean isValid();

    /**
     * 递归列出由 URL 标识的资源的子项的所有资源的完整资源路径.
     *
     * @param url url.
     * @param forPath URL 标识的资源的路径，通常，这是传递给 {@link #getResources(String)} 以获取资源 URL 的值.
     * @return 资源集合.
     * @throws IOException IO 异常
     */
    protected abstract List<String> list(URL url, String forPath) throws IOException;

    /**
     * 递归列出作为在指定路径中找到的所有资源的子项的所有资源的完整资源路径.
     *
     * @param path 资源路径.
     * @return 资源集合
     * @throws IOException IO 异常
     */
    public List<String> list(String path) throws IOException {
        List<String> names = new ArrayList<>();
        for (URL url : getResources(path)) {
            names.addAll(list(url, path));
        }
        return names;
    }

}
