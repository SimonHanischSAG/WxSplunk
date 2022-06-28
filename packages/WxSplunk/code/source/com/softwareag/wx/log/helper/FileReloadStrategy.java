package com.softwareag.wx.log.helper;

import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class FileReloadStrategy extends FileChangedReloadingStrategy {

	/**
	 * This is the default value (in milliseconds) how often a config file is to
	 * be checked for modification (value={@value #FILE_RELOAD_INTERVAL_DEFAULT}
	 * )
	 */
	public static final long FILE_RELOAD_INTERVAL_DEFAULT = 60000;

	/**
	 * This is the minimum value (in milliseconds) after that a config file is
	 * being checked for modification. If a value lower than this is provided in
	 * a config file, it will be overridden by this (
	 * {@value #FILE_RELOAD_INTERVAL_MIN})
	 */
	public static final long FILE_RELOAD_INTERVAL_MIN = 10000;

	/**
	 * This is the maximum value (in milliseconds) after that a config file is
	 * being checked for modification. If a value higher than this is provided
	 * in a config file, it will be overridden by this (
	 * {@value #FILE_RELOAD_INTERVAL_MAX})
	 */
	public static final long FILE_RELOAD_INTERVAL_MAX = 600000;

	/**
	 * Set up file reloading with the default interval (
	 * {@value #FILE_RELOAD_INTERVAL_DEFAULT} milliseconds)
	 */
	public FileReloadStrategy() {
		super();
		this.setReloadInterval(FILE_RELOAD_INTERVAL_DEFAULT);
	}

	/**
	 * Set up file reloading with individual interval
	 * 
	 * @param interval
	 *            Defines (in milliseconds) how often a file will be checked for
	 *            modification and reloaded automatically if a modification has
	 *            occurred. If this value exceeds the boundaries as defined by
	 *            {@link #FILE_RELOAD_INTERVAL_MIN} and
	 *            {@link #FILE_RELOAD_INTERVAL_MAX}, the respective min or max
	 *            value will be used instead and warning message sent to the
	 *            log. If a NULL value is provided, the default value of
	 *            {@value #FILE_RELOAD_INTERVAL_DEFAULT} will be used
	 */
	public FileReloadStrategy(Long interval) {
		super();
		setReloadInterval(interval);
	}

	/**
	 * Sets the
	 * 
	 * @param interval
	 *            {@inheritDoc #FileReloadStrategy(Long)}
	 * 
	 */
	private void setReloadInterval(Long interval) {
		if (interval == null) {
			interval = FILE_RELOAD_INTERVAL_DEFAULT;
		} else if (interval < FILE_RELOAD_INTERVAL_MIN) {
			interval = FILE_RELOAD_INTERVAL_MIN;
		} else if (interval > FILE_RELOAD_INTERVAL_MAX) {
			interval = FILE_RELOAD_INTERVAL_MAX;
		}
		this.setRefreshDelay(interval);
	}

}
