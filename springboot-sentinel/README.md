# SpringCloud alibaba Sentinel
# 流量控制&熔断降级产品对比

|| Sentinel | Hystrix | Resilience4j |
| -------- | -------- | ------- | ------------ |
|隔离策略| 信号量隔离（并发线程数隔离） | 线程池隔离/信号量隔离 | 信号量隔离 |
|熔断降级策略| 基于响应时间、异常比率、异常数 | 基于异常比率 | 基于异常比率、响应时间 |
|实时统计实现| 滑动窗口 | 滑动窗口 | Ring Bit Bufffer |
|动态规则配置| 支持多种数据源 | 支持多种数据源 | 有限支持 |
|扩展性| 多个扩展点 | 插件的形式 | 接口的形式 |
|基于注解的支持| 支持 | 支持 | 支持 |
|限流| 基于QPS，支持基于调用关系的限流 | 有限的支持 | Rate Limiter |
|流量整形| 支持预热模式、匀速器模式、预热派对模式 | 不支持 | 简单的Rate Limiter |
|系统自适应保护| 支持 | 不支持 | 不支持 |
|控制台| 提供开箱即用的控制台、可配置规则、查看秒级监控、机器发现 | 简单的监控查看 |

# Sentinel 介绍

## Sentinel 概述

Sentinel是阿里巴巴出品的面向分布式服务架构的轻量级流量控制组件，主要以流量为切入点，从限流、流量整形、熔断降级、系统负载保护等多个维度来保障微服务的稳定性

## Sentinel 组成

* 核心库：主要是指Java客户端，不依赖任何框架、库，能够运行与java7及以上的版本运行时环境，同时对Dubbo、Spring Cloud等框架也有较好的支持
* 控制台：控制台主要负责管理推送规则、监控、集群限流分配管理、机器发现等

## Sentinel 相关概念

### 资源

资源是Sentinel的关键概念。它可以是Java应用程序中的任何内容，例如，由应用程序提供的服务，或由应用程序调用的其它应用提供的服务，甚至可以是一段代码。只要通过Sentinel API定义的代码，就是资源、能够被Sentinel 保护起来。大部分情况下，可以使用方法签名，URL，甚至服务名称作为资源名来标示资源

### 规则

规则指的是围绕资源的实时状态设定的规则，可以包括流量控制规则、熔断降级规则以及系统保护规则。所有规则可以动态实时调整

## Sentinel 优势

* 友好的控制面板
* 支持实时监控
* 支持多种限流、支持QPS限流，线程数限流以及多种限流策略
* 支持多种降级模式、支持按平均返回时间降级，按多种异常数降级、按异常比率降级等
* 方便扩展开发，支持SPI模式对chain进行扩展
* 支持链路的关联、可以实现按照链路统计限流，系统保护，热门资源保护等



# SpringBoot+Sentinel

### SpringBoot 环境信息

* SpringBoot 2.1.4.RELEASE
* JDK8
* Sentinel 1.7.2
* pom加入sentinel依赖

```java
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-core</artifactId>
    <version>1.7.2</version>
</dependency>
```

### Sentinel 控制台启动

* 下载地址：https://github.com/alibaba/Sentinel/releases

* 启动

  ```text
  java -Dserver.port=8850 -jar sentinel-dashboard-1.8.6.jar
  ```

* 浏览器访问 localhost:8050 账号密码默认sentinel (Sentinel是懒加载，需要先访问几次接口才会显示详情)



### SpringBoot应用与Sentinel控制台绑定

* pom加入依赖

  ```text
  		<dependency>
  			<groupId>com.alibaba.csp</groupId>
  			<artifactId>sentinel-transport-simple-http</artifactId>
  			<version>1.7.2</version>
  		</dependency>
  ```

* JVM启动时添加参数

  ```text
  -Dcsp.sentinel.dashboard.server=localhost:8850 -Dproject.name=springboot-sentinel
  ```



### 定义资源的方式

```java
/**
  * 定义资源的方式
  * @return
  */
    @GetMapping(value = "/hello")
    public String hello(){
        try(Entry hello = SphU.entry("hello");) {
            return "hello";
        } catch (BlockException e) {
            return "系统繁忙,请稍后";
        }
    }
```



### 注解方式调用支持

* 添加注解支持的依赖

  ```text
  <dependency>
      <groupId>com.alibaba.csp</groupId>
      <artifactId>sentinel-annotation-aspectj</artifactId>
      <version>1.7.2</version>
  </dependency>
  ```

  

* 创建Aspect配置类

  ```java
  @Configuration
  public class AspectConfig {
      @Bean
      public SentinelResourceAspect sentinelResourceAspect(){
          return new SentinelResourceAspect();
      }
  }
  ```

  

* 创建限流访问资源代码

  ```java
  /**
   * 注解方式
   * @return
   */
      @SentinelResource(value = "Sentinel_Annotation", blockHandler = "exceptionHandler", fallback = "fallback")
      @GetMapping("/annotation")
      public String annotation() {
          return "Hello Sentinel";
      }
  
  /**
   * 熔断方法
   * @return 内容
   */
      public String fallback(){
          return "访问频率过快";
      }
  ```
  
  

### 主流框架的默认适配

为了减少开发的复杂程度，对大部分的主流框架，例如Web Servlet，Dubbo、Spring Cloud、gRPC、Spring WebFlx、Reactor等都做了适配，只需要引入对应的依赖就可以方便的整合Sentinel
