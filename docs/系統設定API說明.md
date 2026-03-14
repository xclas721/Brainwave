# 系統設定 API（Key-Value）使用說明

> 目標：提供後台可維護的 Key-Value 設定（開關、公告、參數等），存於資料庫，供 API 或前端讀取。

## 1. 用途

- **業務**：功能開關、公告內容、每頁筆數、上傳限制等，不需改程式即可調整。
- **維運**：緊急關閉某功能、調整門檻，由後台管理介面或 API 更新。

## 2. 資料模型

- **表**：`system_config`（Flyway `V3__system_config.sql`）
- **欄位**：`config_key`（唯一）、`config_value`、`config_type`、`description`、`created_at`、`updated_at`

## 3. API 與權限

| 端點 | 說明 | 權限 |
|------|------|------|
| `GET /api/system-configs/by-key/{key}` | 依 key 取得單筆設定 | 依 Guard 設定（預設為後台路徑，需後台登入） |
| `GET /api/system-configs/{id}` | 依 id 取得單筆（管理用） | 同上 |
| `GET /api/system-configs/search` | 分頁搜尋（key/type 等條件） | 同上 |
| `POST /api/system-configs` | 新增設定 | 同上 |
| `PUT /api/system-configs/{id}` | 更新設定內容 | 同上 |

路徑已納入 `app.auth.guard.admin-path-prefixes`，需後台 scope（ADMIN_*）才能存取，詳見 **權限守衛與RBAC說明.md**。

## 4. 關鍵類與路徑

| 層級 | 類別／路徑 |
|------|------------|
| Backend | `SystemConfigController`（`brainwave-backend`） |
| Service | `SystemConfigService`、`SystemConfigRepository`（`brainwave-service/systemconfig`） |
| Entity / DTO / VO | `SystemConfigEntity`、`SystemConfigDto`、`SystemConfigVo`、Request 系列 |
| DB | `system_config` 表，migration：`V3__system_config.sql` |

## 5. 使用範例

- **後台**：呼叫 `GET /api/system-configs/search` 列出設定，`PUT /api/system-configs/{id}` 更新值。
- **前台或內部**：若需給未登入者讀取部分 key（例如公告），可另開一支白名單 API（如 `GET /api/public/config?keys=announcement,feature_x`），或將該路徑加入 `front-exclude-prefixes` 並由程式控制可讀 key。

## 6. 延伸閱讀

- 路徑與權限：**權限守衛與RBAC說明.md**
- 設定治理（app.*）：**設定治理說明.md**
- 專案結構：根目錄 **專案目錄結構.md**（systemconfig 套件）
