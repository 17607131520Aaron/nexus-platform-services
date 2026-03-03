# nexus-platform-services

Java 21 + Spring Boot 3 的传统三层架构单体服务骨架，内置：
- MySQL 支持（Spring Data JPA）
- 多环境配置：`local` / `test` / `staging` / `prod`
- 全局统一响应
- 全局拦截器（TraceId 注入 + 请求日志）
- 全局异常处理

## 分层结构
- `controller`：接口层
- `service`：业务层
- `repository`：数据访问层
- `entity`：实体层

## 启动方式
默认 `local` 环境：
```bash
mvn spring-boot:run
```

指定环境启动：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=staging
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

或者打包后启动：
```bash
mvn clean package
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=local
```

## 示例接口
- `POST /api/users`
- `GET /api/users/{id}`

### POST 请求体
```json
{
  "username": "alice",
  "email": "alice@example.com"
}
```

### 统一响应示例
```json
{
  "code": "0",
  "messageKey": "success",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com"
  },
  "timestamp": 1700000000000,
  "traceId": "2f2b5a82c3f64f4f8f80ee8c11f3f9c4"
}
```
