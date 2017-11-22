package com.liz.fs.common.helper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.ContextLoaderListener;

/**
 * Application Lifecycle Listener implementation class myListener
 *
 */
public class StartupListener extends ContextLoaderListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public StartupListener() {
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  {
        System.out.println("File system is running...");
        super.contextInitialized(event);
        SpringUtils.getBean(SystemService.class).init();
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
        // TODO Auto-generated method stub
    }

}
