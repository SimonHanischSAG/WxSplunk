package com.softwareag.wx.log.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.softwareag.wx.log.helper.Configuration;
import com.softwareag.wx.log.message.Message;

/**
 * <p>
 * Router is an extension to the CompositeConfiguration class that comes with
 * the Apache Commons Configuration library.
 * </p>
 * 
 * @author Christoph Jahn
 */

public class Router extends Configuration {

	protected static Router _instance = new Router();

	private ConcurrentHashMap<String, RoutingEntry> routingEntries = new ConcurrentHashMap<String, RoutingEntry>();

	/**
	 * Accessor method to the singleton
	 * 
	 * @return Instance to work with
	 */
	public static Router getInstance() {
		return _instance;
	}

	/**
	 * @param fileName
	 * @throws ConfigurationException
	 */
	@Override
	public XMLConfiguration addXMLCfgFile(String fileName) {
		XMLConfiguration newCfg = super.addXMLCfgFile(fileName);
		processCfg(newCfg);
		return newCfg;
	}

	@SuppressWarnings("unchecked")
	public void processCfg(HierarchicalConfiguration newCfg) {

		// Extract all destinations for later assignment to the entries
		Map<String, Destination> destinationList = parseCfgDestinationList(newCfg
				.configurationsAt("destinationList/destination"));

		List<HierarchicalConfiguration> cfgEntries = newCfg.configurationsAt("routingEntries/route");

		for (HierarchicalConfiguration cfgEntry : cfgEntries) {
			addRoutingEntry(parseCfgRoutingEntry(cfgEntry, destinationList));
		}
	}

	private Map<String, Destination> parseCfgDestinationList(
			List<HierarchicalConfiguration> destCfgList) {

		ConcurrentHashMap<String, Destination> destList = new ConcurrentHashMap<String, Destination>();
		for (HierarchicalConfiguration destCfg : destCfgList) {
			Destination dest = DestinationFactory.create(destCfg.getString(
					"@type").toUpperCase());
			dest.setId(destCfg.getString("@id"));
			dest.parseCfg(destCfg);
			destList.put(dest.getId(), dest);
		}
		return destList;
	}

	@SuppressWarnings("unchecked")
	private RoutingEntry parseCfgRoutingEntry(HierarchicalConfiguration cfgEntry,
			Map<String, Destination> destinationList) {
		RoutingEntry entry = new RoutingEntry();

		entry.setName(cfgEntry.getString("@name"));
		List<HierarchicalConfiguration> condCfgList = cfgEntry
				.configurationsAt("conditionList/condition");
		for (HierarchicalConfiguration condCfg : condCfgList) {
			Condition cond = ConditionFactory.create(condCfg.getString("@type")
					.toUpperCase());
			cond.setName(condCfg.getString("@name"));
			cond.setValue(condCfg.getString("."));
			entry.addToConditionList(cond);
		}

		//
		List<HierarchicalConfiguration> destCfgList = cfgEntry
				.configurationsAt("destinationList/destination");
		for (HierarchicalConfiguration destCfg : destCfgList) {
			String destID = destCfg.getString("@id");
			entry.addToDestinationList(destinationList.get(destID));
		}

		return entry;
	}

	public List<Destination> getDestinationList(Map<String, Object> paramList) {
		List<Destination> destList = new ArrayList<Destination>();
		for (RoutingEntry entry : routingEntries.values()) {
			if (entry.matches(paramList))
				destList.addAll(entry.getDestinationList().values());
		}
		return destList;
	}

	public void send(Map<String, Object> paramList, Message message) {
		for (Destination dest : this.getDestinationList(paramList)) {
			dest.send(message);
		}
	}

	public Map<String, Destination.ResultFromSend> send(
			List<Destination> destinationList, Message message) {
		Map<String, Destination.ResultFromSend> resultList = new ConcurrentHashMap<String, Destination.ResultFromSend>();
		for (Destination destination : destinationList) {
			resultList.put(destination.getId(), destination.send(message));
		}
		return resultList;
	}

	public ConcurrentHashMap<String, RoutingEntry> getRoutingEntries() {
		return routingEntries;
	}

	public void setRoutingEntries(
			ConcurrentHashMap<String, RoutingEntry> routingEntries) {
		this.routingEntries = routingEntries;
	}

	public RoutingEntry getRoutingEntry(String key) {
		return routingEntries.get(key);
	}

	public void addRoutingEntry(RoutingEntry entry) {
		routingEntries.put(entry.getName(), entry);
	}
}
