package com.zdtx.ifms.common.utils.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串过去日期校验
 * @author zdtx_liujun
 */
public class StrPastValidator implements ConstraintValidator<StrPast, String> {

    @Override
    public void initialize(StrPast strPast) {
    }
    
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintContext) {
        if(date == null) {	//不可为空
            return false;
        }
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date dat = sdf.parse(date);
        	Date today = sdf.parse(sdf.format(new Date()));	//以当前日0时0分0秒为基准
			if(dat.compareTo(today) <= 0) {	//日期早于或等于当前日期
			    return true;
			}
		} catch (ParseException e) {
			return false;
		}
        return false;
    }
    
}
