package com.softwareag.wx.log.router;

public class DestinationFactory {

	public enum Type {
		LOG4J, JMS, BROKER, JDBC, SOAP, REST
	}
	
	public static Destination create(Type type) {
		return create(type.toString());

	}
	
	
	public static Destination create(String type) {
		if (type.equals(DestinationFactory.Type.LOG4J.toString()))
			return new Log4jDestination();
		if (type.equals(DestinationFactory.Type.JMS.toString()))
			return new JmsDestination();
		else
			throw new IllegalArgumentException("Unsupported destination type '"
					+ type + "'");

	}

}
