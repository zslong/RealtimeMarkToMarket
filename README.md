# 简介
**RealTimeMarkToMarketApplication** 系统可用来获取场外期权合约及场内期货对冲合约的实时盯市指标，支持将指标查询结果按照
指定的一个或多个维度聚合显示。聚合维度共有四个，从高到低依次为`投资组合->品种->标的->产品名称`。

系统启动后，可通过在浏览器中输入 http://localhost:8800/m2m/indicators 查询盯视指标，该查询会按照全部四个维度进行聚合显示。
也可通过在url尾部添加请求参数来指定按哪些维度显示，如 http://localhost:8800/m2m/indicators?aggregate=pf;cg 返回按照投资
组合与产品名称聚合的结果。四个维度的参数缩写分别为：
* **pf:** 投资组合
* **cg:** 品种
* **ul:** 标的
* **pg:** 产品名称

多维度查询时可将维度缩写通过`;`拼接。维度参数顺序可任意，但最终显示会按照维度的高低顺序进行聚合显示
# 项目结构
项目采用`SpringBoot+Gradle`开发搭建。数据库选择`H2`内存数据库，因为其轻量并且方便修改测试等特性。
## 代码结构
该项目采用常见的MVC结构。大体分为控制层、服务层、数据层，分别对应的package为`controller`、`service`、
`repository`。另外两个package`dao`与`dto` 分别为表示数据库模型的对象包以及表示服务层模型的对象包。

项目的入口类为`RealTimeMarkToMarketApplication.java`
## 数据库表结构
共有六张表，关系如下。其中`PORTFOLIO_CONTRACT`表为连接表

`UNDERLIER_CATEGORY` 1---N `UNDERLIER` 1---N `OTC_OPTION_CONTRACT` N-`PORTFOLIO_CONTRACT`-N `OTC_OPTION_PORTFOLIO`

`UNDERLIER` 1---N `HEDGE_POSITION`

## 盯视指标查询逻辑设计
1.  `MarkToMarketContoller`收到 **m2m/indicators** 请求并构造 `dto.AggregateQuery`，传递给`MarkToMarketService.getAggregateMarkToMarketIndicators`
2.  `MarkToMarketService`调用repository层函数，获取数据库中的存续场外合约列表及场内对冲期货持仓列表，然后将二者合并转换到一个`dto.Contract`列表
3.  `MarkToMarketService`将Contract列表传递给`PricerService.pricer`计算出mock指标
4.  `MarkToMarketService`调用repository层函数，获取数据库中投资组合列表并转化为`dto.Portfolio`列表
5.  `MarkToMarketService`调用私有aggregate方法，其递归地将`Contract`列表与`Porfolio`列表转化为`dto.Dimension`的树形结构并返回给Controller层，最终返回给客户
该逻辑的单元测试代码位于`test`目录下的`MarkToMarketServiceTest.java`
## 项目配置
配置文件位于resources目录下的`application.yml`。另外该目录下的`data.sql`与`schema.sql`分别为H2数据库的数据生成文件及数据库表结构创建文件*

# 编译及运行
## 编译
该项目可编译为jar文件。在项目根目录下运行如下命令

`./gradlew bootJar`

生成的Jar文件位于项目根目录下的`build/libs`目录。
## 运行
编译得到的Jar包可以通过如下命令运行

`java -jar <generated jar file>`

或者在有源代码的情况下，在根目录下运行如下命令直接启动程序

`./gradlew bootRun`

# 每五秒自动刷新盯视指标方案
考虑前后端通信使用websocket替换HTTP来实现定时刷新
1. 服务端维护一个map，key为客户端websocket session，value为该客户端选择的聚合查询维度
2. 当客户修改聚合维度时，该消息传给服务端。服务端更新map中对应session的value
3. 服务端启动一个定时任务。该任务每五秒运行一次。定时任务的实现可使用Quartz，或者场景简单可使用Java语言本身的ScheduledThreadPool。
定时任务的逻辑如下
   1) 首先获得所有的场外和场内合约列表
   2) 遍历 session map中的客户端session，然后根据对应的聚合维度生成Dimension tree并返回给客户
   

