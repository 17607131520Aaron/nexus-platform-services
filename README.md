# nexus-platform-services

Java 21 + Spring Boot 3 的传统三层架构单体服务骨架，内置：
- MySQL 支持（Spring Data JPA）
- 多环境配置：`local` / `test` / `staging` / `prod`
- 全局统一响应
- 全局拦截器（TraceId 注入 + 请求日志）
- 全局异常处理
- JWT Token 鉴权（Bearer Token）

## 分层结构
- `controller`：接口层
- `service`：业务层
- `repository`：数据访问层
- `entity`：实体层

## 启动方式
### 下载依赖
```bash
mvn clean install -DskipTests
```

只下载依赖不编译可用：
```bash
mvn dependency:go-offline
```

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

也可以用 JVM 参数指定环境：
```bash
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=local
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=test
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=staging
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

或者打包后启动：
```bash
mvn clean package
java -jar target/nexus-platform-services-1.0.0-SNAPSHOT.jar --spring.profiles.active=local
```

### 启动前环境变量（推荐）
```bash
export JWT_SECRET='replace-with-your-32+chars-secret'
```

## 示例接口
- `POST /api/auth/token`（免鉴权，签发 token）
- `POST /api/users`
- `GET /api/users/{id}`

### 获取 token 请求体
```json
{
  "username": "alice"
}
```

### 获取 token 响应示例
```json
{
  "code": 0,
  "data": {
    "token": "xxxxx.yyyyy.zzzzz",
    "tokenType": "Bearer",
    "expiresIn": 7200
  },
  "message": "成功"
}
```

### 受保护接口请求头
```http
Authorization: Bearer xxxxx.yyyyy.zzzzz
```

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
  "code": 0,
  "data": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com"
  },
  "message": "成功"
}
```
