# Backend CI/CD

## 📋 概述

後端專案的 GitHub Actions CI/CD 配置。

## 🔄 CI 流程

### 觸發條件
- Push 到 `main` 或 `develop` 分支
- 建立 Pull Request 到 `main` 或 `develop` 分支

### 執行步驟
1. ✅ 檢查程式碼格式（Spotless）
2. ✅ 執行測試
3. ✅ 建置專案

## 🚀 CD 流程

### 觸發條件
- 推送 tag（格式：`v*`，例如 `v1.0.0`）

### 部署步驟
1. 建置 JAR 檔案
2. 上傳建置產物（Artifacts）

## 📝 使用方式

### 本地測試 CI
```bash
mvn spotless:check -DskipTests -Pspotless
mvn test
mvn clean install -DskipTests
```

### 觸發部署
```bash
git tag v1.0.0
git push origin v1.0.0
```
