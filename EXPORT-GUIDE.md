# Excel/CSV 匯出（P2-3）

## 用途

提供列表資料匯出為 CSV 的通用工具，方便後台「匯出目前列表」之類需求，且不依賴 Excel 專用套件即可產出可於 Excel 開啟的 UTF-8 CSV。

## 實作方式

- **CsvExportHelper**（`brainwave-core` 模組 `com.brainwave.core.export`）
  - 靜態方法：`writeCsv(OutputStream, headers, rows)`、`toCsvBytes(headers, rows)`
  - 第一列為標題，其後每列為一筆資料；欄位依 RFC 4180 規則 escape（逗號、雙引號、換行）
  - 預設寫入 UTF-8 BOM，讓 Excel 正確辨識編碼

## 使用範例

在 Controller 中取得列表後轉成標題列 + 資料列，再呼叫 helper 產出位元組並設定 response：

```java
List<String> headers = List.of("ID", "名稱", "建立時間");
List<List<String>> rows = new ArrayList<>();
for (YourVo vo : list) {
    rows.add(List.of(
        String.valueOf(vo.getId()),
        vo.getName() != null ? vo.getName() : "",
        vo.getCreatedAt() != null ? formatter.format(vo.getCreatedAt()) : ""
    ));
}
byte[] csv = CsvExportHelper.toCsvBytes(headers, rows);
// 設定 Content-Type: text/csv; charset=UTF-8、Content-Disposition: attachment; filename="xxx.csv"
return ResponseEntity.ok().headers(...).body(csv);
```

目前 **UserController** 提供示範端點：`GET /api/users/export`（需 ADMIN 權限），回傳 `users.csv`。

## 擴充為 Excel（.xlsx）

若需產出 `.xlsx`，可另行引入 Apache POI（或類似套件），在 service 或專用 helper 中產出 `Workbook` 並寫入 `OutputStream`；Controller 層仍可沿用「取得列表 → 轉成結構化資料 → 寫入 response」的模式，僅將「CSV 產出」換成「Excel 產出」。
