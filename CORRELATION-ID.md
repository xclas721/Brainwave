# Correlation ID / Request ID（P2-4）

## 用途

為每個 HTTP 請求提供唯一識別碼（X-Request-ID），方便：

- **日誌追蹤**：同一請求的 log 行都帶相同 ID，排查錯誤時可依 ID 過濾
- **前後端對應**：前端可帶入或記錄 response header 的 X-Request-ID，與後端日誌對齊

## 實作方式

- **Filter**：`CorrelationIdFilter`（`brainwave-backend`）
  - 讀取 request header `X-Request-ID`；若無則產生 UUID
  - 寫入 `MDC` 鍵 `requestId`，供 log pattern 使用
  - 將同一 ID 設為 request attribute `correlationId`，並回寫至 response header `X-Request-ID`
- **日誌**：`application.properties` 的 `logging.pattern.console` 已含 `[%X{requestId}]`，每行 log 會印出該請求 ID

## 使用方式

- **後端**：無需額外設定，每個請求都會自動帶上
- **前端 / 呼叫端**：可選在請求時帶入 `X-Request-ID`（例如自己產生 UUID），回應時後端會回傳同一 ID 或新產生的 ID，方便對應日誌

## 取得當前 Request ID（程式內）

若 Controller 或 Service 需要取得當前請求的 correlation ID：

```java
String correlationId = (String) request.getAttribute(CorrelationIdFilter.REQUEST_ATTR_KEY);
```

或從 response header 取得（在回應已送出後）。
