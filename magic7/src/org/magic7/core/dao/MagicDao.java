package org.magic7.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.domain.MagicObject;
import org.magic7.core.domain.MagicObjectRegion;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.domain.MagicRegionCodeLnk;
import org.magic7.core.service.ServiceStaticInfo;
import org.magic7.dynamic.loader.MagicLoaderUtils;

public class MagicDao extends BaseDao {
	private MagicDao(){}
	private static MagicDao instance = new MagicDao();
	private static String objectQuery = " and OBJECT_ID=:objectId ";
	private static String dimensionQuery = " and DISPLAY_NAME=:dimensionDisplayName " +
			" and (STR_VALUE is not null or DATE_VALUE is not null or LIST_STR_VALUE is not null or NUM_VALUE is not null " +
			" or BOOLEAN_VALUE is not null )";
	public static MagicDao getInstance() {
		return instance;
	}
	public Boolean saveMagicObject(MagicObject magicObject){
		return super.save(magicObject);
	}
	public Boolean deleteRowItemByRowId(String rowId){
		String hql = "delete from MagicRowItem r where r.rowId=?";
		return super.deleteById(hql,rowId);
	}
	public Boolean deleteRowById(String id){
		String hql = "delete from MagicRegionRow r where r.id=?";
		return super.deleteById(hql,id);
	}
	public Boolean saveRow(MagicRegionRow row) {
		return super.save(row);
	}
	public Boolean saveRowItem(MagicSuperRowItem item) {
		return super.save(item);
	}
	private void buildMagicDimension(StringBuilder hql,String spaceId,String spaceName,String spaceRegionId, String spaceRegionName, String viewName, Integer destination, Map<String,Object> params) {
		if(destination!=null) {
			hql.append(" and DESTINATION=:destination");
			params.put("destination", destination);
		} else 
			hql.append(" and (destination is null or destination=0 ) ");
		if(StringUtils.isNotEmpty(spaceId)) {
			hql.append(" and a.SPACE_ID=:spaceId");
			params.put("spaceId", spaceId);
		}
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and a.SPACE_NAME=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and a.id=c.DIMENSION_ID and b.NAME=:viewName ");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(spaceRegionId)) {
			hql.append(" and a.SPACE_REGION_ID=:spaceRegionId");
			params.put("spaceRegionId", spaceRegionId);
		}
		if(StringUtils.isNotEmpty(spaceRegionName)) {
			hql.append(" and a.SPACE_REGION_NAME=:spaceRegionName");
			params.put("spaceRegionName", spaceRegionName);
		}
	}
	@SuppressWarnings("unchecked")
	public List<MagicDimension> listDimension(String spaceId,String spaceName, String spaceRegionId, String spaceRegionName, 
			String viewName, Integer destination, String orderBy) {
		StringBuilder hql = new StringBuilder("select distinct a.* from "+ServiceStaticInfo.TABLE_PREFIX+"_DIMENSION a ");
		if(StringUtils.isNotEmpty(viewName))
			hql.append(" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW b on b.SPACE_ID=a.SPACE_ID and b.SPACE_REGION_ID=a.SPACE_REGION_ID " +
					" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW_ITEM c on b.ID=c.VIEW_ID ");
		hql.append(" where 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		buildMagicDimension(hql, spaceId, spaceName, spaceRegionId, spaceRegionName, viewName, destination, params);
		if(StringUtils.isNotEmpty(orderBy)&&StringUtils.isNotEmpty(viewName)&&"seq".equals(orderBy.toLowerCase().trim()))
			hql.append(" order by c."+orderBy);
		else if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by "+orderBy);
		return super.listWithSql(hql.toString(), params,"",MagicDimension.class.getCanonicalName(), 0, 1000);
	}
	private void bulidQueryConditionsSQL(StringBuilder hql,String partition,String objectId,
			List<MagicDimension> dimensions,List<MagicDimension> searchCriterias, Map<String,Object> params) {
		for(MagicDimension dimension:dimensions) {
			if(dimension.getVirtual()) {
				String condition = null;
				if(searchCriterias!=null) {
					for(MagicDimension criteria:searchCriterias) {
						if((!criteria.getDisplayName().equals(dimension.getDisplayName())
								&&!criteria.getName().equals(dimension.getName()))
								||criteria.getQueryCondition()==null)
							continue;
						condition = " and NAME=:"+dimension.getName();
						params.put(dimension.getName(), dimension.getName());
						if(MagicDimension.QueryType.PRECISE.getCode().equals(criteria.getQueryType())) {
							if(criteria.getValueType().equals(MagicDimension.ValueType.STR_VALUE.getCode())) {
								if(StringUtils.isNotEmpty(criteria.getDefaultQuery())) {
									condition = " and ((STR_VALUE=:"+"query_"+criteria.getDisplayName()+" and NAME=:"+dimension.getName()+") or ("+criteria.getDefaultQuery()+"))";
									if(criteria.getDefaultQueryCondition()!=null) {
										params.put("default_query_"+criteria.getDisplayName(), criteria.getLikeModifier()+criteria.getDelimiter()+
												criteria.getDefaultQueryCondition()+criteria.getDelimiter()+criteria.getLikeModifier());
									}
								} else
									condition += " and STR_VALUE=:"+"query_"+criteria.getDisplayName();
								params.put("query_"+criteria.getDisplayName(), criteria.getQueryCondition());
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.NUM_VALUE.getCode())) {
								condition += " and NUM_VALUE=:"+"query_"+criteria.getDisplayName();
								params.put("query_"+criteria.getDisplayName(), criteria.getQueryCondition());
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.DATE_VALUE.getCode())) {
								condition += " and DATE_VALUE=:"+"query_"+criteria.getDisplayName();
								params.put("query_"+criteria.getDisplayName(), criteria.getQueryCondition());
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.BOOLEAN_VALUE.getCode())) {
								condition += " and BOOLEAN_VALUE=:"+"query_"+criteria.getDisplayName();
								params.put("query_"+criteria.getDisplayName(), criteria.getQueryCondition());
							}
						} else if(MagicDimension.QueryType.VAGUE.getCode().equals(criteria.getQueryType())) {
							if(criteria.getValueType().equals(MagicDimension.ValueType.STR_VALUE.getCode())) {
								condition += " and STR_VALUE like :"+"query_"+criteria.getDisplayName();
								params.put("query_"+criteria.getDisplayName(),"%"+criteria.getQueryCondition()+"%");
							}
						} else if(MagicDimension.QueryType.BIGGER.getCode().equals(criteria.getQueryType())) {
							if(criteria.getValueType().equals(MagicDimension.ValueType.NUM_VALUE.getCode())) {
								condition += " and NUM_VALUE<=:"+"query_bigger_"+criteria.getDisplayName();
								params.put("query_bigger_"+criteria.getDisplayName(), criteria.getQueryCondition());
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.DATE_VALUE.getCode())) {
								condition += " and DATE_VALUE<=:"+"query_bigger_"+criteria.getDisplayName();
								params.put("query_bigger_"+criteria.getDisplayName(), criteria.getQueryCondition());
							}
						} else if(MagicDimension.QueryType.SMALLER.getCode().equals(criteria.getQueryType())) {
							if(criteria.getValueType().equals(MagicDimension.ValueType.NUM_VALUE.getCode())) {
								condition += " and NUM_VALUE>=:"+"query_smaller"+criteria.getDisplayName();
								params.put("query_smaller"+criteria.getDisplayName(), criteria.getQueryCondition());
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.DATE_VALUE.getCode())) {
								condition += " and DATE_VALUE>=:"+"query_smaller"+criteria.getDisplayName();
								params.put("query_smaller"+criteria.getDisplayName(), criteria.getQueryCondition());
							}
						} else if(MagicDimension.QueryType.IN.getCode().equals(criteria.getQueryType())) {
							if(criteria.getValueType().equals(MagicDimension.ValueType.STR_VALUE.getCode())) {
								condition += " and STR_VALUE in ('"+criteria.getQueryCondition().
										toString().replaceAll("^[,]{1,}|[,]{1,}$", "").replaceAll(",", "','")+"' )";
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.NUM_VALUE.getCode())){
								condition += " and NUM_VALUE in ('"+criteria.getQueryCondition().
										toString().replaceAll("^[,]{1,}|[,]{1,}$", "").replaceAll(",", "','")+"' )";
							} else if(criteria.getValueType().equals(MagicDimension.ValueType.LIST_STR_VALUE.getCode())){
								String ids[] = criteria.getQueryCondition().toString().replaceAll("^[,]{1,}|[,]{1,}$", "").split(",");
								if(StringUtils.isNotEmpty(criteria.getDefaultQuery())) {
									condition = " and (( NAME=:"+dimension.getName()+" and (";
									for(String id:ids) {
										condition+= " LIST_STR_VALUE like '%,"+id+",%' or";
									}
									condition = condition.replaceAll("(or[\\s]*)$", " ))");
									condition+=" or ("+criteria.getDefaultQuery()+"))";
									if(criteria.getDefaultQueryCondition()==null) {
										params.put("default_query_"+criteria.getDisplayName(), criteria.getQueryCondition());
									} else {
										params.put("default_query_"+criteria.getDisplayName(), criteria.getDefaultQueryCondition());
									}
								} else {
									condition += " and (";
									for(String id:ids) {
										condition+= " LIST_STR_VALUE like '%,"+id+",%' or";
									}
									condition = condition.replaceAll("(or[\\s]*)$", " )");
								}
							}
						}
						hql.append(" inner join (select * from "+partition+"_ROW_ITEM where SPACE_REGION_NAME=:spaceRegionName ");
						if(StringUtils.isNotEmpty(objectId))
							hql.append(objectQuery);
						hql.append(condition+" ) "+criteria.getDisplayName()+" on a.ROW_ID="+criteria.getDisplayName()+".ROW_ID ");
					}
					
				}
			} else {
				String tableName = null;
				String lnkDimension = null;
				String queryDimension = null;
				String condition = "";
				if(searchCriterias!=null) {
					for(MagicDimension query:searchCriterias) {
						if(!query.getDisplayName().equals(dimension.getDisplayName())||query.getQueryCondition()==null)
							continue;
						tableName = DaoAssistant.getTableName(dimension.getRelationEntityName());
						lnkDimension = DaoAssistant.getPropertyName(dimension.getRelationEntityName(), "id");
						queryDimension = DaoAssistant.getPropertyName(query.getRelationEntityName(), query.getName());
						if(MagicDimension.QueryType.PRECISE.getCode().equals(query.getQueryType())) {
							if(StringUtils.isNotEmpty(query.getDefaultQuery())) {
								condition += " and (target."+queryDimension+"=:"+"query_"+query.getDisplayName()+" or "+query.getDefaultQuery()+")";
								params.put("default_query_"+query.getDisplayName(), query.getLikeModifier()+query.getDelimiter()+
										query.getQueryCondition()+query.getDelimiter()+query.getLikeModifier());
							} else
								condition = " and target."+queryDimension+"=:"+"query_"+query.getDisplayName();
							params.put("query_"+query.getDisplayName(), query.getQueryCondition());
						} else if(MagicDimension.QueryType.VAGUE.getCode().equals(query.getQueryType())) {
							condition = " and target."+queryDimension+" like :"+"query_"+query.getDisplayName();
							params.put("query_"+query.getDisplayName(), "%"+query.getQueryCondition()+"%");
						} else if(MagicDimension.QueryType.BIGGER.getCode().equals(query.getQueryType())) {
							condition = " and target."+queryDimension+"<=:"+"query_"+query.getDisplayName();
							params.put("query_"+query.getDisplayName(), query.getQueryCondition());
						} else if(MagicDimension.QueryType.SMALLER.getCode().equals(query.getQueryType())) {
							condition = " and target."+queryDimension+">=:"+"query_"+query.getDisplayName();
							params.put("query_"+query.getDisplayName(), query.getQueryCondition());
						} else if(MagicDimension.QueryType.IN.getCode().equals(query.getQueryType())) {
							if(query.getValueType().equals(MagicDimension.ValueType.NUM_VALUE.getCode()))
								condition = " and target."+queryDimension+" in ("+query.getQueryCondition().
									toString().replaceAll("^[,]{1,}|[,]{1,}$", "")+" )";
							else if(query.getValueType().equals(MagicDimension.ValueType.LIST_STR_VALUE.getCode()))
								condition = " and target."+queryDimension+" in ('"+query.getQueryCondition().
									toString().replaceAll("^[,]{1,}|[,]{1,}$", "").replaceAll(",", "','")+"' )";
						} 
						if(!MagicDimension.ValueType.LIST_STR_VALUE.getCode().equals(dimension.getValueType())) {
							hql.append(" inner join (select distinct item.* from "+partition+"_ROW_ITEM item, " +tableName+" as target "+
									" where item.SPACE_REGION_NAME=:spaceRegionName and item.ENTITY_ID=target."+lnkDimension+
									condition);
						} else {
							hql.append(" inner join (select distinct item.* from "+partition+"_ROW_ITEM item, " +tableName+" as target "+
									" where item.SPACE_REGION_NAME=:spaceRegionName and FIND_IN_SET(target."+lnkDimension+",item.ENTITY_ID)"+
									condition);
						}
						if(StringUtils.isNotEmpty(objectId))
							hql.append(" and item.OBJECT_ID=:objectId ");
						hql.append(" ) "+query.getDisplayName()+" on a.ROW_ID="+query.getDisplayName()+".ROW_ID ");
					}
				}
			}
		}
	}
	public void buildListRowItemValueMap(StringBuilder hql,String partition,String objectId,List<MagicDimension> dimensions,
			List<MagicDimension> searchCriterias,String spaceRegionName, Map<String,Object> params) {
		params.put("spaceRegionName", spaceRegionName);
		if(StringUtils.isNotEmpty(objectId))
			params.put("objectId", objectId);
		if(searchCriterias==null||searchCriterias.size()==0)
			return ;
		bulidQueryConditionsSQL(hql, partition, objectId, dimensions, searchCriterias, params);
	}
	public MagicRegionRow getRowById(String id) {
		return (MagicRegionRow) super.getObject(MagicRegionRow.class, id);
	}
	public MagicSpace getSpaceByName(String name) {
		String hql = "from MagicSpace where name='"+name+"'";
		return (MagicSpace) super.getObject(hql, null);
	}
	@SuppressWarnings("unchecked")
	public List<MagicSpaceRegion> listSpaceRegion(String spaceId,String orderBy,Integer start,Integer count) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegion where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceId)) {
			hql.append(" and spaceId=:spaceId");
			params.put("spaceId", spaceId);
		}
		if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by "+orderBy);
		return super.list(hql.toString(),params, start, count);
	}
	public Boolean saveSpaceRegion(MagicObjectRegion region) {
		return super.save(region);
	}
	public MagicObjectRegion getObjectRegionById(String id) {
		return (MagicObjectRegion) super.getObject(MagicObjectRegion.class, id);
	}
	public MagicObject getMagicObjectById(String id) {
		return (MagicObject) super.getObject(MagicObject.class, id);
	}
	@SuppressWarnings("unchecked")
	public List<MagicSuperRowItem>  listRowItem(String partition, String rowId,String viewName, String orderBy) {
		boolean nullPartition = false;
		if(StringUtils.isEmpty(partition)) {
			partition = ServiceStaticInfo.TABLE_PREFIX;
			nullPartition = true;
		}
		StringBuilder hql = new StringBuilder("select distinct a.* from "+partition+"_ROW_ITEM a ");
		if(StringUtils.isNotEmpty(viewName))
			hql.append(" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW b on a.SPACE_ID=b.SPACE_ID and a.SPACE_REGION_ID=b.SPACE_REGION_ID " +
					" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW_ITEM c on b.ID=c.VIEW_ID ");
		hql.append(" where 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(rowId)) {
			hql.append(" and a.ROW_ID=:rowId");
			params.put("rowId", rowId);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and b.name=:viewName and a.DIMENSION_ID=c.DIMENSION_ID");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by "+orderBy);
		String rowItemClassPath = null;
		if(!nullPartition)
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(partition);
		else
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(null);
		return super.listWithSql(hql.toString(), params, "", rowItemClassPath, 0, 1000);
	}
	@SuppressWarnings("unchecked")
	public List<MagicSuperRowItem>  listRowItem(String partition, String spaceName,String regionName, String viewName,String displayName,String orderBy,Integer start, Integer count) {
		boolean nullPartition = false;
		if(StringUtils.isEmpty(partition)) {
			partition = ServiceStaticInfo.TABLE_PREFIX;
			nullPartition = true;
		}
		StringBuilder hql = new StringBuilder("select distinct a.* from "+partition+"_ROW_ITEM a ");
		if(StringUtils.isNotEmpty(viewName))
			hql.append(" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW b on a.SPACE_ID=b.SPACE_ID and a.SPACE_REGION_ID=b.SPACE_REGION_ID " +
					" inner join "+ServiceStaticInfo.TABLE_PREFIX+"_SPACE_REGION_VIEW_ITEM c on b.ID=c.VIEW_ID ");
		hql.append(" where 1=1 ");
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and a.SPACE_NAME=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and b.name=:viewName and a.DIMENSION_ID=c.DIMENSION_ID");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and a.REGION_NAME=:regionName ");
			params.put("regionName", regionName);
		}
		if(StringUtils.isNotEmpty(displayName)) {
			hql.append(" and a.DISPLAY_NAME=:displayName");
			params.put("displayName", displayName);
		}
		if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by "+orderBy);
		String rowItemClassPath = null;
		if(!nullPartition)
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(partition);
		else
			rowItemClassPath = MagicLoaderUtils.getDynamicRowItemClassName(null);
		return super.listWithSql(hql.toString(), params, "", rowItemClassPath, start, count);
	}
	public MagicObjectRegion getObjectRegion(String objectId,String spaceRegionName){
		StringBuilder hql = new StringBuilder("from MagicObjectRegion where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(objectId!=null && !"".equals(objectId)) {
			hql.append(" and objectId=:objectId");
			params.put("objectId", objectId);
		}
		if(spaceRegionName!=null) {
			hql.append(" and name=:spaceRegionName");
			params.put("spaceRegionName", spaceRegionName);
		}
		return (MagicObjectRegion) super.getObject(hql.toString(), params);
	}
	public MagicSpaceRegion getSpaceRegion(String spaceName, String name) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegion where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(name)) {
			hql.append(" and name=:name");
			params.put("name", name);
		}
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		return (MagicSpaceRegion) super.getObject(hql.toString(), params);
	}
	public Boolean deleteMagicObjectById(String objectId) {
		String hql = "delete from MagicObject r where r.id=?";
		return super.deleteById(hql,objectId);
	}
	public Boolean deleteMagicObjectRegionByObjId(String objectId) {
		String hql = "delete from MagicObjectRegion r where r.objectId=?";
		return super.deleteById(hql,objectId);
	}
	public Boolean deleteRowItemByObjectId(String objectId) {
		String hql = "delete from MagicRowItem r where r.objectId=?";
		return super.deleteById(hql,objectId);
	}
	public Boolean deleteMagicRegionRowByObjId(String objectId) {
		String hql = "delete from MagicRegionRow r where r.objectId=?";
		return super.deleteById(hql,objectId);
	}
	@SuppressWarnings("unchecked")
	public List<MagicObjectRegion> listMagicObjectRegion(String objectId,String spaceId,Integer businessType,String orderBy,Integer start,Integer count) {
		StringBuilder hql = new StringBuilder("select r from MagicObjectRegion r where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		buildMagicObjectRegionQuery(hql, objectId, spaceId, businessType, params);
		if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by r."+orderBy);
		return super.list(hql.toString(), params, 0, 10000);
	}
	private void buildMagicObjectRegionQuery(StringBuilder query,String objectId, String spaceId,
			Integer businessType, Map<String, Object> values) {
		if(objectId!=null && !"".equals(objectId)) {
			query.append(" and objectId =:objectId");
			values.put("objectId", objectId);
		}
		if(spaceId!=null&& !"".equals(spaceId)) {
			query.append(" and spaceId =:spaceId");
			values.put("spaceId", spaceId);
		}
		if(businessType!=null) {
			query.append(" and businessType =:businessType");
			values.put("businessType", businessType);
		}
	}
	public Boolean saveMagicSpaceRegion(MagicSpaceRegion spaceRegion) {
		return super.save(spaceRegion);
	}
	public Boolean saveSpace(MagicSpace space) {
		return super.save(space);
	}
	public Boolean saveMagicDimension(MagicDimension dimension) {
		return super.save(dimension);
	}
	public Boolean saveReginCodeLnk(MagicRegionCodeLnk codeLnk) {
		return super.save(codeLnk);
	}
	@SuppressWarnings("unchecked")
	public List<MagicRegionRow> listRow(String partition,String spaceName,String regionName,String displayName,String objectId,
			Boolean valid,List<MagicDimension> searchCriterias,String orderBy, Integer start, Integer count) {
		if(StringUtils.isEmpty(partition))
			partition = ServiceStaticInfo.TABLE_PREFIX;
		MagicSpaceRegion spaceRegion = getSpaceRegion(spaceName, regionName);
		if(spaceRegion==null)
			throw new RuntimeException("spaceRegion is null");
		List<MagicDimension> dimensions = this.listDimension(null,spaceName, spaceRegion.getId(), null, null, 
				MagicDimension.Destination.FOR_DATA.getCode(), " seq ");
		Map<String,Object> params = new HashMap<String,Object>();
		String header = "select distinct row.* from "+ServiceStaticInfo.TABLE_PREFIX+"_ROW row ,(select a.ROW_ID from (select ROW_ID from "
				+partition+"_ROW_ITEM where SPACE_REGION_NAME=:spaceRegionName ";
		if(StringUtils.isNotEmpty(objectId))
			header+=objectQuery;
		if(StringUtils.isNotEmpty(displayName)) {
			header+=dimensionQuery;
			params.put("dimensionDisplayName", displayName);
		}
		header+= "group by ROW_ID) as a  ";
		StringBuilder hql = new StringBuilder(header);
		buildListRowItemValueMap(hql,partition,objectId,dimensions,searchCriterias,spaceRegion.getName(), params);
		hql.append(") x where x.ROW_ID=row.ID ");
		if(valid!=null) {
			hql.append(" and row.valid=:valid");
			params.put("valid", valid);
		}
		if(orderBy!=null && !"".equals(orderBy))
			hql.append(" order by row."+orderBy);
		return super.listWithSql(hql.toString(), params, "", MagicRegionRow.class.getCanonicalName(), start, count);
	}
	public Integer listRowCount(String partition,String spaceName,String regionName,String displayName, String objectId, Boolean valid, List<MagicDimension> queryConditions) {
		MagicSpaceRegion spaceRegion = getSpaceRegion(spaceName, regionName);
		if(spaceRegion==null)
			throw new RuntimeException("spaceRegion is null");
		if(StringUtils.isEmpty(partition))
			partition = ServiceStaticInfo.TABLE_PREFIX;
		List<MagicDimension> dimensions = this.listDimension(null,spaceName, spaceRegion.getId(), null, null, null, " seq ");
		Map<String,Object> params = new HashMap<String,Object>();
		String header = "select count(*) from (select distinct row.* from "+ServiceStaticInfo.TABLE_PREFIX+"_INFO row ,(select a.ROW_ID from (select ROW_ID from "
				+partition+"_ROW_ITEM where SPACE_REGION_NAME=:spaceRegionName ";
		if(StringUtils.isNotEmpty(objectId))
			header+=objectQuery;
		if(StringUtils.isNotEmpty(displayName)) {
			header+=dimensionQuery;
			params.put("dimensionDisplayName", displayName);
		}
		header+= "group by ROW_ID) as a  ";
		StringBuilder hql = new StringBuilder(header);
		buildListRowItemValueMap(hql,null,objectId,dimensions,queryConditions,spaceRegion.getName(), params);
		hql.append(") x where x.ROW_ID=row.ID ");
		if(valid!=null) {
			hql.append(" and row.valid=:valid");
			params.put("valid", valid);
		}
		hql.append(" ) y");
		return super.listCountWithSQL(hql.toString(), params);
	}
	public MagicDimension getDimensionById(String id) {
		return (MagicDimension) super.getObject(MagicDimension.class, id);
	}
	public MagicDimension getDimension(String spaceName,String regionName, String displayName, Integer destination) {
		StringBuilder hql = new StringBuilder("from MagicDimension where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(displayName)) {
			hql.append(" and displayName=:displayName");
			params.put("displayName", displayName);
		}
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and spaceRegionName=:regionName");
			params.put("regionName", regionName);
		}
		if(destination!=null) {
			hql.append(" and destination=:destination");
			params.put("destination", destination);
		}
		return (MagicDimension) super.getObject(hql.toString(), params);
	}
	public Boolean saveSpaceRegionView(MagicSpaceRegionView view) {
		return super.save(view);
	}
	public Boolean saveSpaceRegionViewItem(MagicSpaceRegionViewItem item) {
		return super.save(item);
	}
	@SuppressWarnings("unchecked")
	public List<MagicSpaceRegionViewItem> listSpaceRegionViewItem(String spaceName,String regionName,String viewName, String orderBy) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegionViewItem where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and regionName=:regionName");
			params.put("regionName", regionName);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and viewName=:viewName");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(orderBy))
			hql.append(" order by "+orderBy);
		return super.list(hql.toString(), params,0,100);
	}
	public MagicSpaceRegionViewItem getSpaceRegionViewItem(String spaceId,String spaceRegionId,String viewName,String dimensionId) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegionViewItem where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceId)) {
			hql.append(" and spaceId=:spaceId");
			params.put("spaceId", spaceId);
		}
		if(StringUtils.isNotEmpty(spaceRegionId)) {
			hql.append(" and spaceRegionId=:spaceRegionId");
			params.put("spaceRegionId", spaceRegionId);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and viewName=:viewName");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(dimensionId)) {
			hql.append(" and dimensionId=:dimensionId");
			params.put("dimensionId", dimensionId);
		}
		return (MagicSpaceRegionViewItem) super.getObject(hql.toString(), params);
	}
	public MagicSpaceRegionView getSpaceRegionView(String spaceName,String spaceRegionName,
			String viewName) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegionView where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(spaceRegionName)) {
			hql.append(" and spaceRegionName=:spaceRegionName");
			params.put("spaceRegionName", spaceRegionName);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and name=:viewName");
			params.put("viewName", viewName);
		}
		return (MagicSpaceRegionView) super.getObject(hql.toString(), params);
	}
	public MagicSpaceRegionViewItem getSpaceRegionViewItemWithName(String spaceName,String spaceRegionName,
			String viewName,String dimensionId) {
		StringBuilder hql = new StringBuilder("from MagicSpaceRegionViewItem where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(spaceRegionName)) {
			hql.append(" and spaceRegionName=:spaceRegionName");
			params.put("spaceRegionName", spaceRegionName);
		}
		if(StringUtils.isNotEmpty(viewName)) {
			hql.append(" and viewName=:viewName");
			params.put("viewName", viewName);
		}
		if(StringUtils.isNotEmpty(dimensionId)) {
			hql.append(" and dimensionId=:dimensionId");
			params.put("dimensionId", dimensionId);
		}
		return (MagicSpaceRegionViewItem) super.getObject(hql.toString(), params);
	}
	public Boolean saveChoiceItem(MagicChoiceItem choiceItem){
		return super.save(choiceItem);
	}
	@SuppressWarnings("unchecked")
	public List<MagicChoiceItem> listChoiceItemByName(String choiceName){
		StringBuilder hql = new StringBuilder("select r from MagicChoiceItem r where 1=1");
		Map<String,Object> params = new HashMap<String,Object>();
		if(choiceName!=null && !"".equals(choiceName)){
			hql.append(" and r.choiceName=:choiceName");
			params.put("choiceName", choiceName);
		}
		hql.append(" order by r.seq asc");
		return super.list(hql.toString(), params, 0, 10000);
	}
	public Boolean saveCodeLib(MagicCodeLib lib) {
		return super.save(lib);
	}
	public MagicCodeLib getCodeLibById(String id) {
		return (MagicCodeLib) super.getObject(MagicCodeLib.class, id);
	}
	public MagicRegionCodeLnk getCodeLnk(String spaceName,String regionName,String signature) {
		StringBuilder hql = new StringBuilder("from MagicRegionCodeLnk where 1=1"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and regionName=:regionName");
			params.put("regionName", regionName);
		}
		if(StringUtils.isNotEmpty(signature)) {
			hql.append(" and signature=:signature");
			params.put("signature", signature);
		}
		return (MagicRegionCodeLnk) super.getObject(hql.toString(), params);
	}
	@SuppressWarnings("unchecked")
	public List<MagicCodeLib> listCodeLib(String spaceName,String regionName, Integer codeType) {
		StringBuilder hql = new StringBuilder("select lib from MagicCodeLib lib,MagicRegionCodeLnk lnk where lib.id=lnk.codeLidId"); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(spaceName)) {
			hql.append(" and lnk.spaceName=:spaceName");
			params.put("spaceName", spaceName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and lnk.regionName=:regionName");
			params.put("regionName", regionName);
		}
		if(StringUtils.isNotEmpty(regionName)) {
			hql.append(" and lib.codeType=:codeType");
			params.put("codeType", codeType);
		}
		
		return super.list(hql.toString(), params,0,10000);
	}
	public Boolean isFreshValue(String partition,String spaceName,String regionName,String displayName,String value) {
		StringBuilder hql = new StringBuilder("select count(*) from "+partition+"_ROW_ITEM a where ");
		Map<String,Object> params = new HashMap<String,Object>();
		hql.append(" a.STR_VALUE=:value");
		params.put("value", value);
		hql.append(" and a.SPACE_NAME=:spaceName");
		params.put("spaceName", spaceName);
		hql.append(" and a.SPACE_REGION_NAME=:regionName");
		params.put("regionName", regionName);
		hql.append(" and a.DISPLAY_NAME=:displayName");
		params.put("displayName", displayName);
		return super.listCountWithSQL(hql.toString(), params).intValue()==0;
	}
	@SuppressWarnings("unchecked")
	public List<MagicSuperRowItem> getRowItem(String partition,String spaceName,String regionName,String displayName,Object value) {
		StringBuilder hql = new StringBuilder("select a.* from "+partition+"_ROW_ITEM a where ");
		Map<String,Object> params = new HashMap<String,Object>();
		hql.append(" (a.STR_VALUE=:value or a.NUM_VALUE:=numValue or a.DATE_VALUE=:dateValue)");
		params.put("value", value);
		params.put("numValue", value);
		params.put("dateValue", value);
		hql.append(" and a.SPACE_NAME=:spaceName");
		params.put("spaceName", spaceName);
		hql.append(" and a.SPACE_REGION_NAME=:regionName");
		params.put("regionName", regionName);
		hql.append(" and a.DISPLAY_NAME=:displayName");
		params.put("displayName", displayName);
		String className = MagicLoaderUtils.getDynamicRowItemClassName(partition);
		return super.listWithSql(hql.toString(),params,null,className,0,1000 );
	}
	public MagicCodeLib getJavaCodeLibBySignature(String signature) {
		String hql = "from MagicCodeLib where signature=:signature and codeType=0";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("signature", signature);
		return (MagicCodeLib) super.getObject(hql, params);
	}
}
