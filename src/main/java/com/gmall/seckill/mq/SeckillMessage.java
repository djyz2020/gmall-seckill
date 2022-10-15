package com.gmall.seckill.mq;


import com.gmall.seckill.po.User;
import lombok.Data;

@Data
public class SeckillMessage {
	private User user;

	private long goodsId;
}
