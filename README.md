# 使用者登入與驗證系統

## 專案背景與目的

此專案為個人練習，以使用者系統作為練習實例，並進一步提升 Spring Boot 應用至 Spring Cloud 微服務架構。

專案整合了 MyBatis、Redis 等常用技術元件，同時引入 Consul 作為微服務中的 "服務註冊中心"，透過 Docker 部署單節點的 Consul 服務，實現服務的高效註冊與發現。

**目的**：
- 練習微服務應用的範例，學習和實踐後端開發技能。
- 體驗簡訊驗證碼登入流程及 Redis 令牌管理的實現。
- 學習微服務架構中的服務註冊與發現機制。
- 熟悉 Docker 化部署及 Spring Cloud 的基礎應用。

---

## 專案簡介

這是一個基於 Spring Boot 的使用者登入與驗證系統，包含以下功能：
- 獲取簡訊驗證碼
- 使用簡訊驗證碼登入
- 登出介面
- 簡訊驗證碼的數據管理
- Redis 數據緩存管理

用於學習和實踐 Java 後端開發的基本技能，包括 MyBatis 數據操作、Redis 使用，以及 Restful API 的構建。

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
    - **注意**：目前尚未實現真實簡訊發送邏輯，需呼叫簡訊平台介面。

**邏輯流程**：
1. 隨機生成六位數字驗證碼。
2. 封裝驗證碼資訊並存入 `user_sms_code` 資料表。
3. 返回操作成功結果。

---

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

**邏輯流程**：
1. 從 `user_sms_code` 表中查詢手機號的驗證碼並比對。
2. 若驗證碼正確，檢查用戶是否已存在。
    - 若用戶不存在，自動註冊新用戶。
    - 若用戶存在，更新登錄狀態與時間。
3. 生成 `accessToken` 並存入 Redis，有效期設置為 30 天。
4. 返回用戶 ID 和訪問令牌。

---

### 3. 登出介面
- **API 路徑**：`/user/loginExit`
- **方法**：POST
- **參數**：
    - `userId`：用戶 ID。
    - `accessToken`：用戶訪問令牌。
- **功能描述**：
    - 刪除 Redis 中的 `accessToken`，實現登出功能。

**邏輯流程**：
1. 根據 `accessToken`，刪除 Redis 中的會話資訊。
2. 返回登出結果，成功返回 `true`，失敗返回 `false`。

---

## 核心功能

### 簡訊驗證碼管理
- 驗證碼生成、存儲到數據庫並支持驗證功能。

### 用戶註冊與登入
- 支持自動用戶註冊與登錄狀態管理。

### 令牌管理
- 使用 Redis 儲存和驗證訪問令牌。

### 微服務架構
- 支持 Spring Cloud 和 Consul 作為註冊中心。

---

## 技術棧

### 後端技術
- **Spring Boot**：核心框架，用於構建後端應用。
- **Spring Cloud**：微服務框架，實現服務治理和架構升級。
- **Consul**：服務註冊與發現中心，管理微服務的動態路由。
- **MyBatis**：數據庫訪問框架，用於執行 SQL 查詢。
- **Redis**：內存型數據庫，用於管理訪問令牌和緩存數據。
- **MySQL**：關係型數據庫，存儲用戶和驗證碼數據。

### 工具
- **Postman**：測試 API。
- **Lombok**：簡化 Java 代碼（如自動生成 Getter/Setter）。
- **Docker**：專案容器化部署。

### Postman 測試集合
[下載 Postman 測試集合](./postman/spring_demo_collection.json)

