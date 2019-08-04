package com.blinkfox.fenix.loader;

import com.blinkfox.zealot.config.ZealotConfigManager;
import com.blinkfox.zealot.config.entity.XmlContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Zealot配置的servlet监听器的初始化加载类
 * @author blinkfox on 2016/10/30.
 */
public class ZealotConfigLoader implements ServletContextListener {

    /** zealotConfig对应的类全路径常量字符串. */
    private static final String CONFIG_CLASS = "zealotConfigClass";

    /** zealot xml 文件所在的位置(可以是xml文件，也可以是目录)，如果是多个位置则用逗号(',')分割. */
    private static final String XML_LOCATIONS = "zealotXmlLocations";

    /** zealot 自定义标签处理器所在的位置(只是目录),如果是多个位置则用逗号(',')分割. */
    private static final String HANDLER_LOCATIONS = "zealotHandlerLocations";

    /**
     * ZealotConfig销毁时执行的方法.
     * @param event 上下文事件对象
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        XmlContext.INSTANCE.getXmlPathMap().clear();
    }

    /**
     * 应用服务器启动时执行.
     * @param event 上下文事件对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 根据配置的zealotConfig路径加载配置文件信息到缓存中
        String configClass = event.getServletContext().getInitParameter(CONFIG_CLASS);
        String xmlLocations = event.getServletContext().getInitParameter(XML_LOCATIONS);
        String handlerLocations = event.getServletContext().getInitParameter(HANDLER_LOCATIONS);
        ZealotConfigManager.getInstance().initLoad(configClass, xmlLocations, handlerLocations);
    }

}