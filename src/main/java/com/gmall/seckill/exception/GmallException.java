package com.gmall.seckill.exception;


import com.gmall.seckill.common.AppStatus;

public class GmallException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private AppStatus appStatus;
	
	public GmallException(AppStatus appStatus) {
		super(appStatus.toString());
		this.appStatus = appStatus;
	}

	public AppStatus getAppStatus() {
		return appStatus;
	}

}
