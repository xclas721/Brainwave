package com.brainwave.core.storage;

import com.brainwave.core.exception.BaseException;

public class StorageException extends BaseException {

    public StorageException(String errorCode, String message) {
        super(errorCode, message);
    }

    public static StorageException failedToStore(String filename, Throwable cause) {
        StorageException ex = new StorageException("STORAGE_STORE_FAILED", "Failed to store file: " + filename);
        ex.initCause(cause);
        return ex;
    }

    public static StorageException fileNotFound(String path) {
        return new StorageException("STORAGE_NOT_FOUND", "File not found: " + path);
    }
}

