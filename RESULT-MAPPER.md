# Result 格式可替換（Response Mapper）

## 目的

保留預設的 `Result<T>` 回應格式，同時允許專案替換為企業規格（例如不同欄位名、加上 correlationId、自訂錯誤碼結構）。

## 實作

- **介面**：`com.brainwave.core.common.ResultMapper`  
  - `success(T data)`、`success(String message, T data)`、`fail(String code, String message)`
- **預設實作**：`com.brainwave.backend.config.DefaultResultMapper`（`@Primary`）  
  - 行為與原本 `Result.success()` / `Result.fail()` 一致，並從 MDC 帶入 `requestId` 至 `Result.correlationId`（與 CorrelationIdFilter 銜接）
- **BaseController**：注入 `ResultMapper`（`required = false`），有注入時用 mapper 組裝回應，否則退回 `Result` 靜態方法

## 替換為自訂格式

1. 實作 `ResultMapper` 介面，回傳自訂 DTO（或 `Result` 子類、包裝類）。
2. 註冊為 Spring Bean，必要時加上 `@Primary`，或移除 / 降低 `DefaultResultMapper` 的 `@Primary`，讓自訂實作優先。

## 注意

- 前端與 OpenAPI 型別需與實際回傳結構一致；若改為自訂格式，需同步更新 API 規格與前端 `schema.d.ts` 或型別定義。
