package com.softwareag.wx.log.router;

public class StringCondition extends Condition {

	public StringCondition() {
		this.setType(ConditionFactory.Type.STRING);
	}
	
	@Override
	public boolean matches(Object compValue) {
		return this.getValue().equals(compValue);
	}

}
