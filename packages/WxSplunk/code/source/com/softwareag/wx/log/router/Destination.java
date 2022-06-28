package com.softwareag.wx.log.router;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import com.softwareag.wx.log.message.Message;
import com.softwareag.wx.log.message.Message.Level;
import com.softwareag.wx.log.router.DestinationFactory.Type;

/**
 * Base class for the various log destination types
 * 
 * @author Christoph Jahn
 * 
 */
public abstract class Destination {

	public enum ResultFromSend {
		OK, UNAVAILABLE, REFUSED, UNKNOWN
	}

	private String id = null;
	private Type type = null;
	protected Level level = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	};

	/**
	 * Set the type of the destination
	 * 
	 * @param type
	 *            Destination type
	 * @see Destination.Type List of possible values
	 */
	public void setType(String type) {
		this.type = Type.valueOf(type);
	}

	public abstract void parseCfg(HierarchicalConfiguration cfg);

	public abstract ResultFromSend send(Message message);
	
	public boolean equals(Destination dst) {
		return (dst != null && this.getId() == dst.getId() && this.getType() == dst.getType());
	}

}
