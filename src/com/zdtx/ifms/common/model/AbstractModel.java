package com.zdtx.ifms.common.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 通用类
 * 抽象POJO，业务POJO继承此类，省写toString、equals、hashCode方法
 * @author Leon Liu
 * @since 2013-2-25 9:19:29
 */
public abstract class AbstractModel implements java.io.Serializable {

	private static final long serialVersionUID = 2035013017939483936L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode(){  
	    return HashCodeBuilder.reflectionHashCode(this);  
	}
}
