## 1.项目涉及的技术介绍

**项目后端共12个模块**

**MySQL：**本项目涉及到的建表SQL已经添加到项目文件夹文件名 emp.sql，主要针对查询语句

**MyBatis:** DOA层的开发，MyBatis配合Mapper动态代理的方式访问数据库

**SpringBoot:** 主要还是Spring IOC和AOP的核心，自动装配省去我们以前Spring配置的繁琐

**SpringMVC:** RESTful接口设计和使用，应用于Web Controller接口的开发

**前端:**  1交互设计 2.Vuejs,3.JQuery

## 2. 模块设计

**通用工具类和VO，异常捕获等统一放在emporium-common模块下**

**所有微服务都统一注册到 emporium-registry模块中**

**emporium-gateway作为微服务的网关**

**商品管理的基本模块以及接口设计emporium-item,并为其他服务提供设计接口**

**文件上传模块 emporium-upload**

**前台页面搜索模块 emporium-search**

**用户鉴权 emporium-auth**

**购物车模块 emporium-cart**

**用户注册与登陆模块 emporium-user**

**用户下单支付模块 emporium-order**

**短信发送与验证模块 emporium-sms**

## Note

本项目的后端运行环境在CentOS7.2下完成的，所以项目中的MySQL运行在Docker容器里, Nginx，Redis, RabbitMQ等所有配置IP和端口号需要自行配置，或采用本项目中CentOS7默认环境，但.yml文件中的配置端口等个别配置需要根据你的环境进行配置



