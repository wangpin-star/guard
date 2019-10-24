package com.jinglun.guard.dataservice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataServiceException extends RuntimeException {
	public DataServiceException(String errMsg) {
		super(errMsg);
		log.error(errMsg);
	}
}
