package com.kpn.bam.opib.connector;

/**
 * Extension of runtime exception represents Exception condition in Cordys
 * Connector.
 * 
 * @author gidwa500
 *
 */
public class ConnectorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor accepting String message.
	 * 
	 * @param msg
	 */
	public ConnectorException(String msg) {
		super(msg);
	}

	/**
	 * Constructor accepting String message and Throwable
	 * 
	 * @param msg
	 * @param cause
	 */
	public ConnectorException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
