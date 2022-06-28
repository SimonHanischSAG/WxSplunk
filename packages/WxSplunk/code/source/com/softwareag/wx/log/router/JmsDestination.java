package com.softwareag.wx.log.router;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import com.softwareag.wx.log.message.Message;

public class JmsDestination extends Destination {

	
	public enum TargetType {
		QUEUE, TOPIC
	}
	
	public JmsDestination() {
		this.setType(DestinationFactory.Type.JMS);
	}

	private TargetType targetType = null;

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	@Override
	public void parseCfg(HierarchicalConfiguration cfg) {
		this.targetType = TargetType.valueOf(cfg.getString("targetType"));
		if (this.targetType == null)
			throw new NullPointerException(
					"Missing configuration element 'targetType' for destination '"
							+ this.getId() + "'");

		
	}

	@Override
	public ResultFromSend send(Message message) {
		
		return ResultFromSend.UNKNOWN;
	}

	

}
