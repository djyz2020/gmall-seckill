package com.gmall.seckill.service.ipml;

import com.gmall.seckill.dao.OrdeInfoMapper;
import com.gmall.seckill.po.OrderInfo;
import com.gmall.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdeInfoMapper ordeInfoMapper;

    @Override
    public long addOrder(OrderInfo orderInfo) {
        return ordeInfoMapper.insertSelective(orderInfo);
    }

    @Override
    public OrderInfo getOrderInfo(long orderId) {
        return ordeInfoMapper.selectByPrimaryKey(orderId);
    }
}
