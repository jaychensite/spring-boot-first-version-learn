# spring-boot-first-version-learn

## 介绍
将 spring boot 项目克隆后，reset到了第一个版本，本工程即为那时的代码，提交时间是2013年4月。
依赖的 spring 版本是快照版spring，版本是 `spring 4.0.0.BOOTSTRAP-SNAPSHOT`
本工程对代码进行了注释，方便阅读；本工程会持续更新，如果fork到自己的仓库后，需要拉取我这边最新的注释，可以在自己仓库新增一个远端仓库（即我这边），然后拉取最新的注释，请自行搜索相关方法

如果下载依赖时遇到一些问题，请务必阅读以下说明。



另外，本工程在博客园已经更新了部分博文，博文中的demo代码即存储于本仓库的 all-demo-in-spring-learning 目录下。

博客园地址：

[曹工说Spring Boot源码（1）-- Bean Definition到底是什么，附spring思维导图分享](https://www.cnblogs.com/grey-wolf/p/12044199.html)

[曹工说Spring Boot源码（2）-- Bean Definition到底是什么，咱们对着接口，逐个方法讲解](https://www.cnblogs.com/grey-wolf/p/12051957.html )

[曹工说Spring Boot源码（3）-- 手动注册Bean Definition不比游戏好玩吗，我们来试一下](https://www.cnblogs.com/grey-wolf/p/12070377.html)

[曹工说Spring Boot源码（4）-- 我是怎么自定义ApplicationContext，从json文件读取bean definition的？](https://www.cnblogs.com/grey-wolf/p/12078673.html)

[曹工说Spring Boot源码（5）-- 怎么从properties文件读取bean](https://www.cnblogs.com/grey-wolf/p/12093929.html)

[曹工说Spring Boot源码（6）-- Spring怎么从xml文件里解析bean的](https://www.cnblogs.com/grey-wolf/p/12114604.html )

[曹工说Spring Boot源码（7）-- Spring解析xml文件，到底从中得到了什么（上）](https://www.cnblogs.com/grey-wolf/p/12151809.html)

[曹工说Spring Boot源码（8）-- Spring解析xml文件，到底从中得到了什么（util命名空间）](https://www.cnblogs.com/grey-wolf/p/12158935.html)

[曹工说Spring Boot源码（9）-- Spring解析xml文件，到底从中得到了什么（context命名空间上）](https://www.cnblogs.com/grey-wolf/p/12189842.html)

[曹工说Spring Boot源码（10）-- Spring解析xml文件，到底从中得到了什么（context:annotation-config 解析）](https://www.cnblogs.com/grey-wolf/p/12199334.html)

[曹工说Spring Boot源码（11）-- context:component-scan，你真的会用吗（这次来说说它的奇技淫巧）](https://www.cnblogs.com/grey-wolf/p/12203743.html)

[曹工说Spring Boot源码（12）-- Spring解析xml文件，到底从中得到了什么（context:component-scan完整解析)](https://www.cnblogs.com/grey-wolf/p/12214408.html)

[曹工说Spring Boot源码（13）-- AspectJ的运行时织入（Load-Time-Weaving），基本内容是讲清楚了（附源码）](https://www.cnblogs.com/grey-wolf/p/12228958.html)

[曹工说Spring Boot源码（14）-- AspectJ的Load-Time-Weaving的两种实现方式细细讲解，以及怎么和Spring Instrumentation集成](https://www.cnblogs.com/grey-wolf/p/12283544.html)

[曹工说Spring Boot源码（15）-- Spring从xml文件里到底得到了什么（context：load-time-weaver 完整解析）](https://www.cnblogs.com/grey-wolf/p/12288391.html)

[曹工说Spring Boot源码（16）-- Spring从xml文件里到底得到了什么（aop：config完整解析【上】）](https://www.cnblogs.com/grey-wolf/p/12314954.html)

[曹工说Spring Boot源码（17）-- Spring从xml文件里到底得到了什么（aop：config完整解析【中】）](https://www.cnblogs.com/grey-wolf/p/12317612.html)  

[曹工说Spring Boot源码（18）-- Spring AOP源码分析三部曲，终于快讲完了 (aop：config完整解析【下】)](https://www.cnblogs.com/grey-wolf/p/12322587.html)

[曹工说Spring Boot源码（19）-- Spring 带给我们的工具利器，创建代理不用愁（ProxyFactory）](https://www.cnblogs.com/grey-wolf/p/12359963.html)

[曹工说Spring Boot源码（20）-- 码网恢恢，疏而不漏，如何记录Spring RedisTemplate每次操作日志](https://www.cnblogs.com/grey-wolf/p/12375656.html)

[曹工说Spring Boot源码（21）-- 为了让大家理解Spring Aop利器ProxyFactory，我已经拼了](https://www.cnblogs.com/grey-wolf/p/12384356.html)  

[曹工说Spring Boot源码（22）-- 你说我Spring Aop依赖AspectJ，我依赖它什么了](https://www.cnblogs.com/grey-wolf/p/12418425.html)  

[曹工说Spring Boot源码（23）-- ASM又立功了，Spring原来是这么递归获取注解的元注解的](https://www.cnblogs.com/grey-wolf/p/12535152.html)

[曹工说Spring Boot源码（24）-- Spring注解扫描的瑞士军刀，asm技术实战（上）](https://www.cnblogs.com/grey-wolf/p/12571217.html)

[曹工说Spring Boot源码（25）-- Spring注解扫描的瑞士军刀，ASM + Java Instrumentation，顺便提提Jar包破解](https://www.cnblogs.com/grey-wolf/p/12584861.html)

[曹工说Spring Boot源码（26）-- 学习字节码也太难了，实在不能忍受了，写了个小小的字节码执行引擎](https://www.cnblogs.com/grey-wolf/p/12600097.html)

[曹工说Spring Boot源码（27）-- Spring的component-scan，光是include-filter属性的各种配置方式，就够玩半天了](https://www.cnblogs.com/grey-wolf/p/12601823.html)  

[曹工说Spring Boot源码（28）-- Spring的component-scan机制，让你自己来进行简单实现，怎么办](https://www.cnblogs.com/grey-wolf/p/12632419.html) 



## 使用步骤
1. 克隆代码

    ```shell
    git clone https://gitee.com/ckl111/spring-boot-first-version-learn.git
    ```

2. 找到自己的maven安装目录，切换到conf目录下，将自己原先的settings.xml保存备份起来，然后使用spring-boot-first-version-learn下的settings.xml。

    注意修改其中的本地仓库地址，设置为你自己需要的目录。

3. 有一个jar包，需要先安装到本地仓库：

    ```shell
    cd spring-boot-first-version-learn\springcore\src\main\resources\lib
    ```

    ```shell
    mvn install:install-file -Dfile=spring-core-4.0.0.BOOTSTRAP-SNAPSHOT-only-asm-cglib.jar -DgroupId=com.spring -DartifactId=spring-core-only-asm-cglib -Dversion=4.0.0 -Dpackaging=jar
    ```

4. 此时，在spring-boot-first-version-learn目录下，执行

    ```shell
    cd spring-boot-first-version-learn
    mvn -DskipTests=true compile
    ```

    基本上，执行以上几步后，即可成功编译该项目。

5. 导入ide，以idea为例

    直接导入以上maven项目即可，另外，请另行import 如下module：

    spring-boot-first-version-learn\all-demo-in-spring-learning\spring-aggregator

    建议都使用本聚合module进行compile。

    ​



## 问题1，阿里云镜像，导致snapshot类型的依赖下载不下来
如果遇到maven依赖下载不下来，尤其是snapshot类型的话，请检查是否使用了上述的maven settings.xml，如果不是，建议优先使用上述文件。

如果需要使用自己的settings.xml，另外，如果部分同学的settings.xml中，包含了如下配置：

```xml
<mirror> 
    <id>aliyun-maven</id> 
    <mirrorOf>*</mirrorOf> 
    <name>aliyun maven</name> 
    <url>http://maven.aliyun.com/nexus/content/groups/public</url> 
</mirror>

```
*上面的mirrorOf是对全部仓库进行镜像，但是我们下载snapshot类型的artifact是要到下面的spring自身的仓库下载的，
所以这里可以修改为如下配置，排除掉 spring-snapshots仓库*：
```xml
<mirror> 
    <id>aliyun-maven</id> 
    <mirrorOf>*,!spring-snapshots</miiroOf>  
    <name>aliyun maven</name> 
    <url>http://maven.aliyun.com/nexus/content/groups/public</url> 
</mirror>
```



 关于maven的mirror和repository配置，可以查阅:
 https://www.cnblogs.com/a-du/p/9645971.html



#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)