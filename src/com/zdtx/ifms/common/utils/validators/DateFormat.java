package com.zdtx.ifms.common.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 日期格式必须为"yyyy-MM-dd"
 * @author zdtx_liujun
 * @since 2013-01-28 15:16
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
@Documented
public @interface DateFormat {
    String message() default "日期格式错误！";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value() default "(19|20)\\d{2}-(0?\\d|1[012])-(0?\\d|[12]\\d|3[01])";
}
