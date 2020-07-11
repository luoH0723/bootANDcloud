# SpringCloud

## 1.导论

- javaSE

- 数据库

- 前端
- Servlet
- Http
- Mybatis
- Spring
- SpringMVC
- SpringBoot
- Dubbo、Zookeeper、分布式基础
- maven、git
- Ajax、json
- ...../.



需要学习的东西

```java
三层架构+MVC
    
框架：
    Spring IOC AOP
    
    SpringBoot，新一代的JavaEE开发标准，自动装配
    
    模块化~   all in one
    
    模块化的开发====all in one，代码没变化~
    
微服务架构4个核心问题？
    1.服务很多，客户端如何访问；
    2.这么多服务，服务之间如何通信？
    3.这么多服务，如何治理？
    4.服务挂了怎么办？
    
    
解决方案：
    springcloud 生态    springboot
    
```



服务器集群从分布式文件系统（xxxDFS）拿文件



用户->CDN->Lvs->Nginx->服务器集群（tomcat）{<-->分布式文件系统}->注册中心（获取服务）{服务提供者（在注册中心注册）}



MQ：消息中间件    队列   异步



缓存{（Redis集群）（ElasticSearch搜索）}



mysql水平拆分/扩展（读）  垂直扩展（写）  读写分离 {（MyCat同步，保证数据一致性）}



|              | Dubbo         | Spring                       |
| ------------ | ------------- | ---------------------------- |
| 服务注册中心 | Zookeeper     | Spring Cloud Netflix Eureka  |
| 服务调用方式 | RPC           | REST API                     |
| 服务监控     | Dubbo-monitor | Spring Boot Admin            |
| 断路器       | 不完善        | Spring Cloud Netflix Hystrix |
| 服务网关     | 无            | Spring Cloud Netflix Zuul    |
| 分布式配置   | 无            | Spring Cloud Config          |
| 服务跟踪     | 无            | Spring Cloud Sleuth          |
| 消息总线     | 无            | Spring Cloud Bus             |
| 数据流       | 无            | Spring Cloud Stream          |
| 批量任务     | 无            | Spring Cloud Task            |

两者最大的区别就在于RPC通信和HTTP的REST方式

## 2.Eureka快速入门

### 2.1创建EurekaServer

```Java
1、创建一个父工程，并且在父工程中指定springcloud的版本，并且将packaing修改为pom

 <packaging>pom</packaging>

<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


2、创建eureka的server，创建springboot工程，并导入依赖，在启动类中添加注解，编写yml文件
导入依赖
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    
添加注解
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}

编写yml配置文件
server:
  port: 8761  #端口号

eureka:
  instance:
    hostname: localhost  #localhost
  client:
    #当前的eureka服务是单机版
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

```

### 2.2创建EurekaClient

```java
1、创建maven工程，修改为springboot
2、导入依赖
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
3、在启动类上添加注解
@SpringBootApplication
@EnableEurekaClient
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }
}
4、编写配置文件
#指定Eureka服务地址
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka


#指定服务的名称
spring:
  application:
    name: CUSTOMER

```

![image-20200701105045738](C:\Users\lh\AppData\Roaming\Typora\typora-user-images\image-20200701105045738.png)

### 2.3测试Eureka

```java
1、创建了yigesearch搜索模块，并且注册到Eureka
2、使用EurekaClient的对象去获取服务信息
@Autowired
    private EurekaClient eurekaClient;
    
3、正常RestTemplate调用即可
@GetMapping("/customer")
    public String customer(){
        //1.通过eurekaClient获取到SEARCH服务的信息
        InstanceInfo info = eurekaClient.getNextServerFromEureka("SEARCH",false);
        //2.获取到访问的地址
        String url = info.getHomePageUrl();
        System.out.println(url);
        //3.通过restTemplate访问
        String result = restTemplate.getForObject(url + "/search", String.class);
        //4.返回
        return result;
    }
```

### 2.4Eureka安全性

```java
实现Eureka认证
1、导入依赖
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
2、编写配置类
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //忽略掉/eureka/**
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}

3、编写配置文件
#指定用户名和密码
spring:
  security:
    user:
      name: root
      password: root

4、其他服务想注册到Eureka需要添加用户名和密码
#指定Eureka服务地址
eureka:
  client:
    service-url:
      defaultZone: http://用户名:密码@localhost:8761/eureka
```

### 2.5Eureka高可用

```
如果程序正在运行，突然Eureka宕机了
情况1：
调用方已经获取过服务，在缓存中存储了目标服务的信息，没有影响
情况2：
调用方未获取过服务，当前功能不可用
```

```java
搭建Eureka高可用
1、准备多台Eureka
采用了复制的方式，删除iml和target文件，并且修改pom.xml中的项目名称，再给父工程添加一个module
2、让服务注册到多台Eureka
#指定Eureka服务地址
eureka:
  client:
    service-url:
      defaultZone: http://root:root@localhost:8761/eureka,http://root:root@localhost:8762/eureka
3、让多台Eureka之间相互通讯
eureka:
  instance:
    hostname: localhost  #localhost
  client:
    registerWithEureka: true  #注册到Eureka上
    fetchRegistry: true #从Eureka上拉取信息
    serviceUrl:
      defaultZone: http://root:root@localhost:8762/eureka
```

### 2.6Eureka的细节

```yml
1、EurekaClient启动时，将自己的信息注册到EurekaServer上，EureKaServer就会存储上EurekaClient的信息
2、当EurekaClient调用服务时，本地没有注册信息的缓存时，去EurekaServer中去获取信息
3、EurekaClient会通过心跳的方式去和EurekaServer进行连接。（默认30sEurekaClient会发送一次心跳请求，如果超过了90s还没有发送心跳信息的话，EurekaServer就认为你宕机了，将当前EurekaClient从注册表中移除）

eureka
  instance:
    lease-renewal-interval-in-seconds: 30	#心跳间隔
    lease-expiration-duration-in-seconds: 90	#多久没有发送，认为你宕机了
    
4、EurekaClient会每隔30s去EurekaServer中去更新本地的注册表
#指定Eureka服务地址
eureka:
  client:
    registry-fetch-interval-seconds: 30	#每隔多久去更新一下本地的注册表缓存信息

5、Eureka的自我保护机制，统计15分钟内，如果一个服务的心跳发送比例低于85%，EurekaServer就会开启自我保护机制
	1.不会从EurekaServer中移除长时间没有收到心跳的服务。
	2.EurekaServer还是可以正常提供服务的。
	3.网络比较稳定时，EurekaServer才会开始将自己的信息被其他节点同步过去
	eureka:
  		server:
    		enable-self-preservation: true  #自我保护机制

6、CAP定理，C-一致性，A-可用性，P-分区容错性，这三个特性在分布式环境下，只能满足2个，而且分区容错性在分布式环境下，是必须要满足的，只能在AC之间进行权衡。

如果选择CP，保证一致性，可能会造成你系统在一定时间内你是不可用的，如果你同步数据的时间比较长，造成损失大。

Eureka就是一个AP的效果，高可用的集群，Eureka集群是无中心的，Eureka即便宕机几个也不会影响系统的使用，不需要重新的去推举一个master，也会导致一定时间内你数据是不一致的
```

## 3.Ribbon

### 3.1引言

```Java
nginx实现客户端和服务的负载均衡

Ribbon实现服务与服务之间的负载均衡

Ribbon是客户端负载均衡:customer客户模块，将2个Serrch模块信息全部拉取到本地的缓存，在customer中自己做一个负载均衡的策略，选中某一个服务

Dubbo是服务端负载均衡：在注册中心中，直接根据你指定的负载均衡策略，帮你选中一个指定的服务信息，并返回
```

### 3.2Ribbon快速入门

```java
1.启动search模块
2.在customer导入Robbin依赖
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
3.配置整合RestTemplate和Ribbon
@SpringBootApplication
@EnableEurekaClient
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}


4.在customer中去访问search（轮询）
    
    @RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/customer")
    public String customer(){
        String result = restTemplate.getForObject("http://SEARCH/search",String.class);
        //4.返回
        return result;
    }
}
```

### 3.3Ribbon配置负载均衡

```java
1.负载均衡策略
	1.RandomRule：随机策略
	2.RoundRobbinRule：轮询策略
	3.WeightedResponseTimeRule：默认会采用轮询的策略，后悔会根据服务的响应时间，自动给你分配权重
	4.BestAvailableRule：根据被调用方并发数最小的去分配
2.采用注解的形式
@Bean
    public IRule ribbonRule(){
        return new RandomRule();
    }
    
3.配置文件去指定负载均衡的策略（单个服务的负载均衡，推荐）
    #ribbon负载均衡策略  指定具体服务的负载均衡策略
SEARCH:        #编写服务名称
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule
```

## 4.服务间的调用-Feign

### 4.1引言

```
Feign可以帮助我们实现面向接口编程，就直接调用其他的服务，简化开发
```

### 4.2Feign的快速入门

```java
1.导入依赖
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
2.添加一个注解
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }

3.创建一个接口，并且和search模块做映射
@FeignClient("SEARCH")  //指定服务名称
public interface SearchClient {

    //value->目标服务的请求路劲，method->映射请求方式
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    String search();

}

4.测试使用
 @Autowired
    private SearchClient searchClient;

    @GetMapping("/customer")
    public String customer(){

        String result = searchClient.search();
        //4.返回
        return result;
    }
```

### 4.3Feign的传递参数方式

```Java
1.注意事项
	1.如果你传递的参数比较复杂时，默认会采用POST的请求方式。
	2.传递单个参数时，推荐使用@PathVariable，如果传递的单个参数比较多，这里也可以采用@RequestParam，不要省略Value属性
	3.传递对象信息时，统一采用json的方式，添加@RequestBody
    
2.在Search模块下准备三个接口
```

