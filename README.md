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
