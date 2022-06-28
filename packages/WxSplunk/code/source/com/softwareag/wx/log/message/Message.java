package com.softwareag.wx.log.message;

import java.text.MessageFormat;

import com.softwareag.wx.log.router.Destination;

public class Message {

	public enum Level {
		TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	}

	public static String MSG_KEY_LEFT_BORDER = "(";
	public static String MSG_KEY_RIGHT_BORDER = ")";
	public static String MSG_KEY_SEPARATOR = ".";

	private Level level = null;
	private Level levelFromCatalog = null;
	private String text = null;
	private Object data = null;
	private String componentKey = null;
	private String facilityKey = null;
	private String messageKey = null;

	private boolean isCatalogBasedMessage = false;

	public void buildFromCatalog(String componentKey, String facilityKey,
			String messageKey, String[] messageParams, boolean sendKeyToLog) {

		this.setComponentKey(componentKey);
		this.setFacilityKey(facilityKey);
		this.setMessageKey(messageKey);
		this.isCatalogBasedMessage = true;

		// Try to get level from catalog
		try {
			this.levelFromCatalog = Level.valueOf(Catalog.getInstance()
					.getMsgLogLevel(componentKey, facilityKey, messageKey));
		} catch (NullPointerException e) {
		}

		String templateStr = null;
		try {
			templateStr = Catalog.getInstance().getMsg(componentKey,
					facilityKey, messageKey);
		} catch (NullPointerException e) {
		}

		String logMessage;

		if (!isEmptyString(templateStr)) {
			MessageFormat template = new MessageFormat(templateStr);
			logMessage = template.format(messageParams);

			// Prepend actual message with its catalog ID?
			if (sendKeyToLog)
				logMessage = logKeysToString(componentKey, facilityKey,
						messageKey) + " " + logMessage;
		} else {
			String paramString = "<none>";
			if (messageParams != null) {
				StringBuffer sb = new StringBuffer();
				for (String param : messageParams) {
					if (param == null)
						break;
					sb.append(param);
					sb.append("; ");
				}
				sb.delete(sb.length() - 2, sb.length()); // Remove last "; "
				paramString = sb.toString();
			}
			logMessage = "Unknown log message for "
					+ logKeysToString(componentKey, facilityKey, messageKey)
					+ " Params = " + paramString;
		}
		setText(logMessage);
	}

	public void buildFromCatalog(String componentKey, String facilityKey,
			String messageKey, String[] messageParams) {
		buildFromCatalog(componentKey, facilityKey, messageKey, messageParams,
				true);
	}

	/**
	 * Get message level for specific destination from catalog file.
	 * 
	 * @param destination
	 * @return If no destination-specific level was specified, the general one
	 *         for that message will returned. Will be NULL for messages
	 *         not catalog-based
	 */
	public Level getLevelFromCatalogForDestination(Destination destination) {
		if (isCatalogBasedMessage)
			return Level.valueOf(Catalog.getInstance().getMsgLogLevel(
					this.componentKey, this.facilityKey, this.messageKey,
					destination.getId()));
		else
			return null;
	}

	private static boolean isEmptyString(String s1) {
		return (s1 == null) || (s1.equals(""));
	}

	public static String logKeysToString(String componentKey,
			String facilityKey, String messageKey) {
		return MSG_KEY_LEFT_BORDER + componentKey + MSG_KEY_SEPARATOR
				+ facilityKey + MSG_KEY_SEPARATOR + messageKey
				+ MSG_KEY_RIGHT_BORDER;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setFacilityKey(String facilityKey) {
		this.facilityKey = facilityKey;
	}

	public String getFacilityKey() {
		return facilityKey;
	}

	public void setComponentKey(String componentKey) {
		this.componentKey = componentKey;
	}

	public String getComponentKey() {
		return componentKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public boolean isCatalogBasedMessage() {
		return isCatalogBasedMessage;
	}

	public void setCatalogBasedMessage(boolean isCatalogBasedMessage) {
		this.isCatalogBasedMessage = isCatalogBasedMessage;
	}

	public Level getLevelFromCatalog() {
		return levelFromCatalog;
	}

	public void setLevelFromCatalog(Level levelFromCatalog) {
		this.levelFromCatalog = levelFromCatalog;
	}

}
