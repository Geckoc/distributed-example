### branch: spring_session

* git url: git@github.com:Geckoc/spring_session.git

--- 
##### description
about session share questions!

1. import dependent  servlet-api | jsp | jstl | spring-session | spring-web
2. config web.xml
   + springSessionRepositoryFilter
   + listener: ContextLoaderListener
   	    - context-param 
	    - load: classpath:applicationContext.xml

3. create applicationContext-session.xml
   + config RedisHttpSessionConfiguration class
 ```xml
<context:annotation-config/>：用于激活已经在Spring容器中注册的bean或者注解，因为
我们通过容器创建的bean中，底层有可能使用了其它的注解，我们通过<context:component-scan>就
不能指定具体的包了，所以可以使用<context:annotation-config/>激活
<!-- spring注解、bean的处理器 -->
<context:annotation-config/>

<!-- Spring session 的配置类 -->
<bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration"/>
```
---

4. config Spring-data-redis
```xml
<!-- 配置jedis连接工厂，用于连接redis -->
<bean id="jedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
    <property name="hostName" value="${redis.hostName}"/>
    <property name="port" value="${redis.port}"/>
    <property name="password" value="${redis.password}"/>
    <property name="usePool" value="${redis.usePool}"/>
    <property name="timeout" value="${redis.timeout}"/>
</bean>
<!--读取redis.properties属性配置文件-->
<context:property-placeholder location="classpath:redis.properties"/>
```
5. config redis.properties file
```properties
redis.hostName=192.168.235.128
redis.port=6379
redis.password=123456
redis.usePool=true
redis.timeout=15000
```

6. import applicationContext-session.xml in applicationContext
```xml
<import resource="applicationContext-session.xml"/> 

```

**finally click config that config file connected**





