package org.magic7.core.service;

import java.lang.reflect.Method;

public class MagicServiceFactory {
	public static MagicService getMagicService() {
		try {
			Method method = Class.forName("org.magic7.core.service.impl.MagicServiceImpl").getMethod("getInstance");
			
			return (MagicService)method.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
