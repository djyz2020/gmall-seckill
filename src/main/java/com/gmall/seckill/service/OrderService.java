package com.gmall.seckill.service;

import com.gmall.seckill.po.OrderInfo;

public interface OrderService {

    long addOrder(OrderInfo orderInfo);

    OrderInfo getOrderInfo(long rderId);
}
