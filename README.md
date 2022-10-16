## 基于 SpringBoot+Mybatis+Redis+RabbitMQ 秒杀系统

## 项目启动说明

1、启动前，请配置 application.properties 中相关redis、mysql、rabbitmq（需要提前创建好队列，队列名称：seckill.queue）地址。

2、登录地址：http://localhost:8888/user/index   

3、商品秒杀列表地址：http://localhost:8888/goods/list

## 其它说明

1、数据库共有一千个用户左右（手机号：从18077200000~18077200998 密码为：123456），为压测准备的。（使用 com.gmall.seckill.util.UserUtil.java该类生成的，生成token做压测也是在此类里面）

2、秒杀优化项：
<ul>
    <li> 页面缓存、商品详情静态化、订单静态化 </li>
    <li> 加入消息队列RabbitMQ，对秒杀接口进行优化 </li>
    <li> 隐藏秒杀接口地址 </li>
    <li> 接口限流防刷 </li>
    <li> 解决超卖问题 </li>
</ul>

<br/>
<a href="https://blog.csdn.net/JokerLJG/article/details/119656022">浅析VO、DTO、DO、PO、POJO区别</a>
