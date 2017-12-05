package org.magic7.core.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.impl.MagicServiceImpl;
import org.magic7.utils.CacheUtil;
import org.magic7.utils.Dates;
import org.magic7.utils.SecurityUtil;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

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
	public static byte[] createValidationPicture(String code) {
		// image初始化
		BufferedImage image = new BufferedImage(90, 30, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		// 图片各属性设置
		graphics.setColor(new Color(255, 255, 255));
		graphics.drawRect(60, 30, 90, 30);
		graphics.fillRect(0, 0, 90, 30);
		graphics.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		// 图片中插入字母
		for (int i = 0; i < code.length(); i++) {
			String temp = code.substring(i, i + 1);
			graphics.setColor(new Color(102, 32, 176));
			graphics.drawString(temp, 13 * i + 6, 16);
		}
		graphics.dispose();

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JPEGImageEncoder jie = JPEGCodec.createJPEGEncoder(output);
		try {
			jie.encode(image);
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e1) {
		}
		return output.toByteArray();
	}
	
	public static void cacheFile(MagicSuperRowItem item,byte[] output) {
		CacheUtil.putFile(item.getRowId()+"."+item.getId(), output,null);
	}
	public static void cacheFile(MagicSuperRowItem item,byte[] output,int timeToLiveSeconds) {
		cacheFile(item.getRowId()+"."+item.getId(), output,timeToLiveSeconds);
	}
	public static void cacheFile(String key,byte[] output,int timeToLiveSeconds) {
		CacheUtil.putFile(key, output,timeToLiveSeconds);
	}
	public static void getCacheFile(MagicSuperRowItem item,byte[] output) {
		getCacheFile(item.getRowId()+"."+item.getId());
	}
	public static byte[] getCacheFile(String key) {
		return CacheUtil.getFile(key);
	}
	
	public static void cacheImage(MagicSuperRowItem item,byte[] output) {
		CacheUtil.putImage(item.getRowId()+"."+item.getId(), output,null);
	}
	public static void cacheImage(MagicSuperRowItem item,byte[] output,int timeToLiveSeconds) {
		cacheImage(item.getRowId()+"."+item.getId(), output,timeToLiveSeconds);
	}
	public static void cacheImage(String key,byte[] output,int timeToLiveSeconds) {
		CacheUtil.putImage(key, output,timeToLiveSeconds);
	}
	public static void getCacheImage(MagicSuperRowItem item,byte[] output) {
		getCacheImage(item.getRowId()+"."+item.getId());
	}
	public static byte[] getCacheImage(String key) {
		return CacheUtil.getImage(key);
	}
	public String generateRandomNum(int length) {
		return SecurityUtil.generateRandomNum(length);
	}
	
	public static void cacheData(MagicSuperRowItem item,byte[] output) {
		CacheUtil.putData(item.getRowId()+"."+item.getId(), output,null);
	}
	public static void cacheData(MagicSuperRowItem item,byte[] output,int timeToLiveSeconds) {
		cacheData(item.getRowId()+"."+item.getId(), output,timeToLiveSeconds);
	}
	public static void cacheData(String key,byte[] output,int timeToLiveSeconds) {
		CacheUtil.putData(key, output,timeToLiveSeconds);
	}
	public static void getCacheData(MagicSuperRowItem item,byte[] output) {
		getCacheData(item.getRowId()+"."+item.getId());
	}
	public static byte[] getCacheData(String key) {
		return CacheUtil.getData(key);
	}
}
