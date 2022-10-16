package com.gmall.seckill.controller;

import com.gmall.seckill.annotations.AccessLimit;
import com.gmall.seckill.po.GoodsBo;
import com.gmall.seckill.mq.MQSender;
import com.gmall.seckill.mq.SeckillMessage;
import com.gmall.seckill.common.RedisConst;
import com.gmall.seckill.po.OrderInfo;
import com.gmall.seckill.po.SeckillOrder;
import com.gmall.seckill.po.User;
import com.gmall.seckill.cache.GoodsKey;
import com.gmall.seckill.cache.RedisService;
import com.gmall.seckill.cache.UserKey;
import com.gmall.seckill.common.AppStatus;
import com.gmall.seckill.common.CommonResult;
import com.gmall.seckill.service.SeckillGoodsService;
import com.gmall.seckill.service.SeckillOrderService;
import com.gmall.seckill.util.CookieUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("seckill")
public class SeckillController implements InitializingBean {

    @Resource
    private RedisService redisService;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private MQSender mqSender;

    /**
     * 如果是集群情况下，需要达到一定量此缓存才能起到重大作用
     */
    private final HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 将库存初始化到本地缓存及redis缓存，原则上应该在创建秒杀活动时候触发的（为了演示，此项目没有创建活动逻辑，所有放在启动项目时候放进内存）
     */
    public void afterPropertiesSet() {
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        if (goodsList == null) {
            return;
        }
        for (GoodsBo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, String.valueOf(goods.getId()), goods.getStockCount(), RedisConst.RedisCacheExtime.GOODS_LIST);
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping("/seckill2")
    public String list2(Model model, @RequestParam("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        model.addAttribute("user", user);
        if (user == null) {
            return "index";
        }
        //判断库存
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", AppStatus.MIAO_SHA_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", AppStatus.REPEATE_MIAOSHA.getMsg());
            return "seckill_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillOrderService.insert(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    @RequestMapping(value = "/{path}/seckill", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> list(@RequestParam("goodsId") long goodsId,
                                      @PathVariable("path") String path,
                                      HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if (user == null) {
            return CommonResult.error(AppStatus.USER_NO_LOGIN);
        }
        //验证path
        boolean check = seckillOrderService.checkPath(user, goodsId, path);
        if (!check) {
            return CommonResult.error(AppStatus.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return CommonResult.error(AppStatus.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return CommonResult.error(AppStatus.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return CommonResult.error(AppStatus.REPEATE_MIAOSHA);
        }
        //入队
        SeckillMessage mm = new SeckillMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(mm);
        return CommonResult.success(0);
    }

    /**
     * 客户端轮询查询是否下单成功
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Long> miaoshaResult(@RequestParam("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if (user == null) {
            return CommonResult.error(AppStatus.USER_NO_LOGIN);
        }
        long result = seckillOrderService.getSeckillResult((long) user.getId(), goodsId);
        return CommonResult.success(result);
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<String> getMiaoshaPath(HttpServletRequest request, User user, @RequestParam("goodsId") long goodsId) {
        String loginToken = CookieUtil.readLoginToken(request);
        user = redisService.get(UserKey.getByName, loginToken, User.class);
        if (user == null) {
            return CommonResult.error(AppStatus.USER_NO_LOGIN);
        }
        String path = seckillOrderService.createMiaoshaPath(user, goodsId);
        return CommonResult.success(path);
    }
}
