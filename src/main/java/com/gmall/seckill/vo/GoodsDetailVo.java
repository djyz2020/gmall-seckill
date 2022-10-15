package com.gmall.seckill.vo;

import com.gmall.seckill.bo.GoodsBo;
import com.gmall.seckill.po.User;
import lombok.Data;

@Data
public class GoodsDetailVo {

    private int miaoshaStatus = 0;

    private int remainSeconds = 0;

    private GoodsBo goods;

    private User user;
}
