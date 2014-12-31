package com.zdtx.ifms.common.utils.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 输入日期必须晚于等于当前日期
 * @author zdtx_liujun
 * @since 2013-01-28 15:15
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrFutureValidator.class)
@Documented
public @interface StrFuture {
    String message() default "日期不可早于当前日期！";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
