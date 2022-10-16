package com.gmall.seckill.cache;

public class AccessKey extends BasePrefix {

	private AccessKey( String prefix) {
		super(prefix);
	}
	
	public static AccessKey withExpire = new AccessKey("access");

}
