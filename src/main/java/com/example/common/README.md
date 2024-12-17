# 使用者登入與驗證系統

## 專案簡介

這是一個基於 Spring Boot 的使用者登入與驗證系統，包含以下功能：
- 獲取簡訊驗證碼
- 使用簡訊驗證碼登入
- 登出介面
- 簡訊驗證碼的數據管理
- Redis 數據緩存管理

此專案設計適合用於學習和實踐 Java 後端開發的基本技能，包括 MyBatis 數據操作、Redis 使用，以及 Restful API 的構建。

---

## 功能介紹

### 1. 獲取簡訊驗證碼
- **API 路徑**：`/user/getCode`
- **方法**：POST
- **參數**：
    - `reqId`：請求 ID，用於追蹤請求。
    - `mobileNo`：手機號碼，用於接收驗證碼。
- **功能描述**：
    - 隨機生成六位數驗證碼。
    - 驗證碼存儲於數據庫表 `user_sms_code` 中，並打印到日誌中。

### 2. 使用簡訊驗證碼登入
- **API 路徑**：`/user/loginByMobile`
- **方法**：POST
- **參數**：
    - `reqId`：請求 ID，用於追蹤請求。
    - `mobileNo`：手機號碼。
    - `smsCode`：簡訊驗證碼。
- **功能描述**：
    - 驗證輸入的手機號和驗證碼是否匹配。
    - 若手機號不存在，則自動註冊新用戶。
    - 生成 `accessToken` 並存儲於 Redis，保存時間為 30 天。

### 3. 登出介面
- **API 路徑**：`/user/loginExit`
- **方法**：POST
- **參數**：
    - `userId`：用戶 ID。
    - `accessToken`：用戶訪問令牌。
- **功能描述**：
    - 刪除 Redis 中的 `accessToken`。
    - 返回操作是否成功的布林值。

---

## 技術棧

### 後端技術
- **Spring Boot**：核心框架，用於構建後端應用。
- **MyBatis**：數據庫訪問框架，用於執行 SQL 查詢。
- **Redis**：內存型數據庫，用於管理訪問令牌和緩存數據。
- **MySQL**：關係型數據庫，存儲用戶和驗證碼數據。

### 工具
- **Postman**：測試 API。
- **Lombok**：簡化 Java 代碼（如自動生成 Getter/Setter）。

---

## 數據庫結構

### user_info 表
| 字段名        | 類型        | 描述             |
|---------------|-------------|------------------|
| id            | BIGINT      | 自增主鍵         |
| user_id       | VARCHAR(50) | 用戶 ID          |
| nick_name     | VARCHAR(50) | 用戶暱稱         |
| mobile_no     | VARCHAR(11) | 用戶手機號碼     |
| password      | VARCHAR(50) | 登錄密碼         |
| is_login      | INT         | 登錄狀態         |
| login_time    | TIMESTAMP   | 登錄時間         |
| is_del        | INT         | 是否刪除         |
| create_time   | TIMESTAMP   | 建立時間         |

### user_sms_code 表
| 字段名        | 類型        | 描述             |
|---------------|-------------|------------------|
| id            | BIGINT      | 自增主鍵         |
| mobile_no     | VARCHAR(11) | 用戶手機號碼     |
| sms_code      | VARCHAR(10) | 驗證碼           |
| send_time     | TIMESTAMP   | 驗證碼發送時間   |
| create_time   | TIMESTAMP   | 建立時間         |

---

## 部署步驟

1. 確保已安裝以下工具：
    - JDK 1.8 或更高版本
    - MySQL 數據庫
    - Redis 伺服器
    - Maven

2. 克隆專案到本地：
   ```bash
   git clone <你的 GitHub 倉庫地址>
   ```

3. 配置 `application.yml` 文件：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/<your_database_name>
       username: <your_username>
       password: <your_password>
     redis:
       host: 127.0.0.1
       port: 6379
       password: <your_redis_password>
   ```

4. 創建數據庫表：
    - 使用 `src/main/resources/sql` 中的建表語句。

5. 啟動應用：
   ```bash
   mvn spring-boot:run
   ```

6. 使用 Postman 測試 API。

---

## 作者
- **你的名字或暱稱**
- GitHub: [你的 GitHub 地址]

如有問題，請提交 Issue 或聯繫作者！
