package com.gmall.seckill.mq;

import com.gmall.seckill.po.GoodsBo;
import com.gmall.seckill.po.SeckillOrder;
import com.gmall.seckill.po.User;
import com.gmall.seckill.cache.RedisService;
import com.gmall.seckill.service.OrderService;
import com.gmall.seckill.service.SeckillGoodsService;
import com.gmall.seckill.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQReceiver {

    @Resource
    private SeckillGoodsService goodsService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        // todo 如果这里出现异常可以进行补偿，重试，重新执行此逻辑，如果超过一定次数还是失败可以将此秒杀置为无效，恢复redis库存
        log.info("receive message:" + message);
        SeckillMessage mm = RedisService.stringToBean(message, SeckillMessage.class);
        User user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsBo goods = goodsService.getseckillGoodsBoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillOrderService.insert(user, goods);
    }
}
