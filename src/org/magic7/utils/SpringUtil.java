package org.magic7.utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class SpringUtil implements ApplicationContextAware{
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		context = contex;
	}
	public static ApplicationContext getContext() {
		return context;
	}
	@SuppressWarnings("unchecked")
	public static <X> X getBean(String beanname)
	{
		return (X) getContext().getBean(beanname);
	}
	public String getAppRootPath() {
		if (WebApplicationContext.class
				.isAssignableFrom(context.getClass())) {
			WebApplicationContext wac = (WebApplicationContext) context;
			String appRootPath = wac.getServletContext().getRealPath("/");
			if (appRootPath.endsWith("/") || appRootPath.endsWith("\\")) {
				appRootPath = appRootPath
						.substring(0, appRootPath.length() - 1);
			}
			return appRootPath;
		} else {
			return null;
		}
	}
	
	public static <T> Map<String, T> getBeansOfTypeMap(Class<T> type) {
		return context.getBeansOfType(type);
	}

}