package com.softwareag.wx.log.router;

public class ConditionFactory {

	public enum Type {
		STRING, REGEX, FILE_INCLUDE
	}
	
	public static Condition create(Type type) {
		return create(type.toString());
	}
	
	
	public static Condition create(String type) {
		if (type.equals(ConditionFactory.Type.STRING.toString()))
			return new StringCondition();
		else if (type.equals(ConditionFactory.Type.REGEX.toString()))
			return new RegexCondition();
		else if (type.equals(ConditionFactory.Type.FILE_INCLUDE.toString()))
			return new FileIncludeCondition();
		else
			throw new IllegalArgumentException("Unsupported condition type '"
					+ type + "'");

	}

}
