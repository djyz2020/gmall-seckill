package com.gmall.seckill.dao;

import com.gmall.seckill.po.SeckillGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface SeckillGoodsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    SeckillGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);
}