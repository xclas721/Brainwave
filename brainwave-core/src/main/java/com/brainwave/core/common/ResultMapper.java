package com.brainwave.core.common;

/**
 * 統一回應格式的組裝策略，可替換為企業規格（例如加上 correlationId、不同欄位名）。
 * 預設使用 {@link Result}；替換實作後可回傳自訂 DTO。
 */
public interface ResultMapper {

    <T> Result<T> success(T data);

    <T> Result<T> success(String message, T data);

    Result<Void> fail(String code, String message);
}
