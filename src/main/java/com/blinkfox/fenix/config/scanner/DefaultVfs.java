package com.blinkfox.fenix.config.scanner;

import com.blinkfox.zealot.exception.ZealotException;
import com.blinkfox.zealot.helpers.IoHelper;
import com.blinkfox.zealot.log.Log;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * DefaultVfs.
 * <p>注:此文件中的大部分内容是从Mybatis中参考过来的.</p>
 *
 * @author blinkfox on 2018-04-25.
 */
public class DefaultVfs {

    private static final Log log = Log.get(XmlScanner.class);

    /** 路径分割符. */
    private static final String PATH_SP = "/";

    /** for循环最大执行时限. */
    private static final long MAX_LIMIT = 3000;
    
    /** 文件协议标识符. */
    private static final String FILE_PROTOCOL = "file";

    /** The magic header that indicates a JAR (ZIP) file. */
    private static final byte[] JAR_MAGIC = { 'P', 'K', 3, 4 };

    /**
     * 获取新实例.
     * @return DefaultVfs实例
     */
    public static DefaultVfs newInstance() {
        return new DefaultVfs();
    }

    /**
     * Recursively list the full resource path of all the resources that are children of the
     * resource identified by a URL.
     *
     * @param url The URL that identifies the resource to list.
     * @param path The path to the resource that is identified by the URL. Generally, this is the
     *            value passed to get the resource URL.
     * @return A list containing the names of the child resources.
     * @throws IOException If I/O errors occur
     */
    public List<String> list(URL url, String path) throws IOException {
        InputStream is = null;
        try {
            List<String> resources = new ArrayList<String>();

            // First, try to find the URL of a JAR file containing the requested resource. If a JAR
            // file is found, then we'll list child resources by reading the JAR.
            URL jarUrl = findJarForResource(url);
            if (jarUrl != null) {
                is = jarUrl.openStream();
                return listResources(new JarInputStream(is), path);
            }

            List<String> children = new ArrayList<String>();
            children = this.getResourceUrls(url, path, is, children);

            // The URL prefix to use when recursively listing child resources
            String prefix = url.toExternalForm();
            if (!prefix.endsWith(PATH_SP)) {
                prefix = prefix + PATH_SP;
            }

            // Iterate over immediate children, adding files and recursing into directories
            for (String child : children) {
                String resourcePath = path + PATH_SP + child;
                resources.add(resourcePath);
                URL childUrl = new URL(prefix + child);
                resources.addAll(list(childUrl, resourcePath));
            }

            return resources;
        } finally {
            IoHelper.closeQuietly(is);
        }
    }

    private List<String> getResourceUrls(URL url, String path, InputStream is,
            List<String> children) throws IOException {
        JarInputStream jarInput = null;
        BufferedReader reader = null;

        try {
            if (isJar(url)) {
                // Some versions of JBoss VFS might give a JAR stream even if the resource
                // referenced by the URL isn't actually a JAR
                is = url.openStream();
                jarInput = new JarInputStream(is);
                for (JarEntry entry; (entry = jarInput.getNextJarEntry()) != null;) {
                    children.add(entry.getName());
                }
                jarInput.close();
            } else {
                is = url.openStream();
                reader = new BufferedReader(new InputStreamReader(is));
                List<String> lines = new ArrayList<String>();
                for (String line; (line = reader.readLine()) != null;) {
                    lines.add(line);
                    if (this.getResources(path + PATH_SP + line).isEmpty()) {
                        lines.clear();
                        break;
                    }
                }

                if (!lines.isEmpty()) {
                    children.addAll(lines);
                }
            }
        } catch (FileNotFoundException e) {
            children = this.getUrls(url, children, e);
        } finally {
            IoHelper.closeQuietly(jarInput);
            IoHelper.closeQuietly(reader);
            IoHelper.closeQuietly(is);
        }
        return children;
    }

    private List<String> getUrls(URL url, List<String> children, FileNotFoundException e) throws IOException {
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            File file = new File(url.getFile());
            if (file.isDirectory()) {
                children = Arrays.asList(file.list());
            }
        } else {
            // No idea where the exception came from so rethrow it
            throw e;
        }
        return children;
    }

    private List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    /**
     * List the names of the entries in the given {@link JarInputStream} that begin with the
     * specified {@code path}. Entries will match with or without a leading slash.
     *
     * @param jar The JAR input stream
     * @param path The leading path to match
     * @return The names of all the matching entries
     * @throws IOException If I/O errors occur
     */
    private List<String> listResources(JarInputStream jar, String path) throws IOException {
        // Include the leading and trailing slash when matching names
        if (!path.startsWith(PATH_SP)) {
            path = PATH_SP + path;
        }
        if (!path.endsWith(PATH_SP)) {
            path = path + PATH_SP;
        }

        // Iterate over the entries and collect those that begin with the requested path
        List<String> resources = new ArrayList<String>();
        for (JarEntry entry; (entry = jar.getNextJarEntry()) != null;) {
            if (!entry.isDirectory()) {
                // Add leading slash if it's missing
                String name = entry.getName();
                StringBuilder sb = new StringBuilder("");
                if (!name.startsWith(PATH_SP)) {
                    name = sb.append(PATH_SP).append(name).toString();
                }

                // Check file name
                if (name.startsWith(path)) {
                    // Trim leading slash
                    resources.add(name.substring(1));
                }
            }
        }
        return resources;
    }

    /**
     * 从jar包中找到URL资源.
     * @param url url
     * @return url
     */
    private URL findJarForResource(URL url) {
        // 如果URL的文件部分本身是一个URL，那么该URL可能指向JAR
        try {
            long startTime = System.currentTimeMillis();
            while (true) {
                url = new URL(url.getFile());
                if (System.currentTimeMillis() - startTime > MAX_LIMIT) {
                    break;
                }
            }
        } catch (MalformedURLException expected) {
            // This will happen at some point and serves as a break in the loop
        }

        // Look for the .jar extension and chop off everything after that
        StringBuilder jarUrl = new StringBuilder(url.toExternalForm());
        int index = jarUrl.lastIndexOf(".jar");
        if (index >= 0) {
            jarUrl.setLength(index + 4);
        } else {
            return null;
        }

        // Try to open and test it
        try {
            URL testUrl = new URL(jarUrl.toString());
            if (this.isJar(testUrl)) {
                return testUrl;
            }

            // WebLogic fix: check if the URL's file exists in the filesystem.
            jarUrl.replace(0, jarUrl.length(), testUrl.getFile());
            File file = new File(jarUrl.toString());

            // File name might be URL-encoded
            if (!file.exists()) {
                file = new File(URLEncoder.encode(jarUrl.toString(), "UTF-8"));
            }

            if (file.exists()) {
                testUrl = file.toURI().toURL();
                if (isJar(testUrl)) {
                    return testUrl;
                }
            }
        } catch (MalformedURLException e) {
            log.warn("Invalid JAR URL: " + jarUrl);
        } catch (UnsupportedEncodingException e) {
            throw new ZealotException("Unsupported encoding?  UTF-8?  That's unpossible.");
        }

        return null;
    }

    /**
     * 根据URL判断是否是jar.
     * @param url url
     * @return 布尔值
     */
    private boolean isJar(URL url) {
        return isJar(url, new byte[JAR_MAGIC.length]);
    }

    private boolean isJar(URL url, byte[] buffer) {
        InputStream is = null;
        try {
            is = url.openStream();
            is.read(buffer, 0, JAR_MAGIC.length);
            if (Arrays.equals(buffer, JAR_MAGIC)) {
                return true;
            }
        } catch (Exception e) {
            // 如果发生异常，则表明不是jar.
        } finally {
            IoHelper.closeQuietly(is);
        }

        return false;
    }

}