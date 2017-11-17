package org.magic7.utils;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.dynamic.loader.MagicLoaderUtils;

public class RegionClasGenerateUtil implements ServletContextListener {

	public RegionClasGenerateUtil() {
		super();
	}

	public void contextInitialized(ServletContextEvent event) {
		MagicService service = MagicServiceFactory.getMagicService();
		List<MagicSpace> magicSpaces = service.listSpace(null, null, null, null);
		for (MagicSpace magicSpace : magicSpaces) {
			List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(magicSpace.getId(), " seq ", 0, 1000);
			for (MagicSpaceRegion spaceRegion : spaceRegions) {
				MagicLoaderUtils.generateRegionClass(magicSpace.getName(), spaceRegion.getName());
			}
		}
	}

	public void contextDestroyed(ServletContextEvent event) {

	}

}
