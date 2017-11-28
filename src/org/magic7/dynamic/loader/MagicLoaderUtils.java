package org.magic7.dynamic.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.core.service.ServiceStaticInfo;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

public class MagicLoaderUtils {
	public static final ClassPool pool = ClassPool.getDefault();
	public static final String packagePrefix = "org.magic7.core." + ServiceStaticInfo.CLASS_PREFIX + ".";
	private static HashMap<String, Object> objectPool = new HashMap<>();
	private static HashMap<String, Class<?>> classtPool = new HashMap<>();
	public static MagicService service = MagicServiceFactory.getMagicService();
	public static final String rowItemPathPrefix = "org.magic7.core.domain.";
	public static final String rowItemClassSuffix = "RowItem";
	public static final String defaultPartition = "Magic";

	public static String generateSignatur(String spaceName, String regionName, MagicCodeLib codeLib) {
		String classPath = packagePrefix + spaceName + "." + regionName;
		CtClass cls = pool.makeClass(classPath);
		try {
			cls.setSuperclass(pool.get("org.magic7.core.service.MagicRegionShell"));
			pool.importPackage("org.magic7.core.domain.MagicSuperRowItem");
			pool.importPackage("org.apache.commons.lang.StringUtils");
			pool.importPackage("org.magic7.utils.ServiceUtil");
			pool.importPackage("org.magic7.core.domain.MagicRegionRow");
			pool.importPackage("java.math.BigDecimal");
			pool.importPackage("java.util.Date");
			
			if (codeLib.getPackages() != null) {
				String packages[] = codeLib.getPackages().split(";");
				for (String packagE : packages)
					pool.importPackage(packagE);
			}
			CtMethod method = CtNewMethod.make(codeLib.getCode(), cls);
			if (method == null)
				return null;
			return method.getName() + method.getSignature();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void generateRegionClass(String spaceName, String regionName) {
		String classPath = packagePrefix + spaceName + "." + regionName;
		CtClass cls = pool.makeClass(classPath);
		try {
			cls.setSuperclass(pool.get("org.magic7.core.service.MagicRegionShell"));
			List<MagicCodeLib> libs = service.listCodeLibWithLnk(spaceName, regionName,
					MagicCodeLib.CodeType.JAVA.getCode());

			pool.importPackage("org.magic7.core.domain.MagicSuperRowItem");
			pool.importPackage("org.apache.commons.lang.StringUtils");
			pool.importPackage("org.magic7.utils.ServiceUtil");
			pool.importPackage("org.magic7.core.domain.MagicRegionRow");
			pool.importPackage("java.math.BigDecimal");
			pool.importPackage("java.util.Date");
			
			for (MagicCodeLib codeLib : libs) {
				if (codeLib.getPackages() != null) {
					String packages[] = codeLib.getPackages().split(";");
					for (String packagE : packages)
						pool.importPackage(packagE);
				}
				CtMethod method = CtNewMethod.make(codeLib.getCode(), cls);
				cls.addMethod(method);
			}
			classtPool.put(classPath, cls.toClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String generateJsScript(String spaceName, String regionName) {
		List<MagicCodeLib> libs = service.listCodeLibWithLnk(spaceName, regionName, MagicCodeLib.CodeType.JS.getCode());
		String script = "";
		for (MagicCodeLib codeLib : libs) {
			script += codeLib.getCode();
		}
		script = "<script>" + script + "</script>";
		return script;
	}

	public static void generateEntityClass(String spaceName, String prefix) {
		String name = prefix + rowItemClassSuffix;
		String classPath = rowItemPathPrefix + name;
		pool.importPackage(rowItemPathPrefix + "MagicSuperRowItem");
		pool.importPackage("javax.persistence.Table");
		pool.importPackage("javax.persistence.Entity");
		CtClass cls = pool.makeClass(classPath);
		try {
			cls.setSuperclass(pool.get(rowItemPathPrefix + "MagicSuperRowItem"));
			ClassFile ccFile = cls.getClassFile();
			ConstPool constpool = ccFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
			Annotation annot = new Annotation("javax.persistence.Entity", constpool);
			annot.addMemberValue("name", new StringMemberValue(prefix + "_ROW_ITEM", constpool));
			attr.addAnnotation(annot);
			ccFile.addAttribute(attr);
			ccFile.setMinorVersion(49);
			ccFile.setMajorVersion(51);
			outPutClass(MagicSuperRowItem.class.getResource(".").getPath(), name, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MagicCodeLib createJavaCodeLib(String spaceName, String regionName, String methodName, String imports,
			String parameterNames, String methodContent) {
		MagicCodeLib lib = new MagicCodeLib();
		lib.setName(methodName);
		lib.setPackages(imports);
		lib.setCode(methodContent);
		lib.setParameterNames(parameterNames);
		lib.setCodeType(MagicCodeLib.CodeType.JAVA.getCode());
		String signature = MagicLoaderUtils.generateSignatur(spaceName, regionName, lib);
		lib.setSignature(signature);
		return lib;
	}

	public static MagicCodeLib createJsCodeLib(String spaceName, String regionName, String methodName,
			String parameterNames, String methodContent) {
		MagicCodeLib lib = new MagicCodeLib();
		lib.setName(methodName);
		lib.setCode(methodContent);
		lib.setParameterNames(parameterNames);
		lib.setCodeType(MagicCodeLib.CodeType.JS.getCode());
		return lib;
	}

	public static void invokeRegionCode(String spaceName, String regionName, String methodName, Object[] params)
			throws Exception {
		String classPath = packagePrefix + spaceName + "." + regionName;
		Object instance = objectPool.get(classPath);
		Class<?> paramTypes[] = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			if (params[i].getClass().getSuperclass().equals(MagicSuperRowItem.class))
				paramTypes[i] = params[i].getClass().getSuperclass();
			else
				paramTypes[i] = params[i].getClass();
		}
		Class<?> clazz = classtPool.get(classPath);
		if (instance == null) {
			if (clazz == null) {
				clazz = Class.forName(classPath);
				classtPool.put(classPath, clazz);
			}
			System.out.println(params[0]);
			instance = clazz.newInstance();
			objectPool.put(classPath, instance);
		}
		Method method = clazz.getMethod(methodName, paramTypes);
		method.invoke(instance, params);
	}

	public static void outPutClass(CtClass cls, String className) {
		try {
			byte[] byteArr = cls.toBytecode();
			FileOutputStream fos = new FileOutputStream(new File("c://temp//" + className + ".class"));
			fos.write(byteArr);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDynamicRowItemClassName(String prefix) {
		if (StringUtils.isEmpty(prefix))
			return rowItemPathPrefix + defaultPartition + rowItemClassSuffix;
		if (prefix.equals(ServiceStaticInfo.TABLE_PREFIX))
			prefix = defaultPartition;
		return rowItemPathPrefix + prefix + rowItemClassSuffix;
	}

	public static void outPutClass(String path, String className, CtClass cls) {
		try {
			byte[] byteArr = cls.toBytecode();
			System.out.println(new File(path + "//" + className + ".class").getCanonicalPath());
			FileOutputStream fos = new FileOutputStream(new File(path + "//" + className + ".class"));
			fos.write(byteArr);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateAllRegionClass() {
		List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(null, null, " seq ", 0, 1000);
		for (MagicSpaceRegion spaceRegion : spaceRegions) {
			MagicLoaderUtils.generateRegionClass(spaceRegion.getSpaceName(), spaceRegion.getName());
		}
	}

}
