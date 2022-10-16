package com.gmall.seckill.exception;

import java.util.List;

import com.gmall.seckill.common.AppStatus;
import com.gmall.seckill.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GmallExceptionHandler {

	@ExceptionHandler(value=Exception.class)
	public CommonResult<String> exceptionHandler(Exception e){
		log.error(e.getLocalizedMessage());
		if(e instanceof GmallException) {
			GmallException ex = (GmallException)e;
			return CommonResult.error(ex.getAppStatus());
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			AppStatus status = AppStatus.BIND_ERROR;
			status.formatMsg(msg);
			return CommonResult.error(status);
		} else {
			return CommonResult.error(AppStatus.SERVER_ERROR);
		}
	}
}
