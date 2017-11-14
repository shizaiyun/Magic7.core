package org.magic7.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.dao.BaseDao;


public class ServiceUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	public static Boolean notNull(Object object, String errorMessage) {
		if (object == null || "".equals(object.toString()))
			throw new RuntimeException(errorMessage);
		return true;
	}
	public static Object getObject(String className,String id) {
		if(StringUtils.isEmpty(className)||StringUtils.isEmpty(id))
			return null;
		String hql = "from "+className+" where id='"+id+"'";
		return BaseDao.getInstance().getObject(hql, null);
	}
	public static Object getObject(Class<?> clazz, String id) {
		return BaseDao.getInstance().getObject(clazz, id);
	}
	public static String generateShortEightUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null)
			return null;
		Map<String, Object> root = new HashMap<>();
		Map<String, Object> value = new HashMap<>();
		for (Field filed : obj.getClass().getFields()) {
			filed.setAccessible(true);
			try {
				value.put(filed.getName(), filed.get(obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		root.put(obj.getClass().getSimpleName(), value);
		return root;
	}
	private static final Pattern emailChecker=Pattern.compile("\\w+@\\w+(\\.\\w+)+");  
	public static boolean isEmail(String email) {
		return emailChecker.matcher(email).matches();
	}
	private static final Pattern phoneChecker=Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[01236789]))\\d{8}$");
	public static boolean isPhoneNumber(String phone) {
		return phoneChecker.matcher(phone).matches();
	}
}
