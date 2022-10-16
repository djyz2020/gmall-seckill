package com.gmall.seckill.service;

import com.gmall.seckill.po.GoodsBo;
import com.gmall.seckill.po.OrderInfo;
import com.gmall.seckill.po.SeckillOrder;
import com.gmall.seckill.po.User;

public interface SeckillOrderService {

    SeckillOrder getSeckillOrderByUserIdGoodsId(long userId , long goodsId);

    OrderInfo insert(User user , GoodsBo goodsBo);

    OrderInfo getOrderInfo(long orderId);

    long getSeckillResult(Long userId, long goodsId);

    boolean checkPath(User user, long goodsId, String path);

    String createMiaoshaPath(User user, long goodsId);

}
