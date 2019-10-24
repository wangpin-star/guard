package com.jinglun.guard.guest.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuestruntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuestruntimeException(Exception e,String message){
		log.info(e.toString());
		log.info(message);
	}
}
