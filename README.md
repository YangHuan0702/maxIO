# MaxIO 🚀

> High-performance, S3-compatible object storage server — inspired by MinIO, built for scale.
---

## 简介

**MaxIO** 是一个仿照 [MinIO](https://min.io) 实现的高性能对象存储服务，完全兼容 Amazon S3 API。MaxIO 专为私有云、混合云及边缘计算场景设计，支持海量非结构化数据的存储与管理。

- 🗄️ **S3 API 兼容** — 无缝对接现有 S3 客户端、SDK 和工具链
- ⚡ **高性能** — 针对大文件读写和高并发访问优化
- 🔒 **安全可靠** — 内置数据加密、访问控制与审计日志
- 🌐 **分布式架构** — 支持多节点集群部署与水平扩展
- 📦 **轻量部署** — 单一二进制文件，开箱即用

---

## 功能特性

| 功能 | 状态 | 说明 |
|------|------|------|
| Bucket 管理 | ✅ 已实现 | 创建、删除、列举 Bucket |
| Object 上传/下载 | ✅ 已实现 | PUT / GET / DELETE Object |
| 分片上传 (Multipart Upload) | ✅ 已实现 | 支持大文件分片传输 |
| 预签名 URL | ✅ 已实现 | 生成临时访问链接 |
| 访问控制 (ACL / IAM) | 🚧 开发中 | 细粒度权限管理 |
| 服务端加密 (SSE) | 🚧 开发中 | AES-256 / KMS 集成 |
| 数据版本控制 | 📋 计划中 | Object Versioning |
| 生命周期管理 | 📋 计划中 | 自动过期、转储策略 |
| 跨域资源共享 (CORS) | ✅ 已实现 | 灵活的 CORS 配置 |
| 纠删码 (Erasure Coding) | 🚧 开发中 | 数据冗余与容错 |

---

## 快速开始

### 环境要求

- [GraalVM 21](https://www.graalvm.org/downloads/)（推荐 GraalVM Community Edition 21+）
- Gradle 8.x（项目内置 Gradle Wrapper，无需单独安装）
- Linux / macOS / Windows
- 推荐内存：≥ 2GB

> **提示：** 使用 [SDKMAN](https://sdkman.io/) 可快速切换 GraalVM 版本：
> ```bash
> sdk install java 21.0.2-graalce
> sdk use java 21.0.2-graalce
> ```

### 安装

**从源码编译（JVM 模式）：**

```bash
git clone https://github.com/yourname/maxio.git
cd maxio
./gradlew build
```

**编译为原生可执行文件（Native Image）：**

```bash
# 需要提前安装 GraalVM native-image 组件
./gradlew nativeCompile
# 产物位于 build/native/nativeCompile/maxio
```

**使用 Docker：**

```bash
docker pull yourname/maxio:latest
docker run -p 9000:9000 -p 9001:9001 \
  -e MAXIO_ROOT_USER=admin \
  -e MAXIO_ROOT_PASSWORD=password123 \
  -v /data:/data \
  yourname/maxio:latest
```

### 启动服务

```bash
# JVM 模式运行
./gradlew run --args="server /data"

# 或使用编译产物直接运行
java -jar build/libs/maxio.jar server /data

# Native Image 模式（更低内存占用，更快启动）
./build/native/nativeCompile/maxio server /data

# 指定端口与监听地址
./maxio server --address 0.0.0.0:9000 --console-address 0.0.0.0:9001 /data

# 多磁盘模式（纠删码）
./maxio server /data/disk{1...4}
```

启动后访问：
- **API 端点：** `http://localhost:9000`
- **Web 控制台：** `http://localhost:9001`

默认凭据：`admin / password123`（生产环境请务必修改）

---

## 配置

MaxIO 支持通过**环境变量**或**配置文件**进行配置。

### 环境变量

```bash
MAXIO_ROOT_USER=admin           # 管理员用户名
MAXIO_ROOT_PASSWORD=secret      # 管理员密码
MAXIO_VOLUMES=/data             # 数据存储路径
MAXIO_SITE_REGION=us-east-1     # 数据中心区域
MAXIO_TLS_CERT_FILE=/path/cert  # TLS 证书路径
MAXIO_TLS_KEY_FILE=/path/key    # TLS 私钥路径
```

### 配置文件（`config.yaml`）

```yaml
server:
  address: "0.0.0.0:9000"
  console_address: "0.0.0.0:9001"
  region: "us-east-1"

storage:
  volumes:
    - /data/disk1
    - /data/disk2

auth:
  root_user: "admin"
  root_password: "your-secure-password"

tls:
  enabled: false
  cert_file: ""
  key_file: ""

log:
  level: "info"     # debug | info | warn | error
  format: "json"
```

---

## API 示例

MaxIO 完全兼容 S3 API，可直接使用 AWS SDK 或 `mc` 客户端操作。

### 使用 AWS CLI

```bash
# 配置端点
aws configure set default.s3.endpoint_url http://localhost:9000

# 创建 Bucket
aws s3 mb s3://my-bucket

# 上传文件
aws s3 cp ./file.txt s3://my-bucket/file.txt

# 列举对象
aws s3 ls s3://my-bucket/

# 下载文件
aws s3 cp s3://my-bucket/file.txt ./download.txt
```

### 使用 Java SDK（AWS SDK v2）

首先在 `build.gradle` 中添加依赖：

```groovy
dependencies {
    implementation platform('software.amazon.awssdk:bom:2.25.0')
    implementation 'software.amazon.awssdk:s3'
}
```

```java
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.net.URI;

public class MaxIOExample {
    public static void main(String[] args) {
        S3Client s3 = S3Client.builder()
            .endpointOverride(URI.create("http://localhost:9000"))
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create("admin", "password123")
            ))
            .forcePathStyle(true) // MaxIO 使用路径风格访问
            .build();

        // 创建 Bucket
        s3.createBucket(r -> r.bucket("my-bucket"));

        // 上传对象
        s3.putObject(
            r -> r.bucket("my-bucket").key("hello.txt"),
            RequestBody.fromString("Hello, MaxIO!")
        );

        // 列举对象
        s3.listObjectsV2(r -> r.bucket("my-bucket"))
          .contents()
          .forEach(obj -> System.out.println(obj.key()));
    }
}
```

---

## 项目结构

```
maxio/
├── src/
│   ├── main/
│   │   ├── java/com/maxio/
│   │   │   ├── MaxIOApplication.java   # 主程序入口
│   │   │   ├── api/                    # S3 API 路由与处理器
│   │   │   ├── auth/                   # 认证与授权
│   │   │   ├── storage/                # 存储引擎核心
│   │   │   ├── erasure/                # 纠删码实现
│   │   │   └── config/                 # 配置管理
│   │   └── resources/
│   │       └── application.yml         # 默认配置
│   └── test/
│       └── java/com/maxio/             # 单元 & 集成测试
├── build.gradle                        # Gradle 构建脚本
├── settings.gradle
├── gradle/
│   └── wrapper/                        # Gradle Wrapper
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 开发指南

```bash
# 克隆仓库
git clone https://github.com/yourname/maxio.git
cd maxio

# 编译项目
./gradlew build

# 运行所有测试
./gradlew test

# 运行集成测试
./gradlew integrationTest

# 本地启动（开发模式）
./gradlew run

# 编译 Native Image
./gradlew nativeCompile

# 代码检查（Checkstyle / SpotBugs）
./gradlew check
```

### 提交规范

本项目遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 新增分片上传支持
fix: 修复大文件 MD5 校验错误
docs: 更新 API 文档
refactor: 重构存储引擎模块
test: 添加 Multipart Upload 单元测试
```

---

## 与 MinIO 的异同

| 对比项 | MinIO | MaxIO |
|--------|-------|-------|
| S3 兼容性 | 完整 | 核心 API |
| 许可证 | AGPL-3.0 | Apache-2.0 |
| 语言 | Go | Java (GraalVM 21) |
| 构建工具 | Make | Gradle |
| Native Image | ✅ | ✅ (GraalVM) |
| 部署方式 | 单机 / 集群 | 单机 / 集群 |
| 纠删码 | ✅ | 🚧 开发中 |
| 商业支持 | ✅ | ❌ |
| 定制扩展 | 受限 | 完全开放 |

> MaxIO 是一个学习与实践项目，旨在深入理解对象存储系统的设计与实现。

---

## 路线图

- [ ] v0.1 — 基础 S3 API（Bucket + Object CRUD）
- [ ] v0.2 — 分片上传 + 预签名 URL
- [ ] v0.3 — 多用户 IAM 权限系统
- [ ] v0.4 — 纠删码存储引擎
- [ ] v0.5 — 多节点集群模式
- [ ] v1.0 — 生产可用正式版本

---

## 贡献

欢迎提交 Issue 和 Pull Request！请先阅读 [CONTRIBUTING.md](CONTRIBUTING.md)。

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feat/your-feature`
3. 提交改动：`git commit -m 'feat: add your feature'`
4. 推送分支：`git push origin feat/your-feature`
5. 发起 Pull Request

---

## 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源。

---

<p align="center">Made with ❤️ | Inspired by <a href="https://min.io">MinIO</a></p>