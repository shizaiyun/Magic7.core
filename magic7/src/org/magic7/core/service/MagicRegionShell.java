package org.magic7.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.impl.MagicServiceImpl;
import org.magic7.utils.Dates;

public class MagicRegionShell {
	private static MagicServiceImpl service = MagicServiceImpl.getInstance();
	public static final String YYYY_MM_DD_HH_mm = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	public boolean saveRowAndRowItem(MagicRegionRow row) {
		return MagicSpaceHandler.saveRow(row);
	}
	public boolean saveRow(MagicRegionRow row) {
		return service.saveRow(row);
	}
	public static MagicRegionRow cloneRow(MagicRegionRow source) {
		return MagicSpaceHandler.cloneRow(source);
	}
	public boolean saveRowItem(MagicSuperRowItem rowItem) {
		return service.saveRowItem(rowItem);
	}
	public MagicSuperRowItem getRowItemFromRow(MagicRegionRow row,String displayName) {
		return MagicSpaceHandler.getRowItemFromRow(row, displayName);
	}
	public List<MagicDimension> getQueryDimension(String spaceName,String regionName,Map<String,String> conditionPairs) {
		return MagicSpaceHandler.getQueryDimension(spaceName, regionName, conditionPairs);
	}
	public Date truncateDate(Date date) {
		return Dates.truncateDate(date);
	}
	public int compareDate(Date date, Date date2) {
		return Dates.compareDate(date, date2);
	}
	public List<MagicRegionRow> listRow(String spaceName,String regionName,String viewName,String dimensionName,String objectId,
			Boolean valid,List<MagicDimension> searchCriterias,String orderBy, Integer start, Integer count) {
		return MagicSpaceHandler.listRow(spaceName, regionName, viewName, dimensionName, objectId, valid, searchCriterias, orderBy, start, count);
	}
	public Integer listRowCount(String spaceName,String regionName,
			String displayName, String objectId, Boolean valid, List<MagicDimension> queryConditions) {
		return MagicSpaceHandler.listRowCount(spaceName, regionName, displayName, objectId, valid, queryConditions);
	}
	public List<MagicSuperRowItem> listRowItem(String spaceName,
			String regionName, String viewName,String displayName,String orderBy,Integer start, Integer count) {
		return MagicSpaceHandler.listRowItem(spaceName, regionName, viewName, displayName, orderBy, start, count);
	}
	public Date parseDate(String strDate, String dateFormat) {
		return Dates.parseDate(strDate, dateFormat);
	}
	public Date addMonth(Date date,int monthNum) {
		return Dates.addMonth(date, monthNum);
	}
	public Date addYears(Date date,int yearNum) {
		return Dates.addYears(date, yearNum);
	}
	public Date getDayEnd(Date date) {
		return Dates.getDayEnd(date);
	}
	public Date getDayStart(Date date) {
		return Dates.getDayStart(date);
	}
	public Date addHour(Date queryDate, int add) {
		return Dates.addHour(queryDate, add);
	}
	public Date getFirstDayOfMonth(Date date) {
		return Dates.getFirstDayOfMonth(date);
	}
	public Date getLastDayOfMonth(Date date) {
		return Dates.getLastDayOfMonth(date);
	}
	public Date addDay(Date date, int day) {
		return Dates.addDay(date,day);
	}
}
