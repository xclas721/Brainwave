# MapStruct 轉換器基底與 Feature 覆寫

## 目的

說明 core 的 `BaseConverter` 契約與各模組的 MapStruct 實作方式；**保留基底、允許 feature-level 覆寫**，以應付複雜欄位或自訂映射邏輯。

## 基底契約（core）

- **介面**：`com.brainwave.core.converter.BaseConverter<E, D, V, R>`
  - 定義：`toDto`、`toEntity`、`toVo`、`toEntityFromRequest`、`toDtoFromRequest`、`updateEntityFromDto`、`updateEntityFromRequest`，以及對應的 List 方法。
- 實際轉換由各模組的 `@Mapper(componentModel = "spring", ...)` 介面實作，MapStruct 產生實作類別。

## Feature-level 覆寫

各模組的 Converter 可：

1. **繼承 BaseConverter**，並以 `@Mapping` 指定忽略或對應欄位（例如 `id`、`createdAt`、`updatedAt`、`password`）。
2. **新增方法**：若 Request 型別有「新增用」與「更新用」之分，可新增例如 `updateEntityFromUpdateRequest(UpdateRequest, @MappingTarget Entity)`，不影響基底介面。
3. **覆寫基底方法**：在介面中 `@Override` 並加上自訂 `@Mapping` 或 `@AfterMapping`，即可覆寫預設行為。

範例（與現有 UserConverter / MemberConverter 一致）：

- `toEntityFromRequest`：`@Mapping(target = "id", ignore = true)` 等。
- `updateEntityFromUpdateRequest`：僅更新 profile、不寫入密碼時，對 `password` 設 `ignore = true`。

## 注意

- 基底介面僅定義契約，不限制 MapStruct 的 `unmappedTargetPolicy`；各模組可依需要設為 `IGNORE` 或 `WARN`。
- 複雜轉換（多來源、自訂邏輯）可寫在 default method 或抽象類別中，由 MapStruct 與手寫程式搭配。
