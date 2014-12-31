package com.zdtx.ifms.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * 后台表单验证帮助类
 * @author zdtx_liujun
 * @since 2013-3-18 11:19:47
 */
public class ValidateUtil {

	private static Validator validator; // 线程安全的

	//初始化验证工具
	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	/**
	 * 验证实体类是否合法
	 * @param t 待验证对象
	 * @param i 验证组
	 * @return Boolean	false:未通过验证，页面alert出验证结果返回
	 * 										true:通过验证
	 */
	public static <T> Boolean validate(T t, Class<?>... i) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, i);
		if (constraintViolations.size() > 0) {	//存在未通过的规则
			String validateError = "保存失败，原因：\\r";
			int c = 1;
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {	//迭代不通过内容
				validateError += c + "、 " + constraintViolation.getMessage() + "\\r";	
				c ++;
			}
			Struts2Util.sendMsg(validateError);	//发送到页面
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}
}
