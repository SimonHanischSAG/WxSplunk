package com.softwareag.wx.log.message;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.softwareag.wx.log.helper.Configuration;

/**
 * <p>
 * Message catalog is an extension to the CompositeConfiguration class that
 * comes with the Apache Commons Configuration library. It provides convenience
 * methods to deal with a set of pre-defined log messages. This should be
 * preferred over the use of arbitrary free-text messages for a number of
 * reasons
 * <ul>
 * <li>It is the only way to allow for localization of message.
 * <li>With free text you never know what log messages might come up
 * <li>Automated log file monitoring with system management tools is greatly
 * simplified if each clear text (for the human reader) is accompanied by an ID.
 * While this could be achieved with free-text logging, it is usually simpler
 * done with a catalog
 * </ul>
 * <p/>
 * <p>
 * It is implemented as a singleton and therefore all access needs to happen via
 * Catalog.getInstance().<i>myMethod()</i>
 * </p>
 * 
 * @author Christoph Jahn
 * 
 */

public class Catalog extends Configuration {

	private String _locale = null;
	
	protected static Catalog _instance = new Catalog();
	
	public static Catalog getInstance() {
		return _instance;
	}
	
	/**
	 * Add messages to the catalog.
	 * 
	 * @param fileName
	 *            Absolute path of the config file
	 * @throws ConfigurationException
	 */
	public XMLConfiguration addXMLCfgFile(String fileName) {
		XMLConfiguration newCfg = super.addXMLCfgFile(fileName);
		if (getLocale() == null)
			this.setLocale(this.getDefaultLocale());
		return newCfg;
	}

	/**
	 * Retrieve the message for a set of criteria. The locale is automatically
	 * determined by going to the "locale" element of the config file, but can
	 * also be changed using the @see Catalog#setLocale(String) method
	 * 
	 * @param componentKey
	 *            Key attribute of a component
	 * @param facilityKey
	 *            Key attribute of the facility
	 * @param messageKey
	 *            Key attribute of the message
	 * @return String for logging.
	 */
	public String getMsg(String componentKey, String facilityKey,
			String messageKey) {
		return this.getString("componentList/component[@key='" + componentKey
				+ "']/facilityList/facility[@key='" + facilityKey
				+ "']/messageList/message[@key='" + messageKey
				+ "']/text[@locale='" + getLocale() + "']");
	}

	/**
	 * Retrieve the default log level for a message
	 * 
	 * @param componentKey
	 *            Key attribute of a component
	 * @param facilityKey
	 *            Key attribute of the facility
	 * @param messageKey
	 *            Key attribute of the message
	 * @return Default log level.
	 */
	public String getMsgLogLevel(String componentKey, String facilityKey,
			String messageKey) {
		String result = null;
		try {
			result = this.getString("componentList/component[@key='"
					+ componentKey + "']/facilityList/facility[@key='"
					+ facilityKey + "']/messageList/message[@key='"
					+ messageKey + "']/@level");
		} catch (NullPointerException e) {
		}
		// Fall back to global default level		
		if (result == null)
			result = getDefaultLogLevel();
		return result;
	}

	/**
	 * Retrieve the log level of a message for a specific logger. If no
	 * logger-specific level is defined, the message's default level will be
	 * returned.
	 * 
	 * The use-case is that the same message can originate from different
	 * "places" (=loggers) and have a different priority depending on where
	 * it came from (e.g. a service is called from different processes). 
	 * 
	 * @param componentKey
	 *            Key attribute of a component
	 * @param facilityKey
	 *            Key attribute of the facility
	 * @param messageKey
	 *            Key attribute of the message
	 * @param loggerName
	 *            Logger name
	 * @return Log level for logger.
	 */
	public String getMsgLogLevel(String componentKey, String facilityKey,
			String messageKey, String loggerName) {
		String result = null;
		try {
			result = this.getString("componentList/component[@key='"
					+ componentKey + "']/facilityList/facility[@key='"
					+ facilityKey + "']/messageList/message[@key='"
					+ messageKey + "']/level[@logger='" + loggerName + "']");
		} catch (NullPointerException e) {
		}
		
		// Fall back to message default level		
		if (result == null)
			result = getMsgLogLevel(componentKey, facilityKey, messageKey);

		return result;
	}

	/**
	 * Retrieve the global default log level
	 * 
	 * @return Default log level.
	 */
	public String getDefaultLogLevel() {
		return this.getString("/defaultLogLevel");
	}

	/**
	 * Retrieve the default locale to be used for log messages
	 * 
	 * @return Default log level.
	 */
	public String getDefaultLocale() {
		return this.getString("/locale");
	}

	/**
	 * Get current locale to be used for log messages
	 * 
	 * @return
	 */
	public String getLocale() {
		return this._locale;
	}

	/**
	 * Set current locale to be used for log messages
	 * 
	 * @param _locale
	 */
	public void setLocale(String _locale) {
		this._locale = _locale;
	}

}
