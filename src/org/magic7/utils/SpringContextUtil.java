package org.magic7.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextUtil implements ServletContextListener {
	private static WebApplicationContext springContext;

	public SpringContextUtil() 
	{
		super();
	}

	public void contextInitialized(ServletContextEvent event) 
	{
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	}


	public void contextDestroyed(ServletContextEvent event)
	{
	
	}

	public static ApplicationContext getApplicationContext() 
	{
		return springContext;
	}


}
