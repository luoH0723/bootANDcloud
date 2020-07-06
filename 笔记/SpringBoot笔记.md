

# SpringBoot笔记

### 异步任务

```Java
@Async  //告诉spring这是一个异步方法
```

### 邮件任务

qq邮箱邮件授权密码

```
xxrlokecupsmdfjj
```

```java
//封装邮件发送方法
/**
     *
     * @param html
     * @param subject  邮件标题
     * @param text  邮件正文
     * @throws MessagingException
     * @Author lh
     */
    public void senMail(Boolean html,String subject,String text) throws MessagingException{
        //一个复杂的邮件发送
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,html);
        //正文
        helper.setSubject(subject);
        helper.setText(text,true);

        //附件
        helper.addAttachment("1.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));
        helper.addAttachment("2.jpg",new File("C:\\Users\\lh\\Pictures\\Camera Roll\\1.jpg"));

        helper.setTo("3247013580@qq.com");
        helper.setFrom("3035419253@qq.com");

        mailSender.send(mimeMessage);
    }
```

application.properties配置

```java
spring.mail.username=3035419253@qq.com
spring.mail.password=xxrlokecupsmdfjj
spring.mail.host=smtp.qq.com
#开启加密验证
spring.mail.properties.mail.smtp.ssl.enable=true
```

### 定时任务

```java
TaskScheduler  	任务调度者
TaskExecutor	任务执行者
//实现定时任务
    
@EnableScheduling   //开启定时功能的注解
@Scheduled	//什么时候执行
    
Cron表达式
    //Cron表达式
    //秒 分 时 日 月 周几
    @Scheduled(cron = "0 20 17 * * ?")  //在每天17时20分0秒执行
```



## 分布式 Dubbo+Zookeeper+springboot

RPC



HTTP SpringCloud



### 分布式理论

负载均衡：使用Nginx代理服务器

HTTP restful

RPC的两个核心：通讯，序列化





序列化：



netty：



Dubbo~

Java RPC框架

三大核心功能：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现



前台 中台 后台



zookeepe(管理）：hadoop hive-

注册中心



dubbo-admin：是一个剪口管理后台~查看我们注册了那些服务，哪些服务被消费了

Dubbo：jar包~



```java
//需要在pom中导入以下依赖
<!--导入依赖Dubbo+Zookeeper -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.7.7</version>
        </dependency>

        <!--  zkclient      -->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
        </dependency>

        <!--日志会冲突        -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
            <!--排除这个slf4j-log4j12-->
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```



步骤：

前提：zookeeper服务已开启

1.提供者提供服务

​	1.导入依赖

​	2.配置注册中心的地址，以及服务发现名和要扫描的包

​	3.在想要被注册的服务上面~增加一个注解@service

2.消费者如何消费

​	1.导入依赖

​	2.配置注册中心的地址，以及服务发现名，和要扫描的包

​	3.从远处注入服务~@Reference

## springcloud



```java
微服务架构会遇到的四个核心问题？

1.这么多服务，客户端该如何去访问？

2.这么多服务，服务之间如何进行通信？

3.这么多服务，如何治理？

4.服务挂了，怎么办？



解决方案：

springcloud，是一套生态，就是来解决以上分布式架构的4个问题

想使用springcloud，必须要掌握springboot，因为springcloud是基于springboot；



1.Spring Cloud NetFlix ，出来了一套解决方案！一站式解决方案，我们都可以直接去这里拿

​	Api网关，zuul组件

​	Feign-->HttpClient-->Http的通信方式，同步并阻塞

​	服务注册与发现，Eureka

​	熔断机制，Hystrix



2018年年底，Netflix宣布无限期停止维护。生态不在维护



2.Apache Dubbo zookeeper，第二套解决系统

​	API：没有！ 要么找第三方组件，要么自己实现

​	Dubbo是一个高性能的基于java实现的RPC通信框架！

​	服务注册与发现，zookeeper 动物园管理者（hadoop，hive）

​	没有熔断机制： 借助了Hystrix



不完善，Dubbo



3.SpringCloud Alibaba 一站式解决方案！

​	

目前，又提出了一种方案，

​	服务网格：下一代微服务标准，Server mesh

​	代表解决方案：istio
    

    
万变不离其宗，一通百通！
    1.API网关，服务路由
    2.http，RPC框架，异步调用
    3.服务注册与发现，高可用
    4.熔断机制，服务降级
    
为什么要解决这个问题？本质：网络是不可靠的！
    
    
```

