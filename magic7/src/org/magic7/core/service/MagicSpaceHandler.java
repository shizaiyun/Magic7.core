package org.magic7.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.dao.DaoAssistant;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.domain.MagicTriggerAssembler;
import org.magic7.core.domain.MagicObject;
import org.magic7.core.domain.MagicObjectRegion;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.dynamic.loader.MagicLoaderUtils;
import org.magic7.utils.Dates;
import org.magic7.utils.ServiceUtil;
public class MagicSpaceHandler {
	public static MagicService service = MagicServiceFactory.getMagicService();
	private MagicSpaceHandler() {}
	public static List<MagicDimension> listDimension(String spaceName,String regionName,String viewName,String dimensionNames,Integer destination) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(spaceName, regionName);
		List<MagicDimension>  dimensions = service.listDimension(null, null, null, spaceRegion.getName(), viewName, destination, " seq ");
		if(StringUtils.isEmpty(dimensionNames))
			return dimensions;
		List<MagicDimension> results = new ArrayList<>();
		String[] names = dimensionNames.split(",");
		for(String name:names) {
			for(MagicDimension dimension:dimensions) {
				if(name.equals(dimension.getDisplayName()))
					results.add(dimension);
			}
		}
		return results;
	}
	public static Boolean deleteRow(String rowId) {
		service.deleteRowById(rowId);
		service.deleteRowItemByRowId(rowId);
		return true;
	} 
	public static Boolean deleteMagicObjectById(String objectId) {
		service.deleteMagicObjectById(objectId);
		service.deleteMagicRegionRowByObjId(objectId);
		service.deleteRowItemByObjectId(objectId);
		return true;
	}
	public static List<MagicDimension> createQueryCondition(String spaceName,String regionName,String conditionPairs,
			HttpServletRequest request) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(conditionPairs, "conditionPairs is null");
		String[] conditions = conditionPairs.split(";");
		List<MagicDimension> queryConditions = new ArrayList<>();
		for(String condition:conditions) {
			String pair[] = condition.split(":");
			if(pair.length!=2)
				continue;
			MagicDimension query = service.getDimension(spaceName, regionName, pair[0], null);
			query.setQueryCondition(pair[1]);
			queryConditions.add(query);
		}
		return queryConditions;
	}
	public static List<MagicRegionRow> listRow(String spaceName,String regionName,String viewName,String dimensionName,String objectId,
			Boolean valid,List<MagicDimension> searchCriterias,String orderBy, Integer start, Integer count) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		String partition = getPartition(spaceName, regionName);
		List<MagicRegionRow> rows = service.listRow(partition, spaceName,regionName, dimensionName, objectId, valid, searchCriterias, orderBy, start, count);
		List<MagicSuperRowItem> items = null;
		for(MagicRegionRow row:rows) {
			items = service.listRowItem(partition, row.getId(), viewName, " seq ");
			populateRowItemList(items,viewName);
			row.setRowItems(items);
		}
		return rows;
	}
	public static void populateRowItemList(List<MagicSuperRowItem> items,String viewName) {
		Object obj = null;
		Object value = null;
		if(items==null||items.size()==0)
			return ;
		for(MagicSuperRowItem item:items) {
			if(StringUtils.isNotEmpty(viewName))
				item.setViewItem(service.getSpaceRegionViewItem(item.getSpaceId(), item.getSpaceRegionId(), viewName, item.getDimensionId()));
			value = null;
			if(item.getLnk()!=null&&item.getLnk()) {
				if(!MagicDimension.ValueType.LIST_STR_VALUE.getCode().equals(item.getValueType())) {
					obj = ServiceUtil.getObject(item.getRelationEntityName(), item.getEntityId());
					if(obj==null)
						continue;
					value = BeanUtils.getBeanProperty(obj, item.getName());
					if(value==null)
						continue;
					if(item.getDualLnk()!=null&&item.getDualLnk()) {
						obj = ServiceUtil.getObject(item.getDualEntityName(), value.toString());
						if(obj==null)
							continue;
						value = BeanUtils.getBeanProperty(obj, item.getDualPropertyName());
						if(value==null)
							continue;
					}
					item.setValueName(value.toString());
					setRowItemValue(item,value);
				} else {
					String idList = item.getEntityId();
					if(StringUtils.isEmpty(idList))
						continue;
					String ids[] = idList.replaceAll("'", "").split(",");
					if(ids.length==0)
						throw new RuntimeException("ids is null");
					String values = "";
					for(String id:ids) {
						obj = ServiceUtil.getObject(item.getRelationEntityName(), id);
						if(obj==null)
							continue;
						value = BeanUtils.getBeanProperty(obj, item.getName());
						if(value==null)
							continue;
						values+=value.toString()+",";
					}
					setRowItemValue(item,values.replaceAll(",$", ""));
				}
			}
		}
		MagicComparator comparator = new MagicComparator();
		Collections.sort(items, comparator);
	}
	public static List<MagicObjectRegion> listObjectRegion(String spaceName,String objectId) {
		return service.listObjectRegion(spaceName, objectId);
	}
	public static void setRowItemValue(MagicSuperRowItem item,Object value) {
		ServiceUtil.notNull(item, "item is null");
		//TODO 提交保存的时候，view信息没有从页面带过来，会出错，暂时屏蔽，后面来改
		/*if(item.getViewItem()==null&&item.getRequired())
			ServiceUtil.notNull(value,item.getDisplayName()+"'s value is null");
		if(item.getViewItem()!=null&&item.getViewItem().getRequired()!=null)
			ServiceUtil.notNull(value,item.getDisplayName()+"'s value is null");*/
		if(MagicDimension.ValueType.STR_VALUE.getCode().equals(item.getValueType()))
			if(value==null) {
				item.setStrValue(null);
			}else {
				item.setStrValue(value.toString());
			}
		else if(MagicDimension.ValueType.DATE_VALUE.getCode().equals(item.getValueType()))
			if(value==null) {
				item.setDateValue(null);
			}else {
				item.setDateValue((Date) value);
			}
		else if(MagicDimension.ValueType.NUM_VALUE.getCode().equals(item.getValueType()))
			if(value==null) {
				item.setNumValue(null);
			}else {
				item.setNumValue((BigDecimal) value);
			}
		else if(MagicDimension.ValueType.BOOLEAN_VALUE.getCode().equals(item.getValueType()))
			if(value==null) {
				item.setBooleanValue(null);
			}else {
				item.setBooleanValue((Boolean) value);
			}
		else if(MagicDimension.ValueType.LIST_STR_VALUE.getCode().equals(item.getValueType()))
			if(value==null) {
				item.setListStrValue(null);
			}else {
				item.setListStrValue(","+(String) value+",");
			}
	}
	public static Object getRowItemValue(MagicSuperRowItem item) {
		ServiceUtil.notNull(item, "item is null");
		if(MagicDimension.ValueType.STR_VALUE.getCode().equals(item.getValueType()))
			return item.getStrValue();
		else if(MagicDimension.ValueType.DATE_VALUE.getCode().equals(item.getValueType()))
			return item.getDateValue();
		else if(MagicDimension.ValueType.NUM_VALUE.getCode().equals(item.getValueType()))
			return item.getNumValue();
		else if(MagicDimension.ValueType.BOOLEAN_VALUE.getCode().equals(item.getValueType()))
			return item.getBooleanValue();
		else if(MagicDimension.ValueType.LIST_STR_VALUE.getCode().equals(item.getValueType()))
			return item.getListStrValue();
		return null;
	}
	public static Boolean isMultiply(String spaceName,String regionName) {
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(spaceName, regionName);
		return spaceRegion.getMultiply();
	}
	public static MagicRegionRow getRowById(String id) {
		ServiceUtil.notNull(id, "id is null");
		MagicRegionRow row = service.getRowById(id);
		List<MagicSuperRowItem> items = service.listRowItem(getPartition(row.getSpaceName(),  row.getRegionName()), id, null, null);
		populateRowItemList(items,null);
		row.setRowItems(items);
		return row;
	}
	public static Boolean saveRow(MagicRegionRow row) {
		ServiceUtil.notNull(row, "row is null");
		MagicObjectRegion objectRegion = service.getObjectRegionById(row.getRegionId());
		if(!objectRegion.getMultiply()) {
			List<MagicRegionRow> rows = listRow( row.getSpaceName(), row.getRegionName(), null,null, row.getObjectId(), true, null, null, 0, 2);
			if(rows.size()>0&&!rows.get(0).getId().equals(row.getId()))
				throw new RuntimeException(objectRegion.getName()+" is single data region, it already has one data");
		}
		objectRegion.setHasData(true);
		List<MagicSuperRowItem> values = row.getRowItems();
		try {
			service.saveRow(row);
			Object obj = null;
			Object value = null;
			for(MagicSuperRowItem item:values) {
				if(MagicDimension.PersistenceType.TEMP.getCode().equals(item.getValueType()))
					continue;
				/*if(row.getValid()) {
					if(item.getRequired()&&getRowItemValue(item)==null)
						throw new RuntimeException(item.getDisplayName()+"'s value is null");
				}*/
				item.setRowId(row.getId());
				check:if(item.getLnk()!=null&&item.getLnk()) {
					value = getRowItemValue(item);
					if(value==null)
						break check;
					obj = ServiceUtil.getObject(item.getRelationEntityName(), item.getEntityId());
					if(obj==null)
						break check;
					//org.apache.commons.beanutils.BeanUtils.setProperty(obj, item.getName(), value);//暂时屏蔽
					//ServiceUtil.saveObject(obj);
				}
				service.saveRowItem(item);
			}
			service.saveObjectRegion(objectRegion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static MagicRegionRow createRow(String spaceName,String regionName,String objectId, Boolean valid) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(objectId, "objectId is null");
		MagicSpace space = service.getSpaceByName(spaceName);
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(spaceName, regionName);
		ServiceUtil.notNull(spaceRegion, "spaceRegion is null");
		MagicObjectRegion objectRegion = service.getObjectRegion(objectId, spaceRegion.getName());
		ServiceUtil.notNull(objectRegion, "region is null");
		String rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(space.getPartition());
		if(StringUtils.isNotEmpty(spaceRegion.getPartition()))
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(spaceRegion.getPartition());
		MagicRegionRow row = new MagicRegionRow();
		row.setObjectId(objectId);
		row.setRegionId(objectRegion.getId());
		row.setCreateDate(new Date());
		row.setSpaceId(objectRegion.getSpaceId());
		row.setSpaceRegionId(objectRegion.getSpaceRegionId());
		row.setSpaceName(objectRegion.getSpaceName());
		row.setRegionName(objectRegion.getName());
		row.setValid(valid);
		List<MagicDimension> dimensions = service.listDimension(null, null, objectRegion.getSpaceRegionId(), null, null, MagicDimension.Destination.FOR_DATA.getCode(), "seq asc ");
		MagicSuperRowItem item = null;
		List<MagicSuperRowItem> values = new ArrayList<>();
		for(MagicDimension dimension:dimensions) {
			try {
				item = (MagicSuperRowItem) Class.forName(rowItemClassPath).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			org.springframework.beans.BeanUtils.copyProperties(dimension, item);
			item.setId(null);
			item.setObjectId(objectId);
			item.setRegionId(objectRegion.getId());
			item.setSpaceId(objectRegion.getSpaceId());
			item.setSpaceRegionId(objectRegion.getSpaceRegionId());
			item.setRowId(row.getId());
			item.setCreateDate(row.getCreateDate());
			item.setDimensionId(dimension.getId());
			item.setDimensionName(dimension.getName());
			item.setSeq(dimension.getSeq());
			item.setPageType(dimension.getPageType());
			item.setDisplayName(dimension.getDisplayName());
			item.setSpaceName(objectRegion.getSpaceName());
			item.setRegionName(objectRegion.getName());
			item.setDualLnk(dimension.getDualLnk());
			item.setDualEntityName(dimension.getDualEntityName());
			item.setTailLabel(dimension.getTailLabel());
			item.setVisible(dimension.getVisible());
			item.setEditable(dimension.getEditable());
			item.setSpaceRegionName(objectRegion.getName());
			if(MagicDimension.DefaultValue.CURRENT_DATE.getName().equals(item.getDefaultValue())) {
				item.setDateValue(new Date());
				item.setValueName(Dates.format(item.getDateValue(), Dates.DATETIME_FORMAT));
			}  else if(item.getDefaultValue()!=null)
				setRowItemValue(item, item.getDefaultValue());
			values.add(item);
		}
		row.setRowItems(values);
		return row;
	}
	public static void createRowItem(String spaceName,String regionName,MagicDimension dimension,String objectId,String rowId) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		ServiceUtil.notNull(objectId, "objectId is null");
		MagicSpace space = service.getSpaceByName(spaceName);
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(spaceName, regionName);
		ServiceUtil.notNull(spaceRegion, "spaceRegion is null");
		MagicObjectRegion objectRegion = service.getObjectRegion(objectId, spaceRegion.getName());
		ServiceUtil.notNull(objectRegion, "region is null");
		MagicSuperRowItem item = null;
		String rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(space.getPartition());
		if(StringUtils.isNotEmpty(spaceRegion.getPartition()))
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(spaceRegion.getPartition());
		try {
			item = (MagicSuperRowItem) Class.forName(rowItemClassPath).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		org.springframework.beans.BeanUtils.copyProperties(dimension, item);
		item.setId(null);
		item.setObjectId(objectId);
		item.setRegionId(objectRegion.getId());
		item.setSpaceId(objectRegion.getSpaceId());
		item.setSpaceRegionId(objectRegion.getSpaceRegionId());
		item.setRowId(rowId);
		item.setCreateDate(new Date());
		item.setDimensionId(dimension.getId());
		item.setDimensionName(dimension.getName());
		item.setSeq(dimension.getSeq());
		item.setPageType(dimension.getPageType());
		item.setDisplayName(dimension.getDisplayName());
		item.setSpaceName(objectRegion.getSpaceName());
		item.setRegionName(objectRegion.getName());
		item.setDualLnk(dimension.getDualLnk());
		item.setDualEntityName(dimension.getDualEntityName());
		item.setTailLabel(dimension.getTailLabel());
		item.setVisible(dimension.getVisible());
		item.setEditable(dimension.getEditable());
		item.setSpaceRegionName(objectRegion.getName());
		if(MagicDimension.DefaultValue.CURRENT_DATE.getName().equals(item.getDefaultValue())) {
			item.setDateValue(new Date());
			item.setValueName(Dates.format(item.getDateValue(), Dates.DATETIME_FORMAT));
		}  else if(item.getDefaultValue()!=null)
			setRowItemValue(item, item.getDefaultValue());
	}
	public static MagicObject createMagicObject(MagicObject object) {
		ServiceUtil.notNull(object.getSpaceId(), "object.spaceId is null");
		DaoAssistant.closeSessionByService();
		DaoAssistant.currentSession(false);
		try {
			DaoAssistant.beginTransaction();
			service.saveMagicObject(object);
			List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(null, object.getSpaceId(), null, 0, 1000);
			MagicObjectRegion region = null;
			for(MagicSpaceRegion spaceRegion:spaceRegions) {
				region = new MagicObjectRegion();
				org.springframework.beans.BeanUtils.copyProperties(spaceRegion, region);
				region.setObjectId(object.getId());
				region.setId(null);
				region.setSpaceRegionId(spaceRegion.getId());
				region.setSpaceName(spaceRegion.getSpaceName());
				region.setSeq(spaceRegion.getSeq());
				region.setRegionType(spaceRegion.getRegionType());
				region.setSourceName(spaceRegion.getSourceName());
				region.setDimensionNum(spaceRegion.getDimensionNum());
				region.setExtraEditor(spaceRegion.getExtraEditor());
				service.saveObjectRegion(region);
				if(!region.getMultiply())
					saveRow(createRow(object.getSpaceName(),spaceRegion.getName(),object.getId(),false));
			}
			DaoAssistant.commitTransaction();
		} catch (Exception e) {
			DaoAssistant.rollBackTransaction();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DaoAssistant.closeSessionByService();
		}
		return object;
	}
	public static MagicObject createMagicObjectBySpace(String spaceName) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		MagicObject object = new MagicObject();
		MagicSpace space = service.getSpaceByName(spaceName);
		ServiceUtil.notNull(space, "space is null");
		object.setSpaceId(space.getId());
		object.setSpaceName(space.getName());
		return createMagicObject(object);
	}
	public static void syncRowItem(MagicRegionRow source,MagicRegionRow target) {
		ServiceUtil.notNull(source, "source is null");
		ServiceUtil.notNull(source.getRowItems(), "source.rowItems is null");
		ServiceUtil.notNull(target, "target is null");
		ServiceUtil.notNull(target.getRowItems(), "target.rowItems is null");
		List<MagicSuperRowItem> sourceItems = source.getRowItems();
		List<MagicSuperRowItem> targetItems = target.getRowItems();
		for(MagicSuperRowItem sourceItem:sourceItems) {
			for(MagicSuperRowItem targetItem:targetItems) {
				if(targetItem.getDisplayName().equals(sourceItem.getDisplayName())) {
					setRowItemValue(targetItem,getRowItemValue(sourceItem));
				}
			}
		}
	}
	public static void cloneRowItem(MagicRegionRow source,MagicRegionRow target) {
		ServiceUtil.notNull(source, "source is null");
		ServiceUtil.notNull(source.getRowItems(), "source.rowItems is null");
		ServiceUtil.notNull(target, "target is null");
		ServiceUtil.notNull(target.getRowItems(), "target.rowItems is null");
		List<MagicSuperRowItem> sourceItems = source.getRowItems();
		List<MagicSuperRowItem> targetItems = target.getRowItems();
		targetItems.clear();
		MagicSpace space = service.getSpaceByName(source.getSpaceName());
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(source.getSpaceName(), source.getRegionName());
		String rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(space.getPartition());
		if(StringUtils.isNotEmpty(spaceRegion.getPartition()))
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(spaceRegion.getPartition());
		for(MagicSuperRowItem sourceItem:sourceItems) {
			MagicSuperRowItem item = null;
			try {
				item = (MagicSuperRowItem) Class.forName(rowItemClassPath).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			org.springframework.beans.BeanUtils.copyProperties(sourceItem, item);
			item.setId(null);
			targetItems.add(item);
		}
	}
	public static MagicRegionRow cloneRow(MagicRegionRow source) {
		ServiceUtil.notNull(source, "source is null");
		ServiceUtil.notNull(source.getRowItems(), "source.rowItems is null");
		MagicRegionRow target = new MagicRegionRow();
		org.springframework.beans.BeanUtils.copyProperties(source, target);
		target.setRowItems(new ArrayList<MagicSuperRowItem>());
		target.setId(null);
		cloneRowItem(source,target);
		return target;
	}
	private static class MagicComparator implements Comparator<MagicSuperRowItem> {
		public int compare(MagicSuperRowItem item1, MagicSuperRowItem item2) {
			if(item1.getViewItem()!=null&&item2.getViewItem()==null)
				return -1;
			if(item1.getViewItem()==null&&item2.getViewItem()!=null)
				return 1;
			if(item1.getViewItem()!=null&&item2.getViewItem()!=null)
				return item1.getViewItem().getSeq().compareTo(item2.getViewItem().getSeq());
			if(item1.getSeq()==null&&item2.getSeq()==null)
				return 0;
			if(item1.getSeq()==null&&item2.getSeq()!=null)
				return -1;
			if(item1.getSeq()!=null&&item2.getSeq()==null)
				return 1;
			return item1.getSeq().compareTo(item1.getSeq());
		}
    }
	public static MagicObject cloneMagicObject(String objectId){
		ServiceUtil.notNull(objectId, "objectId is null");
		MagicObject oldObject=service.getMagicObjectById(objectId);
		oldObject.setId(null);
		service.saveMagicObject(oldObject);
		MagicObject newObject=oldObject;
		String spaceName=oldObject.getSpaceName();
		List<MagicObjectRegion> objectRegions=service.listObjectRegion(spaceName, objectId);
		for(MagicObjectRegion objectRegion:objectRegions){
			objectRegion.setId(null);
			objectRegion.setObjectId(newObject.getId());
			service.saveObjectRegion(objectRegion);
			String partition = getPartition(spaceName, objectRegion.getName());
			List<MagicRegionRow> rows = service.listRow(partition, spaceName, objectRegion.getName(), objectId, null, null, null, 0, 1000);
			List<MagicSuperRowItem> items = null;
			for(MagicRegionRow row:rows) {
				items = service.listRowItem(partition, row.getId(), null, " seq ");
				row.setId(null);
				row.setObjectId(newObject.getId());
				row.setRegionId(objectRegion.getId());
				row.setValid(false);
				row.setRowItems(items);
				service.saveRow(row);
				for(MagicSuperRowItem item:items){
					item.setId(null);
					item.setObjectId(newObject.getId());
					item.setRegionId(objectRegion.getId());
					item.setRowId(row.getId());
					service.saveRowItem(item);
				}
				row.setRowItems(items);
				service.saveRow(row);
			}
		}
		return newObject;
	}
	public static Boolean executeTrigger(MagicRegionRow row ,String trigger,Map<String, Object> params) throws Exception {
		ServiceUtil.notNull(row, "items is null");
		List<MagicTriggerAssembler> assemblers = service.listTriggerAssembler(trigger, row.getSpaceName(), row.getRegionName(), " seq ");
		String codeName = null;
		String displayName = null;
		try {
			for(MagicTriggerAssembler assembler:assemblers) {
				String parameterNames = assembler.getParameterNames();
				displayName = assembler.getDisplayName();
				Object[] inParams = null;
				if(StringUtils.isNotEmpty(parameterNames)) {
					String names[] = parameterNames.split(",");
					inParams = new Object[names.length];
					for(int i=0;i<names.length;i++) {
						if(i==0&&"defaultRowItem".equals(names[i]))
							inParams[i] = getRowItemFromRow(row, assembler.getDisplayName());
						else if(i==0&&"defaultRow".equals(names[i]))
							inParams[i] = row;
						else
							inParams[i] = params.get(names[i]);
					}
				}
				MagicLoaderUtils.invokeRegionCode(row.getSpaceName(), row.getRegionName(),assembler.getCodeName() , inParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("invoke "+row.getSpaceName()+"-"+row.getRegionName()+"-"+codeName+" on "+displayName+" failed");
		}
		return true;
	}
	public static List<MagicSuperRowItem> listRowItem(String spaceName,
			String regionName, String viewName,String displayName,String orderBy,Integer start, Integer count) {
		ServiceUtil.notNull(spaceName, "spaceName is null");
		ServiceUtil.notNull(regionName, "regionName is null");
		return service.listRowItem(getPartition(spaceName, regionName), spaceName, regionName, viewName, displayName, orderBy, start, count);
	}
	public static Boolean isFreshValue(MagicSuperRowItem item) {
		return isFreshValue(item.getSpaceName(), item.getRegionName(), item.getDisplayName(), item.getStrValue());
	}
	public static Boolean isFreshValue(String spaceName,String regionName,String displayName,String value) {
		if(value==null)
			return true;
		return service.isFreshValue(getPartition(spaceName, regionName), spaceName, regionName, displayName, value);
	}
	public static MagicSuperRowItem getRowItemFromRow(MagicRegionRow row,String displayName) {
		if(row.getRowItems()==null||StringUtils.isEmpty(displayName))
			return null;
		for(MagicSuperRowItem item:row.getRowItems()) 
			if(displayName.equals(item.getDisplayName()))
				return item;
		return null;
	}
	public static Integer listRowCount(String spaceName,String regionName,
			String displayName, String objectId, Boolean valid, List<MagicDimension> queryConditions) {
		return service.listRowCount(getPartition(spaceName, regionName), spaceName, regionName, displayName, objectId, valid, queryConditions);
	}
	public static String getPartition(String spaceName,String regionName) {
		MagicSpace space = service.getSpaceByName(spaceName);
		MagicSpaceRegion spaceRegion = service.getSpaceRegion(spaceName, regionName);
		String partition = spaceRegion.getPartition();
		if(StringUtils.isEmpty(partition))
			partition = space.getPartition();
		return partition;
	}
	public static List<MagicDimension> getQueryDimension(String spaceName,String regionName,Map<String,String> conditionPairs) {
        ServiceUtil.notNull(spaceName, "spaceName is null");
        ServiceUtil.notNull(regionName, "regionName is null");
        ServiceUtil.notNull(conditionPairs, "conditionPairs is null");
        List<MagicDimension> queryConditions = new ArrayList<>();
        Set<Entry<String,String>> entrySet = conditionPairs.entrySet();
        for (Entry<String, String> entry : entrySet) {
               if(StringUtils.isNotBlank(entry.getValue())) {
                      MagicDimension query = service.getDimension(spaceName, regionName, entry.getKey(), MagicDimension.Destination.FOR_DATA.getCode());
                      query.setQueryCondition(entry.getValue());
                      queryConditions.add(query);
               }
        }
        return queryConditions;
	}
	public static Boolean saveCodeLib(MagicCodeLib lib) {
		if(MagicCodeLib.CodeType.JAVA.getCode().equals(lib.getCodeType())) {
			MagicCodeLib codeLib = service.getJavaCodeLibBySignature(lib.getSignature());
			if(codeLib!=null)
				throw new RuntimeException("JAVA Code "+lib.getName()+"-"+lib.getParameterNames()+":already in system");
		}
		return service.saveCodeLib(lib);
	}
	public static List<MagicDimension> createSearchCriterias(String spaceName,String regionName,String queryView,Map<String,Object> conditionPairs) {
        ServiceUtil.notNull(spaceName, "spaceName is null");
        ServiceUtil.notNull(regionName, "regionName is null");
        ServiceUtil.notNull(conditionPairs, "conditionPairs is null");
        MagicDimension.Destination destination = MagicDimension.Destination.FOR_QUERY; 
        if(StringUtils.isBlank(queryView)) {
        	destination=MagicDimension.Destination.FOR_DATA;
        }
        List<MagicDimension> queryConditions = new ArrayList<>();
        Set<Entry<String,Object>> entrySet = conditionPairs.entrySet();
        for (Entry<String, Object> entry : entrySet) {
               if(entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                      MagicDimension query = service.getDimension(spaceName, regionName, entry.getKey(), destination.getCode());
                      if(query!=null) {
                    	  query.setQueryCondition(entry.getValue());
                    	  queryConditions.add(query);
                      }
               }
        }
        return queryConditions;
	}
	public static MagicObject createSupplementMagicObject(String objectId) {
		ServiceUtil.notNull(objectId, "objectId is null");
		MagicObject object = service.getMagicObjectById(objectId);
		ServiceUtil.notNull(object, "object is null");
		ServiceUtil.notNull(object.getSpaceId(), "object.spaceId is null");
		DaoAssistant.closeSessionByService();
		DaoAssistant.currentSession(false);
		try {
			DaoAssistant.beginTransaction();
			List<MagicSpaceRegion> spaceRegions = service.listSupplementSpaceRegion(object.getId(), object.getSpaceId(), null, 0, 1000);
			MagicObjectRegion region = null;
			for(MagicSpaceRegion spaceRegion:spaceRegions) {
				region = new MagicObjectRegion();
				org.springframework.beans.BeanUtils.copyProperties(spaceRegion, region);
				region.setObjectId(object.getId());
				region.setId(null);
				region.setSpaceRegionId(spaceRegion.getId());
				region.setSpaceName(spaceRegion.getSpaceName());
				region.setSeq(spaceRegion.getSeq());
				region.setRegionType(spaceRegion.getRegionType());
				region.setSourceName(spaceRegion.getSourceName());
				region.setDimensionNum(spaceRegion.getDimensionNum());
				region.setExtraEditor(spaceRegion.getExtraEditor());
				service.saveObjectRegion(region);
				if(!region.getMultiply())
					saveRow(createRow(object.getSpaceName(),spaceRegion.getName(),object.getId(),false));
			}
			DaoAssistant.commitTransaction();
		} catch (Exception e) {
			DaoAssistant.rollBackTransaction();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DaoAssistant.closeSessionByService();
		}
		return object;
	}
}
 
