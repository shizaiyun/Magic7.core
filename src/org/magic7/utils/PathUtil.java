package org.magic7.utils;

import java.io.File;

/**
 * 读取路径
 * 

 */
public class PathUtil {

	/**
	 * classpath的获取(在Eclipse中为获得src或者classes目录的路径)
	 * 
	 * @return
	 */
	public static String classpath() {
		return Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ");
	}

	/**
	 * 获取项目的实际路径,eg:D:\workspace\labs
	 * 
	 * @return
	 */
	public static String projectRealPath() {
		return System.getProperty("user.dir") + "/";
	}

	/**
	 * 获取src目录的实际路径,eg:D:\workspace\labs/src/
	 * 
	 * @return
	 */
	public static String srcRealPath() {
		return System.getProperty("user.dir") + "/src/";
	}

	/**
	 * 获取webRoot路径
	 * @return
	 */
	public static String webRootPath() {
		return classpath().replace("WEB-INF/classes/", "").substring(1);
	}
	/**
	 * 获取目录的上一级
	 * @param path 目录路径
	 * @return
	 */
	public static String parentPath(String path){
		String parentPath = "";
		try {
			parentPath = new File(PathUtil.webRootPath()).getParent()+"/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentPath;
	}

	public static void main(String[] args) {
		System.out.println(PathUtil.srcRealPath());
		System.out.println(PathUtil.projectRealPath());
		System.out.println(PathUtil.webRootPath());
		System.out.println(parentPath(PathUtil.webRootPath()));
	}

}
