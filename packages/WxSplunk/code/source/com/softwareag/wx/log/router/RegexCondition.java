package com.softwareag.wx.log.router;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCondition extends Condition {

	public RegexCondition() {
		this.setType(ConditionFactory.Type.REGEX);
	}

	@Override
	public boolean matches(Object compValue) {
		Pattern p = Pattern.compile(this.getValue());
		Matcher m = p.matcher((String) compValue);
		return m.matches();
	}

}
