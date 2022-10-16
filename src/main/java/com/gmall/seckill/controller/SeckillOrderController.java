package com.gmall.seckill.controller;

import com.gmall.seckill.po.GoodsBo;
import com.gmall.seckill.po.OrderInfo;
import com.gmall.seckill.po.User;
import com.gmall.seckill.redis.RedisService;
import com.gmall.seckill.redis.UserKey;
import com.gmall.seckill.common.AppStatus;
import com.gmall.seckill.common.CommonResult;
import com.gmall.seckill.service.SeckillGoodsService;
import com.gmall.seckill.service.SeckillOrderService;
import com.gmall.seckill.util.CookieUtil;
import com.gmall.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
public class SeckillOrderController {

    @Resource
    private RedisService redisService;

    @Resource
    private SeckillOrderService seckillOrderService;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public CommonResult<OrderDetailVo> info(Model model, @RequestParam("orderId") long orderId , HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null) {
            return CommonResult.error(AppStatus.USER_NO_LOGIN);
        }
        // TODO: 可自行扩展缓存中获取，请勿吐槽，此教程只是为了让大家知道整个流程，细节东西自行拓展
        OrderInfo order = seckillOrderService.getOrderInfo(orderId);
        if(order == null) {
            return CommonResult.error(AppStatus.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return CommonResult.success(vo);
    }
}
