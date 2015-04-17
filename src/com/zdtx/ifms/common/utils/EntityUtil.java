package com.zdtx.ifms.common.utils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 帮助类 - 所有的帮助类需要定义通用接口 根据不同的需求 不同的实现
 * */


/**
 * @ClassName: EntityUtils
 * @Description: 实体类相关帮助类
 * @author Leon Liu
 * @date 2012-5-22 下午03:00:37
 * @version V1.0
 */

public  class EntityUtil {

	/**
	 * 根据类名获取类全名
	 * @param simpleName 类简称
	 * @return 类全名
	 */
//	public static String getFullNameBySimple(String simpleName) {
//		simpleName = simpleName.substring(0, 1).toUpperCase() + simpleName.substring(1);
//		FullClassNameGetter fullClassNameGetter = FullClassNameGetter.getInstance();
//		for(String name : fullClassNameGetter.getFullClassName(simpleName)) {
//			if(name.contains("com.zdtx")) {
//				return name;
//			}
//		}
//		return null;
//	}

	/**
	 * 根据getting方法获得属性名
	 * @return
	 */
	public static String getProByGetting(String getName) {
		String res = getName.substring(3);
		return res.substring(0, 1).toLowerCase() + res.substring(1);
	}

	/**
	 * 根据表头获取对应实体属性名（实体类getting方法注解必须写正确）
	 *
	 * @param entityName 实体类简名
	 * @param header
	 * @return
	 */
	public static String getColByComment(String entityName, String header) {
		try {
			//遍历实体类的所有getting方法，获取其注解的columnDefination
			Method[] methods =  Class.forName(entityName).getMethods();
			for (Method method : methods) {
				if(method.getDeclaredAnnotations().length == 0) { continue; }	//无注解方法，略过
				if(null == method.getAnnotation(Column.class)) {	//无Column注解
					if(null == method.getAnnotation(JoinColumn.class)) {	//也无JoinColumn注解，略过
						continue;
					} else {	//有JoinColumn注解
						@SuppressWarnings("rawtypes")
						Class c = method.getReturnType();	//获得关联对象
						if(null != getColByComment(c.getName(), header)) {
							return getProByGetting(method.getName()) + "." + getColByComment(c.getName(), header);	//嵌套执行
						} else {
							continue;
						}
					}
				} else if(null == method.getAnnotation(Column.class).columnDefinition() || "".equals(method.getAnnotation(Column.class).columnDefinition()) || "主键".equals(method.getAnnotation(Column.class).columnDefinition())) {	//有Column注解但无注释或注释为"主键"，略过
					continue;
				} else {
					if(header.equals(method.getAnnotation(Column.class).columnDefinition())) {
						return getProByGetting(method.getName());
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据属性名获取属性值
	 * @param <E>
	 * @param entity 对象实体
	 * @param fieldName 属性名，关联属性用"."连接
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <E> String getValueByField(final E entity, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		String value = "";
		Field field = null;
		if(Utils.isEmpty(fieldName)) {
			return value;
		}
		Class<?> clazz = entity.getClass();
		Object obj = entity;
		if(!fieldName.contains(".")) {	//非关联对象
			field = clazz.getDeclaredField(fieldName.trim());		//获得属性
		} else {	//关联属性
			String[] fields = fieldName.split("\\.");
			int len = fields.length;
			for (int i = 0; i < len - 1; i++) {	//获取最终的关联对象
				Field f = clazz.getDeclaredField(fields[i]);
				if(!Modifier.isPublic(f.getModifiers())) {		//设置非共有类属性可访问
					f.setAccessible(true);
				}
				clazz = f.getType();
				obj = f.get(obj);
			}
			field = clazz.getDeclaredField(fields[len - 1]);
		}
		if(!Modifier.isPublic(field.getModifiers())) {		//设置非共有类属性可访问
			field.setAccessible(true);
		}
		if (null != field.get(obj)) {
			if(field.getType() == Date.class) {	//日期类型
				value = String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(field.get(obj)));
			} else {
				value = String.valueOf(field.get(obj));
			}
		}
		return value;
	}
}
