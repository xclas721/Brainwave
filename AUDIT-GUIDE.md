# Audit Log 使用說明（P1-2）

> 目標：用最小成本先有「誰在打哪個 API、做了什麼、成功或失敗、花多久」的稽核軌跡。

## 1. 啟用方式

- 設定檔：`app.audit.*`
  - `app.audit.enabled`：是否啟用稽核（預設 false）
  - **`app.audit.sink`**：輸出目標，`console`（僅 log，預設）或 `db`（寫入 audit_log 表）
  - `app.audit.include-request-body`：暫不使用，預留未來記錄 body 用
  - `app.audit.include-response-body`：暫不使用，預留未來記錄 body 用
  - `app.audit.max-body-length`：未來記錄 body 時的長度上限
  - `app.audit.mask-fields`：未來要遮罩的欄位（目前只定義，不實際使用）

在本地要啟用稽核（僅 log），在環境或 `application-local.properties` 設定：

```properties
app.audit.enabled=true
```

要改為**寫入 DB**（合規／查詢用），再加上：

```properties
app.audit.enabled=true
app.audit.sink=db
```

需有 `audit_log` 表（Flyway  migration `V2__audit_log.sql`）。啟用後，凡標記了 `@Audit` 的方法會依 sink 輸出至 log 或 DB。

## 2. 稽核註解 `@Audit`

位置：`brainwave-core/src/main/java/com/brainwave/core/audit/Audit.java`

用法（加在 Controller 或 Service 方法上）：

```java
@Audit(action = "CREATE_USER", resource = "USER")
public ResponseEntity<Result<UserDto>> createUser(@Valid @RequestBody UserRequest request) { ... }
```

欄位說明：

- `action`：這次操作的名稱，例如 `CREATE_USER`、`UPDATE_CONFIG`。
- `resource`：操作目標資源，例如 `USER`、`SYSTEM_CONFIG`；若未指定，預設會使用 `類別名.方法名`。

## 3. 切面行為 `AuditAspect`

位置：`brainwave-core/src/main/java/com/brainwave/core/audit/AuditAspect.java`

行為摘要：

- 使用 Spring AOP 攔截所有標記了 `@Audit` 的方法。
- 先執行原本方法（`joinPoint.proceed()`），不更動回傳值與例外流程。
- 若 `app.audit.enabled=false`，則不輸出任何稽核 log。
- 若 `app.audit.enabled=true`，則在 finally 區塊輸出一條類似下列格式的 log：

```text
AUDIT action=CREATE_USER resource=USER method=POST path=/api/users success=true durationMs=34 requestId=abc-123 principal=TokenPrincipal[scope=ADMIN_USER, subject=7, role=ADMIN]
```

目前記錄的欄位：

- `action`：來自 `@Audit(action = ...)`
- `resource`：來自 `@Audit(resource = ...)`，或預設類別名.方法名
- `method`：HTTP 方法（GET/POST/PUT/DELETE…）
- `path`：請求路徑（例如 `/api/users`、`/api/system-configs`）
- `success`：boolean，方法是否拋出例外
- `durationMs`：方法執行時間（毫秒）
- `requestId`：來自 MDC（由 CorrelationIdFilter 寫入），與 X-Request-ID 一致，方便與一般 log 串接
- `principal`：若有登入，從 request attribute `auth.principal`（AuthGuardInterceptor 設定）取出的 principal 字串；未登入則為空

## 4. 已掛載範例

目前示範範圍（之後可視需求擴充）：

- `UserController`（`brainwave-backend`）
  - `createUser`：`@Audit(action = "CREATE_USER", resource = "USER")`
  - `updateUser`：`@Audit(action = "UPDATE_USER", resource = "USER")`
  - `deleteUser`：`@Audit(action = "DELETE_USER", resource = "USER")`

這些 API 在 `app.audit.enabled=true` 時，每次被呼叫都會產生一筆 AUDIT log。

## 5. 後續可擴充方向（非本次範圍）

1. ~~**加入「誰」的資訊**~~（已實作：AuditAspect 從 request attribute `auth.principal` 取 principal，並從 MDC 取 requestId 一併寫入 log。）

2. ~~**落地到 DB 或外部 log 系統**~~（已實作：`app.audit.sink=db` 時由 `DbAuditLogService` 寫入 `audit_log` 表；`AuditLogService` 介面、`ConsoleAuditLogService` / `DbAuditLogService` 依 sink 切換。）

3. **支援記錄 request/response body**
   - 依 `AuditProperties` 的 include* 與 maxBodyLength 決定是否抓 body。
   - 對 `maskFields` 中的欄位做遮罩（例如密碼、token）。

4. ~~**標準化格式與 trace id 整合**~~（已實作：AUDIT log 已含 requestId，與 CorrelationIdFilter 的 MDC 一致。）

