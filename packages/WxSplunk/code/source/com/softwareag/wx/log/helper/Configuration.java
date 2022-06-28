package com.softwareag.wx.log.helper;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

/**
 * <p>
 * This is an extension to the CompositeConfiguration class that comes with the
 * Apache Commons Configuration library. It provides convenience methods to deal
 * with adding/removing XML config files.
 * <p/>
 * <p>
 * It is implemented as a singleton and therefore all access needs to happen via
 * Configuration.getInstance().<i>myMethod()</i>
 * </p>
 * 
 * @author Christoph Jahn
 * 
 */

public class Configuration extends CompositeConfiguration {

	protected static Configuration _instance = new Configuration();
	private ConcurrentHashMap<String, XMLConfiguration> _cfgFiles = new ConcurrentHashMap<String, XMLConfiguration>();

	/**
	 * Accessor method to the singleton
	 * 
	 * @return Instance to work with
	 */
	public static Configuration getInstance() {
		return _instance;
	}

	protected Configuration() {

	}

	/**
	 * Remove messages from the catalog.
	 * 
	 * @param fileName
	 *            Absolute path of the config file
	 * @throws ConfigurationException
	 */
	public void removeXMLCfgFile(String fileName) {
		if (_cfgFiles.containsKey(fileName)) {
			XMLConfiguration removeCfg = _cfgFiles.get(fileName);
			this.removeConfiguration(removeCfg);
		}
	}

	/**
	 * Add messages to the catalog.
	 * 
	 * @param fileName
	 *            Absolute path of the config file
	 * @throws ConfigurationException
	 */
	public XMLConfiguration addXMLCfgFile(String fileName) {

		// If config file was loaded before, remove it
		removeXMLCfgFile(fileName);

		XMLConfiguration newCfg = new XMLConfiguration();
		newCfg.setFileName(fileName);
		
		newCfg.setDelimiterParsingDisabled(true);


		// newCfg.setValidating(true);
		try {
			newCfg.load();
		} catch (ConfigurationException ce) {
			throw new UndeclaredThrowableException(ce);
		}
		newCfg.setExpressionEngine(new XPathExpressionEngine());

		newCfg.setReloadingStrategy(new FileReloadStrategy());

		_cfgFiles.put(fileName, newCfg);
		this.addConfiguration(newCfg);
		return newCfg;
	}
	
	/**
	 * Return configuration as read from file
	 * @param fileName
	 * @return Configuration object
	 */
	public XMLConfiguration getConfig(String fileName) {
		return _cfgFiles.get(fileName);
	}

}
