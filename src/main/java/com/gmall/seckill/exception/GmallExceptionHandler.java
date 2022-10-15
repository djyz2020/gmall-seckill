package com.gmall.seckill.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gmall.seckill.result.CodeMsg;
import com.gmall.seckill.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@ResponseBody
@Slf4j
public class GmallExceptionHandler {

	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(Exception e){
		log.error(e.getLocalizedMessage());
		if(e instanceof GmallException) {
			GmallException ex = (GmallException)e;
			return Result.error(ex.getCm());
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		} else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
