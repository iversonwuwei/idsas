package com.zdtx.ifms.common.utils.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

    private Pattern pattern;
    
    @Override
    public void initialize(DateTimeFormat dateFormat) {
        this.pattern = Pattern.compile(dateFormat.value());
    }
    
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintContext) {
        if(date == null) {
            return false;
        }
        if(this.pattern.matcher(date).matches()) {
            return true;
        }
        return false;
    }
}