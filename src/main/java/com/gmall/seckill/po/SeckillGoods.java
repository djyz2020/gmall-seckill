package com.gmall.seckill.po;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SeckillGoods {
    private Long id;

    private Long goodsId;

    private BigDecimal seckilPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}