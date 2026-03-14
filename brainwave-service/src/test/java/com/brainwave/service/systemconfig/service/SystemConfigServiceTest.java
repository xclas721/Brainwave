package com.brainwave.service.systemconfig.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brainwave.core.exception.BusinessException;
import com.brainwave.service.systemconfig.converter.SystemConfigConverter;
import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.entity.SystemConfigEntity;
import com.brainwave.service.systemconfig.repository.SystemConfigRepository;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class SystemConfigServiceTest {

    @Mock
    private SystemConfigRepository repository;

    @Mock
    private SystemConfigConverter converter;

    @InjectMocks
    private SystemConfigService service;

    private SystemConfigEntity entity;

    @BeforeEach
    void setUp() {
        entity = new SystemConfigEntity();
        entity.setId(1L);
        entity.setKey("SITE_TITLE");
        entity.setValue("Brainwave");
        entity.setType("STRING");
    }

    @Test
    void create_whenKeyNotExists_shouldCreateConfig() {
        SystemConfigRequest request = new SystemConfigRequest();
        request.setKey("SITE_TITLE");
        request.setValue("Brainwave");
        request.setType("STRING");

        when(repository.existsByKey("SITE_TITLE")).thenReturn(false);
        when(converter.toEntityFromRequest(request)).thenReturn(entity);
        when(repository.save(any(SystemConfigEntity.class))).thenReturn(entity);
        when(converter.toDto(entity)).thenReturn(SystemConfigDto.builder()
                .id(1L)
                .key("SITE_TITLE")
                .value("Brainwave")
                .type("STRING")
                .build());

        SystemConfigDto dto = service.create(request);

        assertEquals("SITE_TITLE", dto.getKey());
        verify(repository).save(any(SystemConfigEntity.class));
    }

    @Test
    void create_whenKeyExists_shouldThrowBusinessException() {
        SystemConfigRequest request = new SystemConfigRequest();
        request.setKey("SITE_TITLE");
        request.setValue("Brainwave");
        request.setType("STRING");

        when(repository.existsByKey("SITE_TITLE")).thenReturn(true);

        BusinessException ex = null;
        try {
            service.create(request);
        } catch (BusinessException e) {
            ex = e;
        }

        assertNotNull(ex);
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void getByKey_shouldReturnDto() {
        when(repository.findByKey("SITE_TITLE")).thenReturn(Optional.of(entity));
        when(converter.toDto(entity)).thenReturn(SystemConfigDto.builder()
                .id(1L)
                .key("SITE_TITLE")
                .value("Brainwave")
                .type("STRING")
                .build());

        SystemConfigDto dto = service.getByKey("SITE_TITLE");

        assertEquals("SITE_TITLE", dto.getKey());
        verify(repository).findByKey("SITE_TITLE");
    }

}

