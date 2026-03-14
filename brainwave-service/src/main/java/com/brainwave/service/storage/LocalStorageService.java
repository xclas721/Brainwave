package com.brainwave.service.storage;

import com.brainwave.core.config.properties.StorageProperties;
import com.brainwave.core.storage.StorageException;
import com.brainwave.core.storage.StorageService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LocalStorageService implements StorageService {

    private final StorageProperties storageProperties;

    public LocalStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    @SuppressWarnings("null")
    public String store(String pathUnderBase, InputStream inputStream) {
        if (!"local".equalsIgnoreCase(storageProperties.getProvider())) {
            throw new StorageException("STORAGE_PROVIDER_MISMATCH", "Current provider is not local");
        }

        try {
            String cleanPath = StringUtils.cleanPath(pathUnderBase);
            String basePath = Objects.requireNonNull(storageProperties.getLocal().getBasePath(), "storage base path");
            Path base = Paths.get(basePath).toAbsolutePath().normalize();
            Path target = base.resolve(cleanPath).normalize();

            Files.createDirectories(target.getParent());
            Files.copy(inputStream, target);

            return cleanPath.replace("\\", "/");
        } catch (IOException e) {
            throw StorageException.failedToStore(pathUnderBase, e);
        }
    }

    @Override
    public String toPublicUrl(String storedPath) {
        String baseUrl = storageProperties.getLocal().getPublicBaseUrl();
        if (!StringUtils.hasText(baseUrl)) {
            return storedPath;
        }
        if (baseUrl.endsWith("/")) {
            return baseUrl + storedPath;
        }
        return baseUrl + "/" + storedPath;
    }
}

