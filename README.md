<div align="center">

# 门店通 StoreLink · 连锁门店管理系统（后端）

**Spring Boot 3 + MyBatis-Plus + Sa-Token 的前后端分离门店管理后端服务**

![JDK](https://img.shields.io/badge/JDK-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.5-brightgreen)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.15-red)
![Sa-Token](https://img.shields.io/badge/Sa--Token-1.39.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1)
![License](https://img.shields.io/badge/License-MIT-yellow)

</div>

---

## 一、项目简介

StoreLink 是一套面向连锁品牌的门店管理系统后端，覆盖 **门店档案、加盟审批、门店业绩、到店核销、库存调拨、销售协助** 六大业务子域，支持「总部 — 门店 — 员工」三级的多门店、多角色连锁经营场景，帮助总部统一管理门店运营与终端业绩。

服务提供 **40+ 个 RESTful 接口**，统一以 `/api/v1` 为前缀；鉴权走 Sa-Token 会话，权限控制到**接口级**（`@SaCheckPermission`）与**数据行级**（按登录身份自动收窄门店范围），并配套全局异常处理、参数校验、接口耗时统计与 Excel 导出能力。

> 本项目为**后端服务**，前端为独立仓库（Vue3 + Element Plus + Pinia + Axios）。

## 二、技术栈

| 分类 | 选型 |
| --- | --- |
| 语言 / 运行时 | JDK 21 |
| 框架 | Spring Boot 3.0.5、Spring MVC、Spring AOP |
| 持久层 | MyBatis-Plus 3.5.15（`LambdaQueryWrapper` + XML 复杂查询） |
| 数据库 | MySQL 8.0（InnoDB / utf8mb4），库名 `store_link` |
| 认证授权 | Sa-Token 1.39.0（会话鉴权 + `@SaCheckPermission` 权限点） |
| 参数校验 | Spring Boot Validation（`@Valid` + 全局异常统一处理） |
| 文档 | Knife4j / OpenAPI 3（`/doc.html`） |
| 导出 | 阿里 EasyExcel 3.3.2 |
| 其他 | Lombok、`@TableLogic` 逻辑删除、`@Version` 乐观锁、`MetaObjectHandler` 自动填充 |

## 三、整体架构

```
                       前端（Vue3 · Element Plus · Pinia · Axios）
                                      │  HTTP · /api/v1/**
                        ┌─────────────▼─────────────┐
                        │   Sa-Token 拦截器          │  全局登录校验（除 /auth/login）
                        │   StpUtil.checkLogin()     │
                        └─────────────┬─────────────┘
                                      │
                        ┌─────────────▼─────────────┐
                        │   Controller 层            │  @SaCheckPermission 接口级权限
                        │   Auth / Store / Dashboard │  参数校验 · 统一响应 Result<T>
                        │   SalesAssist / StoreAudit │
                        │   StorePerf / StoreVerify  │
                        │   TransferStock / User     │
                        └─────────────┬─────────────┘
                                      │
                        ┌─────────────▼─────────────┐
                        │   Service 层               │  业务逻辑 · 数据权限收窄
                        │   （含 TimeAspect 耗时切面）│  @Transactional
                        └─────────────┬─────────────┘
                                      │
                        ┌─────────────▼─────────────┐
                        │   Mapper 层                │  MyBatis-Plus
                        │   BaseMapper + StoreMapper.xml
                        └─────────────┬─────────────┘
                                      │
                                 ┌────▼────┐
                                 │  MySQL  │  store_link
                                 └─────────┘
```

## 四、功能模块

| 业务子域 | 控制器 | 主要能力 |
| --- | --- | --- |
| 认证 | `AuthController` | 登录、登出、获取当前用户信息（会话存 `roleCode` / `storeId`） |
| 首页看板 | `DashboardController` | 门店总数 / 营业门店数 / 业绩排名（**按身份自动收窄范围**） |
| **门店管理** | `StoreController` | 门店档案增删改查、条件分页、**Excel 导出** |
| 加盟审批 | `StoreAuditController` | 加盟申请列表、创建、审批状态流转 |
| 门店业绩 | `StorePerfController` | 业绩录入、列表、按门店查询 |
| 到店核销 | `StoreVerifyController` | 核销单创建与查询 |
| 库存调拨 | `TransferStockController` | 调拨单创建、查询、状态流转 |
| 销售协助 | `SalesAssistController` | 销售协助单管理 |
| 用户管理 | `UserController` | 用户增删改查、角色分配 |

**接口风格约定**：`GET /list` 分页查询 · `GET /{id}` 详情 · `POST /create` 新增 · `PUT /{id}` 修改 · `DELETE /{id}` 删除 · `GET /export` 导出。删除接口在**前端**配套弹窗二次确认。

## 五、角色与数据权限

### 5 个角色

| 角色编码 | 角色 | 可见范围 |
| --- | --- | --- |
| `ADMIN` | 管理员 | 全部门店 |
| `HQ` | 总部 | 全部门店 |
| `STORE_MANAGER` | 店长 | 本门店 |
| `GUIDE` | 导购 | 本门店 / 本人数据 |
| `SUPERVISOR` | 督导 | 跨门店巡查 |

### 两级权限控制

**① 接口级（功能权限）**：每个接口标注权限点，由 Sa-Token 校验，例如：

```java
@GetMapping("/list")
@SaCheckPermission("store:list")
public Result<PageResult<StoreVO>> list(StorePageDTO dto) { ... }
```

权限点按「模块:动作」组织（`store:list` / `store:create` / `store:update` / `store:delete` / `store:export` / `audit:*` / `perf:*` …），通过 `t_storelink_system_role_permission` 挂到角色上。

**② 数据行级（数据权限）**：判定依据**始终取自服务端会话**（`StpUtil.getSession()`），不信任前端传参。以首页看板为例：

```java
Object roleCode  = StpUtil.getSession().get("roleCode");
Object storeId   = StpUtil.getSession().get("storeId");
boolean isAdminOrHQ = "ADMIN".equals(roleCode) || "HQ".equals(roleCode);

// 非总部角色：无论前端传什么，查询条件都被强制加上本门店
if (!isAdminOrHQ && currentStoreId != null) {
    wrapper.eq(Store::getStoreId, currentStoreId);
}
```

**因此直接改 URL 参数或请求体也无法越权访问别家门店的数据**——前端传的门店 ID 只用于在已授权范围内进一步筛选，不用于判断归属。

## 六、数据库设计

### 表清单（10 张，统一 `t_storelink_` 前缀）

| 表 | 说明 |
| --- | --- |
| `t_storelink_store` | 门店档案 |
| `t_storelink_system_user` | 用户 |
| `t_storelink_system_role` | 角色 |
| `t_storelink_system_role_permission` | 角色—权限点关联 |
| `t_storelink_system_user_role` | 用户—角色关联 |
| `t_storelink_storeaudit` | 加盟审批 |
| `t_storelink_storeperf` | 门店业绩 |
| `t_storelink_storeverify` | 到店核销 |
| `t_storelink_transferstock` | 库存调拨 |
| `t_storelink_salesassist` | 销售协助 |

### 门店表字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `store_id` | bigint | 主键，自增 |
| `name` | varchar | 门店名称 |
| `city` | varchar | 所在城市 |
| `status` | varchar | 门店状态（`active` 营业中 / 其余为筹建、停业、关闭） |
| `created_at` | datetime | 创建时间（**自动填充**） |
| `updated_at` | datetime | 更新时间（**自动填充**） |
| `deleted` | tinyint | 逻辑删除：`0` 正常 / `1` 已删（`@TableLogic`） |
| `version` | bigint | 乐观锁版本号（`@Version`） |

### 两个通用约定

**① 逻辑删除**：MyBatis-Plus 全局配置 `logic-delete-field: deleted`，`delete` 语句自动改写为 `UPDATE ... SET deleted = 1`，所有查询自动追加 `deleted = 0`，业务代码无需关心。

**② 乐观锁**：门店等主数据带 `version` 字段，并发更新时由 MyBatis-Plus 自动加 `WHERE version = ?` 并递增——**两个管理员同时改同一家门店时，后提交的那个会失败而不是覆盖**。

## 七、公共能力与统一约定

| 能力 | 实现 | 说明 |
| --- | --- | --- |
| 统一响应 | `Result<T>` | 所有接口返回 `{code, msg, data}` 结构 |
| 全局异常 | `GlobalExceptionHandler` | 分别处理 `BusinessException`、`NotLoginException`（401）、`NotPermissionException`（403）、`MethodArgumentNotValidException`（参数校验失败）、兜底 `Exception`——**不让原始堆栈泄漏到前端** |
| 参数校验 | `@Valid` + DTO 注解 | 校验失败由全局异常处理器统一转成可读提示 |
| 智能填充 | `MyMetaObjectHandler` | `created_at` 插入时填、`updated_at` 插入与更新时填，业务代码不手写时间 |
| 耗时统计 | `TimeAspect` | 环绕 `com.ojbk.controller..*`，记录类名、方法名、入参与耗时；异常时记录错误日志并原样抛出 |
| 会话隔离 | `SaTokenConfig` | 全局拦截 `/**`，仅放行登录、接口文档与静态资源 |

## 八、本地运行

### 环境要求

| 组件 | 版本 / 端口 |
| --- | --- |
| JDK | 21 |
| Maven | 3.9+ |
| MySQL | 8.0，端口 3306，库名 `store_link` |

### 启动步骤

1. **建库导表**：创建数据库 `store_link`，执行建表与初始化脚本（表结构见第七节）；
2. **改数据源**：按本机情况修改 `src/main/resources/application.yaml` 中的 `spring.datasource`（默认 `localhost:3306/store_link`）；
3. **启动**：

```bash
mvn spring-boot:run
```

服务端口 **9090**。接口文档：`http://localhost:9090/doc.html`。

### 本地快速验证

```bash
# 登录，拿到 satoken（返回头里）
curl -X POST http://localhost:9090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 带上令牌查询门店列表
curl http://localhost:9090/api/v1/store/list \
  -H "satoken: <上一步拿到的 token>"
```

## 九、工程结构

```
StoreLink
├── src/main/java/com/ojbk
│   ├── common
│   │   ├── aspect        TimeAspect          接口耗时统计切面
│   │   ├── config        SaTokenConfig       Sa-Token 拦截器与放行规则
│   │   │                 MybatisPlusConfig   分页插件、乐观锁插件
│   │   ├── exception     BusinessException   业务异常
│   │   └── handler       GlobalExceptionHandler  全局异常处理
│   │                     MyMetaObjectHandler     时间字段自动填充
│   ├── controller        9 个控制器，40+ 接口
│   ├── service / Impl    业务逻辑（含数据权限收窄）
│   ├── mapper            Mapper 接口（StoreMapper.xml 承载复杂查询）
│   ├── entity            10 个实体，与表一一对应
│   ├── dto               入参对象（含分页与校验注解）
│   └── vo                出参对象（含 Excel 导出模型）
├── src/main/resources
│   ├── application.yaml  数据源、MyBatis-Plus、Sa-Token 配置
│   └── mapper/StoreMapper.xml
└── pom.xml
```

## 十、已知限制与后续规划

诚实记录当前方案的边界：

| 项 | 现状 | 规划 |
| --- | --- | --- |
| 会话存储 | Sa-Token 内存会话，**仅适用于单实例部署** | 多实例需切换为 Redis 集中存储 |
| 幂等控制 | 依赖数据库约束与应用层校验 | 引入幂等表 / 请求令牌，覆盖无唯一键的场景 |
| 数据权限 | 按门店维度收窄，逻辑写在 Service 内 | 抽成注解 + 切面，避免业务代码重复 |
| 数据库账号 | 配置文件中为本地开发默认值 | 改为环境变量注入，生产使用最小权限账号 |
| 接口文档 | Knife4j 已接入 | 补充统一错误码说明 |

## 十一、致谢

项目基于 Spring Boot 3 与 MyBatis-Plus 生态构建，鉴权采用 [Sa-Token](https://sa-token.cc/)。
