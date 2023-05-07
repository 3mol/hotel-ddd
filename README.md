# 酒店管理系统 基于DDD的设计和实现
## 项目简介
使用DDD相关的设计模式和思想，实现一个酒店管理系统，包括酒店、房间、客户、订单等模块，实现了酒店的预订、入住、退房等功能。

## 项目结构
```text
Adapter
    Controller
    Rpc
Application -> Client(无依赖的POJO)
    DomainService
    Scheduler
    Consumer
Domain
    DomainEntity
    Gateway
        RepositoryGateway(interface)
        RemoteApiGateway(interface)
Infra
    RemoteApiGatewayImpl
    RepositoryGatewayImpl
    
Common
    所有服务都通用的工具
依赖关系：
    Application -> Domain
    Application -> Infra 
    Adapter -> Application
    Domain <- Infra # Domain层不依赖Infra层
    
Infra 依赖JPA、Feign、其他服务的Client等组件，与外部系统交互
Domain 无依赖
Application 依赖Kafka、Spring-Boot等组件
Adapter 依赖MVC组件
Client 依赖Feign
```

```plantuml
namespace Adapter{}
namespace Application{}
namespace Domain{}
namespace Client{}
namespace Infra{}
    Application --> Domain
    Application --> Infra: 查询数据
    Application -> Client: 对外的client包、remote接口
    Adapter --> Application
    Domain <.- Infra: 实现接口 
```

待办事务
1. 微服务化
   - order
   - payment
   - room
   - hotel
   - user
   - report
2. 分布式事件处理方案
3. 分布式事务处理方案
4. CQRS, 命令处理器、查询处理器、读写分离
5. 聚合并发操作一致性问题处理方案
6. 简易网关
7. 简单的前端页面

### 部署
使用Docker进行打包，每个服务打包成一个镜像。为了执行完整的集成测试，可以启动所有服务后执行测试服务的脚本即可。

### 测试
单元测试

- 直接运行在对应的模块执行test命令即可
  `mvn -pl ddd-microservice-modules/hotel-service test`
  集成测试
- 先运行相关的所有服务，或者使用docker-compose运行所有相关的容器，然后执行集成测试服务的测试用例
  `mvn -pl ddd-microservice-modules/intergration-testing test`