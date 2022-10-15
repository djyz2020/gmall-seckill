package com.gmall.seckill.mq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	public static final String MIAOSHA_QUEUE = "seckill.queue";

	@Bean
	public MessageConverter getMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
