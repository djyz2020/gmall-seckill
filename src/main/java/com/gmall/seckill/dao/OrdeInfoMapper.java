package com.gmall.seckill.dao;

import com.gmall.seckill.po.OrderInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdeInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}