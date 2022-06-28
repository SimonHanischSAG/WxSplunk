package com.softwareag.wx.log.router;

import com.softwareag.wx.log.router.ConditionFactory.Type;


public abstract class Condition {

	

	private String name = null;
	private Type type = null;
	private String value = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	};

	public void setType(String type) {
		this.type = Type.valueOf(type);	
	}
	
	public abstract boolean matches(Object compValue);
	

}
