package com.zdtx.ifms.common.utils.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrFutureValidator implements ConstraintValidator<StrFuture, String> {

	@Override
	public void initialize(StrFuture strFuture) {
	}
    
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintContext) {
        if(date == null) {
            return false;
        }
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date dat = sdf.parse(date);
        	Date today = sdf.parse(sdf.format(new Date()));	//以当前日0时0分0秒为基准
			if(dat.compareTo(today) >= 0) {	//日期晚于或等于当前日期
			    return true;
			}
		} catch (ParseException e) {
			return false;
		}
        return false;
    }
}
