package com.kpn.opib.bam.data.service;

public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = 3524149718589077206L;

	OrderNotFoundException(String msg) {
		super(msg);
	}

	OrderNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

}
