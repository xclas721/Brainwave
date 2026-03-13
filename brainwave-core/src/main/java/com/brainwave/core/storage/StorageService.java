package com.brainwave.core.storage;

import java.io.InputStream;

/**
 * 檔案儲存抽象介面，後續可替換為本地或 S3/MinIO 等實作。
 */
public interface StorageService {

    /**
     * 儲存檔案並回傳儲存後的相對路徑或 key。
     *
     * @param pathUnderBase 建議存放路徑（例如 "avatars/user-1.png"）
     * @param inputStream   檔案內容
     * @return 儲存後的相對路徑或 key
     */
    String store(String pathUnderBase, InputStream inputStream);

    /**
     * 產生公開 URL（若支援）。
     *
     * @param storedPath store 回傳的相對路徑或 key
     * @return 公開 URL，若未設定 publicBaseUrl 則可能回傳相對路徑
     */
    String toPublicUrl(String storedPath);
}

