package com.brainwave.service.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brainwave.core.config.properties.StorageProperties;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalStorageServiceTest {

    private StorageProperties storageProperties;
    private LocalStorageService localStorageService;

    @BeforeEach
    void setUp() {
        storageProperties = new StorageProperties();
        storageProperties.setProvider("local");
        storageProperties.getLocal().setBasePath("build/uploads-test");
        storageProperties.getLocal().setPublicBaseUrl("https://cdn.example.com/files");
        localStorageService = new LocalStorageService(storageProperties);
    }

    @Test
    void store_shouldCreateFileUnderBasePath() throws Exception {
        String path = "avatars/user-" + System.nanoTime() + ".txt";
        String content = "hello";

        String storedPath = localStorageService.store(path, new ByteArrayInputStream(content.getBytes()));

        assertEquals(path, storedPath);

        Path target = Path.of(storageProperties.getLocal().getBasePath(), storedPath);
        assertTrue(Files.exists(target));
        assertEquals(content, Files.readString(target));
    }

    @Test
    void toPublicUrl_shouldCombineBaseUrlAndPath() {
        String url = localStorageService.toPublicUrl("avatars/user-1.png");
        assertEquals("https://cdn.example.com/files/avatars/user-1.png", url);
    }
}

