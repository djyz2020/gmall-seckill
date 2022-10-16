package com.gmall.seckill.common;

public class RedisConst {

    public interface RedisCacheExtime {
        int REDIS_SESSION_EXTIME = 60 * 30;

        int GOODS_LIST = 60 * 30 * 24;

        int GOODS_ID = 60;

        int GOODS_INFO = 60;
    }
}
