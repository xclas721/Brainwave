# Config Layer 治理規範（P0-3）

> 目標：把「設定」從到處亂飛的 `@Value` 字串和零散環境變數，收斂成**有規則、有型別、可驗證**的一層 Config Layer。

## 1. 分層原則（設定從哪裡來？）

- **Env Layer**：環境變數為部署入口（例如 `APP_AUTH_GUARD_ENABLED`）。
- **Profile Layer**：`application-{profile}.properties` 提供不同環境預設值。
- **Runtime Layer**：程式碼只透過型別化設定物件存取（`@ConfigurationProperties`）。

## 2. 命名規範（app.* 要怎麼取名？）

- 所有業務設定統一放在 `app.*` 前綴。
- 以 bounded context 分段：`app.auth.*`、`app.cors.*`、後續 `app.storage.*`、`app.audit.*`。
- 屬性名稱使用 kebab-case（例如 `app.auth.guard.enabled`）。

## 3. 程式碼使用規範（寫程式時怎麼用設定？）

- 優先使用 `@ConfigurationProperties` 類別，不再新增新的 `@Value` 字串注入。
- 設定類別集中在 `brainwave-core` 的 `com.brainwave.core.config.properties`。
- Service/Config/Interceptor 只依賴設定類別，不直接解析環境變數字串。

## 4. 已落地範圍（第一、二階段）

- `AuthProperties`：接管 `app.auth.demo.*`、`app.auth.front.demo.*`、`app.auth.guard.enabled` 與 **Guard 路徑**（`admin-path-prefixes`、`front-path-prefixes`、`front-exclude-prefixes`），詳見 RBAC-GUIDE。
- `CorsProperties`：接管 `app.cors.allowed-origins`、`app.cors.allow-credentials`
- `StorageProperties`：定義 `app.storage.*`（local / s3）型別化骨架
- `AuditProperties`：定義 `app.audit.*`（啟用、body、遮罩）型別化骨架
- `DefaultAuthFacade`、`AuthGuardInterceptor`、`CorsConfig` 已改為型別化存取。
- `application.properties` 已補齊 `app.storage.*`、`app.audit.*` 預設鍵。
- `META-INF/additional-spring-configuration-metadata.json` 已宣告 `app.*` 自訂鍵，避免 IDE 出現 unknown property 提示。
- 設定類別已加入 `@Validated` 與基本約束，並以 `ConfigPropertiesValidationTest` 驗證關鍵欄位。

## 5. 後續擴充建議

1. 實作 `StorageService` 時，統一僅讀取 `StorageProperties`。
2. 實作 `Audit Log` 時，統一僅讀取 `AuditProperties`。
3. 視需要補充「條件式驗證」（例如 provider=s3 時強制 bucket/region）。
4. 若導入 DB 設定中心，保持 `app.*` 命名與型別化存取介面不變。
