package com.gmall.seckill.common;

public class CommonResult<T> {

	private int code;

	private String msg;

	private T data;

	public boolean isSuccess(){
		return this.code == AppStatus.SUCCESS.getCode();
	}

	public static  <T> CommonResult<T> success(T data){
		return new CommonResult<T>(data);
	}

	public static  <T> CommonResult<T> error(AppStatus codeMsg){
		return new CommonResult<T>(codeMsg);
	}

	private CommonResult(T data) {
		this.code = AppStatus.SUCCESS.getCode();
		this.msg = AppStatus.SUCCESS.getMsg();
		this.data = data;
	}

	private CommonResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private CommonResult(AppStatus codeMsg) {
		if(codeMsg != null) {
			this.code = codeMsg.getCode();
			this.msg = codeMsg.getMsg();
		}
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
