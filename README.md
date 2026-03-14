# Brainwave 後端平台

這是一個以 **Spring Boot 3.x + Java 17** 為核心的多模組後端專案，目標是提供乾淨的分層結構、可重用的基礎能力，以及穩定的擴充方式，讓後續業務模組能快速成型。

## 設計目標
- 統一錯誤處理與回應格式，降低 API 行為不一致的風險
- 模組化分層清楚，業務邏輯可獨立演進
- 以範例模板引導新增模組的做法，降低重工成本

## 模組定位（概念）
- `brainwave-core`：共用基礎設施（例外、回應、基底類別、轉換器）
- `brainwave-service`：業務實作層（Entity/DTO/Service/Repository）
- `brainwave-backend`：API 層（Controller / 對外介面）

## User / Member 範例定位
- `user`（後台帳號）與 `member`（前台會員）皆為**範例模組**，展示雙軌 Identity 的寫法。
- 新專案可依需求保留單軌或雙軌；若只需一種帳號，請參考 `IDENTITY-MODULE-TEMPLATE.md` 的單軌/雙軌決策與**移除/整併指引**。