package com.softwareag.wx.log.router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoutingEntry {

	private String name = null;
	private ConcurrentHashMap<String, Condition> conditionList = new ConcurrentHashMap<String, Condition>();
	private ConcurrentHashMap<String, Destination> destinationList = new ConcurrentHashMap<String, Destination>();
	
	
	public boolean matches(Map<String, Object> paramList) {
		if (paramList == null)
			return false;
		if (paramList.size() != conditionList.size())
			return false;
		for (Condition cond : conditionList.values()) {
			if (paramList.containsKey(cond.getName())) {
				if (!cond.matches( paramList.get(cond.getName())))
					return false;
			} else
				return false;
		}
		return true;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ConcurrentHashMap<String, Condition> getConditionList() {
		return conditionList;
	}
	public void setConditionList(ConcurrentHashMap<String, Condition> condList) {
		this.conditionList = condList;
	}
	
	public void addToConditionList(Condition cond) {
		conditionList.put(cond.getName(), cond);
	}
	
	public ConcurrentHashMap<String, Destination> getDestinationList() {
		return destinationList;
	}
	
	public void setDestinationList(ConcurrentHashMap<String, Destination> destList) {
		this.destinationList = destList;
	}
	
	public void addToDestinationList(Destination dest) {
		destinationList.put(dest.getId(), dest);
	}
}
