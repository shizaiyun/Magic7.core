package org.magic7.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.dao.MagicDao;
import org.magic7.core.dao.SupervisionDao;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicRowItem;
import org.magic7.core.domain.MagicChoice;
import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.domain.MagicTriggerAssembler;
import org.magic7.core.domain.supervision.MagicMenu;
import org.magic7.core.domain.supervision.MagicMenuConnector;
import org.magic7.core.domain.supervision.MagicPage;
import org.magic7.core.domain.supervision.MagicPageAssembler;
import org.magic7.core.domain.supervision.MagicParticipant;
import org.magic7.core.domain.supervision.MagicPaticipantGroup;
import org.magic7.core.domain.MagicObject;
import org.magic7.core.domain.MagicObjectRegion;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.domain.MagicRegionCodeLnk;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.ServiceStaticInfo;
import org.magic7.utils.ServiceUtil;

public class MagicServiceImpl implements MagicService {
	private static MagicServiceImpl instance = new MagicServiceImpl();
	private MagicServiceImpl() {}
	private static final MagicDao magicDao = MagicDao.getInstance();
	private static final SupervisionDao supervisionDao = SupervisionDao.getInstance();
	public static MagicServiceImpl getInstance() {
		return instance;
	}
	public Boolean saveMagicObject(MagicObject magicObject){
		ServiceUtil.notNull(magicObject, "magicObject is null");
		ServiceUtil.notNull(magicObject.getSpaceId(), "magicObject.spaceId is null");
		ServiceUtil.notNull(magicObject.getSpaceName(), "magicObject.spaceName is null");
		if(magicObject.getCreateDate()==null)
			magicObject.setCreateDate(new Date());
		return magicDao.saveMagicObject(magicObject);
	}
	public Boolean saveRow(MagicRegionRow row) {
		ServiceUtil.notNull(row, "row is null");
		ServiceUtil.notNull(row.getRegionId(), "row.regionId is null");
		ServiceUtil.notNull(row.getObjectId(), "row.objectId is null");
		ServiceUtil.notNull(row.getObjectId(), "row.spaceId is null");
		ServiceUtil.notNull(row.getSpaceRegionId(), "row.spaceRegionId is null");
		ServiceUtil.notNull(row.getSpaceName(), "row.spaceName is null");
		ServiceUtil.notNull(row.getRegionName(), "row.regionName is null");
		List<MagicSuperRowItem> values = row.getRowItems();
		ServiceUtil.notNull(values, "row.values is null");
		if(row.getCreateDate()==null)
			row.setCreateDate(new Date());
		if(row.getValid()==null)
			row.setValid(true);
		return magicDao.saveRow(row);
	}
	public Boolean saveRowItem(MagicSuperRowItem item) {
		ServiceUtil.notNull(item, "item is null");
		ServiceUtil.notNull(item.getRowId(), "item.rowId is null");
		ServiceUtil.notNull(item.getName(), "item.name is null");
		ServiceUtil.notNull(item.getPageType(), "item.pageType is null");
		ServiceUtil.notNull(item.getRequired(), "item.required is null");
		ServiceUtil.notNull(item.getRegionId(), "item.regionId is null");
		ServiceUtil.notNull(item.getSeq(), "item.seq is null");
		ServiceUtil.notNull(item.getSpaceId(), "item.spaceId is null");
		ServiceUtil.notNull(item.getObjectId(), "item.objectId is null");
		ServiceUtil.notNull(item.getSpaceRegionId(), "item.spaceRegionId is null");
		ServiceUtil.notNull(item.getValueType(), "item.valueType is null");
		ServiceUtil.notNull(item.getDimensionId(), "item.dimensionId is null");
		ServiceUtil.notNull(item.getDimensionName(), "item.dimensionName is null");
		ServiceUtil.notNull(item.getLnk(), "item.lnk is null");
		ServiceUtil.notNull(item.getSpaceName(), "item.spaceName is null");
		ServiceUtil.notNull(item.getRegionName(), "item.regionName is null");
		return magicDao.saveRowItem(item);
	}
	public List<MagicDimension> listDimension(String spaceId,String spaceName, String spaceRegionId, 
			String spaceRegionName, String viewName, Integer destination, String orderBy) {
		return magicDao.listDimension(spaceId, spaceName, spaceRegionId, spaceRegionName, viewName, destination, orderBy);
	}
	public Boolean deleteRowItemByRowId(String rowId) {
		ServiceUtil.notNull(rowId, "rowId is null");
		return magicDao.deleteRowItemByRowId(rowId);
	}
	public Boolean deleteRowById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return magicDao.deleteRowById(id);
	}
	public MagicRegionRow getRowById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return magicDao.getRowById(id);
	}
	public Map<String,MagicSuperRowItem> getRowItemMapByRowId(String rowId) {
		List<MagicSuperRowItem> items = this.listRowItem(null, rowId, null, null);
		HashMap<String, MagicSuperRowItem> values = new HashMap<>();
		for(MagicSuperRowItem item:items)
			values.put(item.getName(), item);
		return values;
	}
	public Boolean saveObjectRegion(MagicObjectRegion region) {
		ServiceUtil.notNull(region, "region is null");
		ServiceUtil.notNull(region.getName(), "region.name is null");
		ServiceUtil.notNull(region.getSpaceId(), "region.spaceId is null");
		ServiceUtil.notNull(region.getObjectId(), "region.objectId is null");
		ServiceUtil.notNull(region.getSpaceRegionId(), "region.spaceRegionId is null");
		ServiceUtil.notNull(region.getSeq(), "region.seq is null");
		ServiceUtil.notNull(region.getRegionType(), "region.regionType is null");
		return magicDao.saveSpaceRegion(region);
	}
	public MagicObjectRegion getObjectRegionById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return magicDao.getObjectRegionById(id);
	}
	public MagicObject getMagicObjectById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return magicDao.getMagicObjectById(id);
	}
	public List<MagicSuperRowItem> listRowItem(String partition, String rowId, String viewName, String orderBy) {
		ServiceUtil.notNull(rowId, "rowId is null");
		return magicDao.listRowItem(partition, rowId,viewName, orderBy);
	}
	public List<MagicSuperRowItem> listRowItem(String partition, String spaceName,
			String regionName, String viewName,String displayName,String orderBy,Integer start, Integer count) {
		return magicDao.listRowItem(partition, spaceName,regionName, viewName,displayName,orderBy, start, count);
	}
	public MagicObjectRegion getObjectRegion(String objectId,String spaceRegionName){
		ServiceUtil.notNull(objectId, "objectId is null");
		ServiceUtil.notNull(spaceRegionName, "businessType is null");
		return magicDao.getObjectRegion(objectId,spaceRegionName);
	}
	public Boolean saveSpaceRegion(MagicSpaceRegion spaceRegion) {
		ServiceUtil.notNull(spaceRegion, "spaceRegion is null");
		ServiceUtil.notNull(spaceRegion.getName(), "spaceRegion.name is null");
		ServiceUtil.notNull(spaceRegion.getDescription(), "spaceRegion.description is null");
		ServiceUtil.notNull(spaceRegion.getMultiply(), "spaceRegion.multiply is null");
		ServiceUtil.notNull(spaceRegion.getSeq(), "spaceRegion.seq is null");
		ServiceUtil.notNull(spaceRegion.getSpaceId(), "spaceRegion.spaceId is null");
		ServiceUtil.notNull(spaceRegion.getSpaceName(), "spaceRegion.spaceName is null");
		ServiceUtil.notNull(spaceRegion.getRegionType(), "spaceRegion.regionType is null");
		return magicDao.saveMagicSpaceRegion(spaceRegion);
	}
	public Boolean deleteMagicObjectById(String objectId) {
		ServiceUtil.notNull(objectId, "objectId is null");
		this.deleteMagicObjectRegionByObjId(objectId);
		this.deleteRowItemByObjectId(objectId);
		this.deleteMagicRegionRowByObjId(objectId);
		return magicDao.deleteMagicObjectById(objectId);
	}
	public Boolean deleteMagicObjectRegionByObjId(String objectId) {
		ServiceUtil.notNull(objectId, "objectId is null");
		return magicDao.deleteMagicObjectRegionByObjId(objectId);
	}
	public Boolean deleteRowItemByObjectId(String objectId) {
		ServiceUtil.notNull(objectId, "objectId is null");
		return magicDao.deleteRowItemByObjectId(objectId);
	}
	public Boolean deleteMagicRegionRowByObjId(String objectId) {
		ServiceUtil.notNull(objectId, "objectId is null");
		return magicDao.deleteMagicRegionRowByObjId(objectId);
	}
	public List<MagicObjectRegion> listMagicObjectRegion(String objectId,String spaceId,Integer businessType,
			String orderBy,Integer start,Integer count) {
		return magicDao.listMagicObjectRegion(objectId, spaceId, businessType, orderBy, start, count);
	}
	public Boolean saveSpace(MagicSpace space) {
		ServiceUtil.notNull(space, "space is null");
		ServiceUtil.notNull(space.getName(), "space.name is null");
		ServiceUtil.notNull(space.getDescription(), "space.description is null");
		return magicDao.saveSpace(space);
	}
	public Boolean saveDimension(MagicDimension dimension) {
		ServiceUtil.notNull(dimension, "dimension is null");
		ServiceUtil.notNull(dimension.getLnk(), "dimension.lnk is null");
		if(dimension.getLnk()) 
			ServiceUtil.notNull(dimension.getRelationEntityName(), "dimension.relationEntityName is null");
		ServiceUtil.notNull(dimension.getName(), "dimension.name is null");
		ServiceUtil.notNull(dimension.getDisplayName(), "dimension.displayName is null");
		ServiceUtil.notNull(dimension.getPageType(), "dimension.pageType is null");
		ServiceUtil.notNull(dimension.getRequired(), "dimension.required is null");
		ServiceUtil.notNull(dimension.getSeq(), "dimension.seq is null");
		ServiceUtil.notNull(dimension.getSpaceId(), "dimension.spaceId is null");
		ServiceUtil.notNull(dimension.getSpaceRegionId(), "dimension.regionId is null");
		ServiceUtil.notNull(dimension.getValueType(), "dimension.valueType is null");
		ServiceUtil.notNull(dimension.getVirtual(), "dimension.virtual is null");
		ServiceUtil.notNull(dimension.getSpaceName(), "dimension.spaceName is null");
		ServiceUtil.notNull(dimension.getSpaceRegionName(), "dimension.spaceName is null");
		if(dimension.getDestination()==null)
			dimension.setDestination(MagicDimension.Destination.FOR_DATA.getCode());
		return magicDao.saveMagicDimension(dimension);
	}
	public MagicDimension getDimensionById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return magicDao.getDimensionById(id);
	}
	public List<MagicRegionRow> listRow(String partition,String spaceName,String regionName,String objectId,
			Boolean valid,List<MagicDimension> queryConditions,String orderBy, Integer start, Integer count) {
		return this.listRow(partition, spaceName, regionName, null, objectId, valid, queryConditions, orderBy, start, count);
	}
	public List<MagicRegionRow> listRow(String partition,String spaceName,String regionName,String displayName,String objectId,
			Boolean valid,List<MagicDimension> searchCriterias,String orderBy, Integer start, Integer count) {
		return magicDao.listRow(partition, spaceName, regionName, displayName, objectId, valid, searchCriterias, orderBy, start, count);
	}
	public MagicDimension getDimension(String spaceName,String regionName, String dimensionName, Integer destination) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(dimensionName, "dimensionName is null");
		return magicDao.getDimension(spaceName, regionName, dimensionName, destination);
	}
	public MagicSpaceRegion getSpaceRegion(String spaceName, String regionName) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		return magicDao.getSpaceRegion(spaceName, regionName);
	}
	public MagicSpace getSpaceByName(String name) {
		ServiceUtil.notNull(name, "name is null");
		return magicDao.getSpaceByName(name);
	}
	public List<MagicObjectRegion> listObjectRegion(String spaceName,String objectId) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(objectId, "objectId is null");
		MagicSpace  space = this.getSpaceByName(spaceName);
		ServiceUtil.notNull(space, "space is null");
		return this.listMagicObjectRegion(objectId, space.getId(), null, " seq ", 0, 1000);
	}
	public List<MagicSpaceRegion> listSpaceRegion(String spaceName,String spaceId,String orderBy,Integer start, Integer count) {
		return magicDao.listSpaceRegion(spaceName, spaceId, orderBy, start, count);
	}
	public Boolean saveSpaceRegionView(MagicSpaceRegionView view) {
		ServiceUtil.notNull(view, "view is null");
		ServiceUtil.notNull(view.getName(), "view.name is null");
		ServiceUtil.notNull(view.getSpaceId(), "view.spaceId is null");
		ServiceUtil.notNull(view.getSpaceName(), "view.spaceName is null");
		ServiceUtil.notNull(view.getSpaceRegionId(), "view.spaceId is null");
		ServiceUtil.notNull(view.getSpaceRegionName(), "view.spaceName is null");
		if(view.getLayout()==null)
			view.setLayout(MagicSpaceRegionView.Layout.NORMAL.getName());
		return magicDao.saveSpaceRegionView(view);
	}
	public Integer listRowCount(String partition, String spaceName,String regionName,
			String displayName, String objectId, Boolean valid, List<MagicDimension> queryConditions) {
		return magicDao.listRowCount(partition, spaceName, regionName, displayName, objectId, valid, queryConditions);
	}
	public Boolean saveSpaceRegionViewItem(MagicSpaceRegionViewItem item) {
		ServiceUtil.notNull(item, "item is null");
		ServiceUtil.notNull(item.getName(), "item.name is null");
		ServiceUtil.notNull(item.getSpaceId(), "item.spaceId is null");
		ServiceUtil.notNull(item.getSpaceName(), "item.spaceName is null");
		ServiceUtil.notNull(item.getSpaceRegionId(), "item.spaceRegionId is null");
		ServiceUtil.notNull(item.getSpaceRegionName(), "item.spaceRegionName is null");
		if(StringUtils.isEmpty(item.getLayout()))
			item.setLayout(MagicSpaceRegionView.Layout.NORMAL.getName());
		return magicDao.saveSpaceRegionViewItem(item);
	}
	public List<MagicSpaceRegionViewItem> listSpaceRegionViewItem(String spaceName,String regionName,String viewName, String orderBy) {
		return magicDao.listSpaceRegionViewItem(spaceName, regionName, viewName, orderBy);
	}
	public MagicSpaceRegionViewItem getSpaceRegionViewItem(String spaceId,String spaceRegionId,
			String viewName,String dimensionId) {
		ServiceUtil.notNull(spaceId, "spaceId is null");
		ServiceUtil.notNull(spaceRegionId, "spaceRegionId is null");
		ServiceUtil.notNull(viewName, "viewName is null");
		ServiceUtil.notNull(dimensionId, "dimensionId is null");
		return magicDao.getSpaceRegionViewItem(spaceId, spaceRegionId, viewName, dimensionId);
	}
	public MagicSpaceRegionViewItem getSpaceRegionViewItemWithName(String spaceName,String regionName,
			String viewName,String dimensionId) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(viewName, "viewName is null");
		ServiceUtil.notNull(dimensionId, "dimensionId is null");
		return magicDao.getSpaceRegionViewItemWithName(spaceName, regionName, viewName, dimensionId);
	}
	public Boolean saveChoiceItem(MagicChoiceItem choiceItem){
		ServiceUtil.notNull(choiceItem, "choiceItem is null");
		ServiceUtil.notNull(choiceItem.getChoiceName(), "choiceItem.choiceName is null");
		ServiceUtil.notNull(choiceItem.getValueCode(), "choiceItem.valueCode is null");
		ServiceUtil.notNull(choiceItem.getValueName(), "choiceItem.valueName is null");
		return magicDao.saveChoiceItem(choiceItem);
	}
	public MagicSpaceRegionView getSpaceRegionView(String spaceName,String dimensionRegionName,
			String viewName) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		//ServiceUtil.notNull(dimensionRegionName, "spaceName is null");
		ServiceUtil.notNull(viewName, "viewName is null");
		return magicDao.getSpaceRegionView(spaceName, dimensionRegionName, viewName);
	}
	public Boolean saveCodeLib(MagicCodeLib lib) {
		ServiceUtil.notNull(lib, "lib is null");
		if(MagicCodeLib.CodeType.JAVA.getCode().equals(lib.getCodeType())) 
			ServiceUtil.notNull(lib.getSignature(), "lib.signature is null");
		return magicDao.saveCodeLib(lib);
	}
	public MagicCodeLib getCodeLibById(String id) {
		return magicDao.getCodeLibById(id);
	}
	public MagicRegionCodeLnk getCodeLnk(String spaceName,String regionName, String signature) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(signature, "signature is null");
		return magicDao.getCodeLnk(spaceName, regionName, signature);
	}
	public Boolean saveReginCodeLnk(MagicRegionCodeLnk codeLnk) {
		ServiceUtil.notNull(codeLnk, "codeLnk is null");
		ServiceUtil.notNull(codeLnk.getSpaceId(), "codeLnk.spaceId is null");
		ServiceUtil.notNull(codeLnk.getSpaceName(), "codeLnk.spaceName is null");
		ServiceUtil.notNull(codeLnk.getRegionId(), "codeLnk.regionId is null");
		ServiceUtil.notNull(codeLnk.getRegionName(), "codeLnk.regionName is null");
		ServiceUtil.notNull(codeLnk.getCodeLidId(), "codeLnk.codeLibId is null");
		ServiceUtil.notNull(codeLnk.getCodeName(), "codeLnk.codeName is null");
		ServiceUtil.notNull(codeLnk.getSignature(), "codeLnk.signature is null");
		return magicDao.saveReginCodeLnk(codeLnk);
	}
	public List<MagicCodeLib> listCodeLibWithLnk(String spaceName,String regionName, Integer codeType) {
		return magicDao.listCodeLibWithLnk(spaceName, regionName, codeType);
	}
	public Boolean isFreshValue(String partition,String spaceName,String regionName,String displayName,String value) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(displayName, "displayName is null");
		ServiceUtil.notNull(value, "value is null");
		if(StringUtils.isEmpty(partition))
			partition = ServiceStaticInfo.TABLE_PREFIX;
		return magicDao.isFreshValue(partition, spaceName, regionName, displayName, value);
	}
	public List<MagicSuperRowItem> getRowItem(String partition,String spaceName,String regionName,String displayName,Object value) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(displayName, "displayName is null");
		ServiceUtil.notNull(value, "value is null");
		if(StringUtils.isEmpty(partition))
			partition = ServiceStaticInfo.TABLE_PREFIX;
		return magicDao.getRowItem(partition, spaceName, regionName, displayName, value);
	}
	public MagicCodeLib getJavaCodeLibBySignature(String signature) {
		ServiceUtil.notNull(signature, "signature is null");
		return magicDao.getJavaCodeLibBySignature(signature);
	}
	public List<MagicSpace> listSpace(String spaceName, String orderBy, Integer start, Integer count) {
		return magicDao.listSpace(spaceName, orderBy, start, count);
	}
	public MagicSpace getSpaceById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return (MagicSpace) magicDao.getObject(MagicSpace.class, id);
	}
	public MagicSpaceRegion getSpaceRegionById(String regionId) {
		ServiceUtil.notNull(regionId, "regionId is null");
		return (MagicSpaceRegion) magicDao.getObject(MagicSpaceRegion.class, regionId);
	}
	public List<MagicChoiceItem> listChoiceItem(String name, String code) {
		return magicDao.listChoiceItem(name, code);
	}
	public List<MagicSpaceRegionView> listSpaceRegionView(String spaceName,String regionName) {
		return magicDao.listSpaceRegionView(spaceName, regionName);
	}
	public MagicSpaceRegionView getViewById(String viewId) {
		ServiceUtil.notNull(viewId, "viewId is null");
		return (MagicSpaceRegionView) magicDao.getObject(MagicSpaceRegionView.class, viewId);
	}
	public MagicSpaceRegionViewItem getViewItemById(String itemId) {
		ServiceUtil.notNull(itemId, "itemId is null");
		return (MagicSpaceRegionViewItem) magicDao.getObject(MagicSpaceRegionViewItem.class, itemId);
	}
	public Boolean saveChoice(MagicChoice choice) {
		ServiceUtil.notNull(choice, "choice is null");
		ServiceUtil.notNull(choice.getChoiceName(), "choice.choiceName is null");
		return magicDao.save(choice);
	}
	public MagicChoice getChoiceById(String choiceId) {
		ServiceUtil.notNull(choiceId, "choiceId is null");
		return (MagicChoice) magicDao.getObject(MagicChoice.class, choiceId);
	}
	public List<MagicSpaceRegion> listSupplementSpaceRegion(String objectId, String spaceId, String orderBy,
			Integer start, Integer count) {
		return magicDao.listSupplementSpaceRegion(objectId, spaceId, orderBy, start, count);
	}
	public List<MagicChoice> listChoice(String name, String code) {
		return magicDao.listChoice(name, code);
	}
	public MagicChoiceItem getChoiceItemById(String choiceItemId) {
		ServiceUtil.notNull(choiceItemId, "choiceItemId is null");
		return (MagicChoiceItem) magicDao.getObject(MagicChoiceItem.class, choiceItemId);
	}
	public List<MagicCodeLib> listCodeLib(String name,String description, Integer codeType,String orderBy,Integer start,Integer count) {
		return magicDao.listCodeLib(name, description, codeType, orderBy, start, count);
	}
	public Boolean deleteCodeLnk(String codeId,String spaceName,String regionName) {
		ServiceUtil.notNull(codeId, "codeId is null");
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		return magicDao.deleteCodeLnk(codeId, spaceName, regionName);
	}
	public MagicTriggerAssembler getTriggerAssembler(String triggerName,String codeLibId,String dimensionId,Integer seq) {
		ServiceUtil.notNull(triggerName, "triggerName is null");
		ServiceUtil.notNull(codeLibId, "codeLibId is null");
		ServiceUtil.notNull(dimensionId, "dimensionId is null");
		ServiceUtil.notNull(seq, "seq is null");
		return magicDao.getTriggerAssembler(triggerName, codeLibId, dimensionId, seq);
	}
	public List<MagicTriggerAssembler> listTriggerAssembler(String triggerName,String spaceName,String regionName,String orderBy) {
		ServiceUtil.notNull(triggerName, "triggerName is null");
		ServiceUtil.notNull(spaceName, "spaceName is null");
		return magicDao.listMagicTriggerAssembler(triggerName, spaceName, regionName, orderBy);
	}
	public Boolean saveTriggerAssembler(MagicTriggerAssembler assembler) {
		ServiceUtil.notNull(assembler, "assembler is null");
		ServiceUtil.notNull(assembler.getCodeLibId(), "assembler.codeLibId is null");
		ServiceUtil.notNull(assembler.getTriggerName(), "assembler.triggerName is null");
		ServiceUtil.notNull(assembler.getDimensionId(), "assembler.dimensionId is null");
		ServiceUtil.notNull(assembler.getSpaceName(), "assembler.spaceName is null");
		ServiceUtil.notNull(assembler.getRegionName(), "assembler.regionName is null");
		ServiceUtil.notNull(assembler.getDisplayName(), "assembler.displayName is null");
		ServiceUtil.notNull(assembler.getSignature(), "assembler.signature is null");
		ServiceUtil.notNull(assembler.getCodeName(), "assembler.codeName is null");
		return magicDao.save(assembler);
	}
	public MagicTriggerAssembler getAssemblerById(String id) {
		ServiceUtil.notNull(id, "id is null");
		return (MagicTriggerAssembler) magicDao.getObject(MagicTriggerAssembler.class, id);
	}
	public Boolean deleteAssembler(MagicTriggerAssembler assembler) {
		ServiceUtil.notNull(assembler, "assembler is null");
		return magicDao.delete(assembler);
	}
	public List<String> listRowIds(String partition,String spaceName,String regionName,String displayName,String objectId,
			Boolean valid,List<MagicDimension> searchCriterias,String orderBy, Integer start, Integer count) {
		return magicDao.listRowIds(partition, spaceName, regionName, displayName, objectId, valid, searchCriterias, orderBy, start, count);
	}
	public List<String>  listRowItemIds(String partition, String rowId,String viewName, String orderBy) {
		return magicDao.listRowItemIds(partition, rowId, viewName, orderBy);
	}
	public MagicSuperRowItem getRowItemById(String rowId) {
		return (MagicSuperRowItem) magicDao.getObject(MagicRowItem.class, rowId);
	}
	public Boolean savePage(MagicPage magicPage) {
		ServiceUtil.notNull(magicPage, "magicPage is null");
		ServiceUtil.notNull(magicPage.getName(), "magicPage.name is null");
		ServiceUtil.notNull(magicPage.getDataViewId(), "magicPage.dataViewId is null");
		ServiceUtil.notNull(magicPage.getSpaceId(), "magicPage.spaceId is null");
		ServiceUtil.notNull(magicPage.getSpaceName(), "magicPage.spaceName is null");
		ServiceUtil.notNull(magicPage.getRegionId(), "magicPage.regionId is null");
		ServiceUtil.notNull(magicPage.getRegionName(), "magicPage.regionName is null");
		return supervisionDao.savePage(magicPage);
	}
	public Boolean savePageAssembler(MagicPageAssembler pageAssembler) {
		ServiceUtil.notNull(pageAssembler, "pageAssembler is null");
		ServiceUtil.notNull(pageAssembler.getName(), "pageAssembler.name is null");
		ServiceUtil.notNull(pageAssembler.getMenuId(), "pageAssembler.menuId is null");
		ServiceUtil.notNull(pageAssembler.getPageId(), "pageAssembler.pageId is null");
		ServiceUtil.notNull(pageAssembler.getRegionType(), "pageAssembler.regionType is null");
		ServiceUtil.notNull(pageAssembler.getSeq(), "pageAssembler.seq is null");
		return supervisionDao.savePageAssembler(pageAssembler);
	}
	public Boolean saveParticipant(MagicParticipant participant) {
		ServiceUtil.notNull(participant, "participant is null");
		ServiceUtil.notNull(participant.getName(), "participant.name is null");
		ServiceUtil.notNull(participant.getParticipantType(), "participant.participantType is null");
		ServiceUtil.notNull(participant.getSeq(), "participant.seq is null");
		ServiceUtil.notNull(participant.getToken(), "participant.token is null");
		return supervisionDao.saveParticipant(participant);
	}
	public Boolean saveMenuConnector(MagicMenuConnector menuConnector) {
		ServiceUtil.notNull(menuConnector, "menuConnector is null");
		ServiceUtil.notNull(menuConnector.getAccessorId(), "menuConnector.accessorId is null");
		ServiceUtil.notNull(menuConnector.getProviderId(), "menuConnector.providerId is null");
		return supervisionDao.saveMenuConnector(menuConnector);
	}
	public Boolean saveMenu(MagicMenu magicMenu) {
		ServiceUtil.notNull(magicMenu, "magicMenu is null");
		ServiceUtil.notNull(magicMenu.getMenuConnectorId(), "magicMenu.connectorId is null");
		ServiceUtil.notNull(magicMenu.getName(), "magicMenu.name is null");
		return supervisionDao.saveMenu(magicMenu);
	}
	public Boolean savePaticipantGroup(MagicPaticipantGroup participantGroup) {
		ServiceUtil.notNull(participantGroup, "participantGroup is null");
		ServiceUtil.notNull(participantGroup.getName(), "participantGroup.name is null");
		return supervisionDao.savePaticipantGroup(participantGroup);
	}
}
