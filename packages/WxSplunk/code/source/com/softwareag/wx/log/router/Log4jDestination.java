package com.softwareag.wx.log.router;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.softwareag.wx.log.message.Message;

public class Log4jDestination extends Destination {

	public Log4jDestination() {
		this.setType(DestinationFactory.Type.LOG4J);
	}

	private String loggerName = null;

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public void parseCfg(HierarchicalConfiguration cfg) {
		this.loggerName = cfg.getString("loggerName");
		if (this.loggerName == null)
			throw new NullPointerException(
					"Missing configuration element 'loggerName' for destination '"
							+ this.getId() + "'");

		List<Object> cfgFileList = cfg.getList("configFile");
		for (Object cfgFile : cfgFileList) {
			final String s = (String) cfgFile;
			addDOMConfig(new File(s));
		}
	}

	@Override
	public ResultFromSend send(Message message) {
		Logger.getLogger(loggerName).log(getLogLevel(message),
				message.getText());
		return ResultFromSend.OK;
	}

	private boolean addDOMConfig(File log4jConfigFile) {
		if (log4jConfigFile.exists()) {
			DOMConfigurator
					.configureAndWatch(log4jConfigFile.getAbsolutePath());
			return true;
		} else {
			throw new IllegalArgumentException("Config file '"
					+ log4jConfigFile.getAbsolutePath() + "' does not exist");
		}

	}

	/**
	 * Get effective log level for message. If the message is catalog-based,
	 * this required an additional lookup as the whether the destination changes
	 * the level from its default to destination-specific value
	 * 
	 * @param message
	 * @return
	 */
	private Level getLogLevel(Message message) {
		if (message.isCatalogBasedMessage())
			return Level.toLevel(message
					.getLevelFromCatalogForDestination(this).toString());
		else
			return Level.toLevel(message.getLevel().toString());
	}

}
