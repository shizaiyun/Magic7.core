package org.magic7.utils;

import java.util.Date;
import java.util.List;

import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.domain.MagicRegionCodeLnk;
import org.magic7.core.service.MagicSpaceHandler;
import org.magic7.core.service.impl.MagicServiceImpl;
import org.magic7.dynamic.loader.MagicLoaderUtils;
import org.springframework.beans.BeanUtils;

public class MagicUtil {
	private static MagicServiceImpl service = MagicServiceImpl.getInstance();
	public static void createSpaceAndRegion(String spaceName,String tabLayout,String regionName,Integer dimensionNum,String description,Boolean multiply,Integer regionType,int seq){
		MagicSpace space = service.getSpaceByName(spaceName);
		if(space==null) {
			space = new MagicSpace();
			space.setName(spaceName);
			space.setValid(true);
			space.setDescription(description);
			space.setCreateDate(new Date());
			space.setTabLayout(tabLayout);
			service.saveSpace(space);
		}
		MagicSpaceRegion spaceRegion = new MagicSpaceRegion();
		spaceRegion.setSpaceId(space.getId());
		spaceRegion.setSpaceName(space.getName());
		spaceRegion.setName(regionName);
		spaceRegion.setDescription(description);
		spaceRegion.setSeq(seq);
		spaceRegion.setMultiply(multiply);
		spaceRegion.setRegionType(regionType);
		spaceRegion.setDimensionNum(dimensionNum);
		service.saveSpaceRegion(spaceRegion);
	}
	public static MagicDimension createLnkDimension(MagicSpace space,MagicSpaceRegion spaceRegion,String description,String name,
			String displayName,Integer pageType,Integer queryType,Integer valueType,String entityName,String url,Integer seq) {
		return createDimension(space,spaceRegion,description,name,displayName,true,
				pageType,false,false,entityName,MagicDimension.Destination.FOR_DATA.getCode(),queryType,seq++,
				valueType,false,null);
	}
	public static MagicDimension createNormalDimension(MagicSpace space,MagicSpaceRegion spaceRegion,String description,String name,
			String displayName,Integer pageType,Integer queryType,Integer valueType,Integer seq) {
		return createDimension(space,spaceRegion,description,name,displayName,false,
				pageType,false,true,null,MagicDimension.Destination.FOR_DATA.getCode(),queryType,seq++,
				valueType,false,null);
	}
	public static MagicDimension createDimension(MagicSpace space,MagicSpaceRegion spaceRegion,String description,String name,
			String displayName,Boolean isLnk,Integer pageType,Boolean isRequired,Boolean isVirtual,String entityName,
			Integer destination,Integer queryType,Integer seq,Integer valueType,Boolean dualLnk,String dualEntityName ) {
		MagicDimension dimension = new MagicDimension();
		dimension.setDescription(description);
		dimension.setName(name);
		dimension.setDisplayName(displayName);
		dimension.setLnk(isLnk);
		dimension.setPageType(pageType);
		dimension.setRequired(isRequired);
		dimension.setSeq(seq);
		dimension.setSpaceId(space.getId());
		dimension.setSpaceName(space.getName());
		dimension.setSpaceRegionId(spaceRegion.getId());
		dimension.setSpaceRegionName(spaceRegion.getName());
		dimension.setValueType(valueType);
		dimension.setVirtual(isVirtual);
		dimension.setRelationEntityName(entityName);
		dimension.setDestination(destination);
		dimension.setQueryType(queryType);
		dimension.setDualLnk(dualLnk);
		dimension.setDualEntityName(dualEntityName);
		return dimension;
	}
	public static void bindView(MagicDimension dimension,MagicSpaceRegionView view,Integer seq ) {
		MagicSpaceRegionViewItem viewItem = new MagicSpaceRegionViewItem();
		BeanUtils.copyProperties(view, viewItem);
		viewItem.setId(null);
		viewItem.setName(dimension.getDescription());
		viewItem.setViewId(view.getId());
		viewItem.setViewName(view.getName());
		viewItem.setDimensionId(dimension.getId());
		viewItem.setPageAction(false);
		viewItem.setSeq(seq);
		if(dimension.getVisible()!=null){
			viewItem.setVisible(dimension.getVisible());
		}
		service.saveSpaceRegionViewItem(viewItem);
	}
	public static MagicSpaceRegionView createView(MagicSpace space,MagicSpaceRegion spaceRegion,String viewName, Integer destination) {
		MagicSpaceRegionView view = new MagicSpaceRegionView();
		view.setSpaceId(space.getId());
		view.setSpaceRegionId(spaceRegion.getId());
		view.setName(viewName);
		view.setSpaceName(space.getName());
		view.setSpaceRegionName(spaceRegion.getName());
		view.setDestination(destination);
		service.saveSpaceRegionView(view);
		return view;
	}
	public static MagicRegionCodeLnk getJavaCodeLnk(MagicSpace space,MagicSpaceRegion spaceRegion,String methodName,String importsClass,String parameterNames,String code) {
		MagicCodeLib lib = MagicLoaderUtils.createJavaCodeLib(space.getName(),spaceRegion.getName(),
				methodName,importsClass,parameterNames,code);
		MagicRegionCodeLnk lnk = service.getCodeLnk(space.getName(), spaceRegion.getName(), lib.getSignature());
		if(lnk!=null)
			return lnk;
		MagicSpaceHandler.saveCodeLib(lib);
		lnk = new MagicRegionCodeLnk();
		lnk.setSpaceId(space.getId());
		lnk.setSpaceName(space.getName());
		lnk.setRegionId(spaceRegion.getId());
		lnk.setRegionName(spaceRegion.getName());
		lnk.setCodeLidId(lib.getId());
		lnk.setCodeName(lib.getName());
		lnk.setSignature(lib.getSignature());
		lnk.setParameterNames(lib.getParameterNames());
		service.saveReginCodeLnk(lnk);
		return lnk;
	}
	public static void createSpaceAndRegion(String spaceName,String regionName,String description,Boolean multiply,Integer regionType,int seq){
		MagicSpace space = service.getSpaceByName(spaceName);
		if(space==null) {
			space = new MagicSpace();
			space.setName(spaceName);
			space.setValid(true);
			space.setDescription(description);
			space.setCreateDate(new Date());
			space.setTabLayout(MagicSpace.TabLayout.HORIZONTAL.getName());
			service.saveSpace(space);
		}
		MagicSpaceRegion spaceRegion = new MagicSpaceRegion();
		spaceRegion.setSpaceId(space.getId());
		spaceRegion.setSpaceName(space.getName());
		spaceRegion.setName(regionName);
		spaceRegion.setDescription(description);
		spaceRegion.setSeq(seq);
		spaceRegion.setMultiply(multiply);
		spaceRegion.setExtraEditor(multiply);
		spaceRegion.setRegionType(regionType);
		spaceRegion.setDimensionNum(2);
		service.saveSpaceRegion(spaceRegion);
	}
	public static void createSpaceAndRegion(String spaceName,String regionName,Integer dimensionNum,String description,Boolean multiply,Integer regionType,int seq){
		MagicSpace space = service.getSpaceByName(spaceName);
		if(space==null) {
			space = new MagicSpace();
			space.setName(spaceName);
			space.setValid(true);
			space.setDescription(description);
			space.setCreateDate(new Date());
			space.setTabLayout(MagicSpace.TabLayout.HORIZONTAL.getName());
			service.saveSpace(space);
		}
		MagicSpaceRegion spaceRegion = new MagicSpaceRegion();
		spaceRegion.setSpaceId(space.getId());
		spaceRegion.setSpaceName(space.getName());
		spaceRegion.setName(regionName);
		spaceRegion.setDescription(description);
		spaceRegion.setSeq(seq);
		spaceRegion.setMultiply(multiply);
		spaceRegion.setRegionType(regionType);
		spaceRegion.setDimensionNum(dimensionNum);
		service.saveSpaceRegion(spaceRegion);
	}
	public static void bindChoiceItem(MagicDimension dimension,String choiceName,String choiceCode,String choiceCodeItems[],String choiceItems[]) {
		if(choiceItems==null||choiceItems.length==0)
			throw new RuntimeException(choiceName+"'s choiceItems[] is null") ;
		List<MagicChoiceItem> severities = service.listChoiceItem(null,choiceCode);
		if(severities!=null&&severities.size()==0) {
			for(int i=0;i<choiceItems.length;i++){
				MagicChoiceItem choiceItem = new MagicChoiceItem();
				choiceItem.setChoiceCode(choiceCode);
				choiceItem.setChoiceName(choiceName);
				choiceItem.setValueCode(choiceCodeItems[i]);
				choiceItem.setValueName(choiceItems[i]);
				service.saveChoiceItem(choiceItem);
			}
		}
		dimension.setChoiceName(choiceName);
		dimension.setChoiceCode(choiceCode);
	}
}
