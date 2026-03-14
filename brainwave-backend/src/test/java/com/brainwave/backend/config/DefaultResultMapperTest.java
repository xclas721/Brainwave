package com.brainwave.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brainwave.core.common.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class DefaultResultMapperTest {

    private DefaultResultMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DefaultResultMapper();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void success_withoutMdc_shouldSetCorrelationIdNull() {
        Result<String> result = mapper.success("data");
        assertTrue(result.isSuccess());
        assertEquals("SUCCESS", result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals("data", result.getData());
        assertNull(result.getCorrelationId());
    }

    @Test
    void success_withMdc_shouldSetCorrelationId() {
        MDC.put("requestId", "req-123");
        try {
            Result<String> result = mapper.success("data");
            assertTrue(result.isSuccess());
            assertEquals("req-123", result.getCorrelationId());
        } finally {
            MDC.remove("requestId");
        }
    }

    @Test
    void success_withMessage_shouldUseMessage() {
        Result<Integer> result = mapper.success("建立成功", 1);
        assertTrue(result.isSuccess());
        assertEquals("建立成功", result.getMessage());
        assertEquals(1, result.getData());
    }

    @Test
    void fail_shouldSetCodeAndMessage() {
        Result<Void> result = mapper.fail("VALIDATION_ERROR", "欄位不可為空");
        assertFalse(result.isSuccess());
        assertEquals("VALIDATION_ERROR", result.getCode());
        assertEquals("欄位不可為空", result.getMessage());
        assertNull(result.getData());
        assertNotNull(result.getTimestamp());
    }
}
