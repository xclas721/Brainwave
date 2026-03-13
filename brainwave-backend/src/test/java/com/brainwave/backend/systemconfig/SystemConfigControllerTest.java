package com.brainwave.backend.systemconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brainwave.core.exception.GlobalExceptionHandler;
import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import com.brainwave.service.systemconfig.service.SystemConfigService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class SystemConfigControllerTest {

    @Mock
    private SystemConfigService systemConfigService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new SystemConfigController(systemConfigService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getByKey_shouldReturnConfig() throws Exception {
        SystemConfigDto dto = SystemConfigDto.builder()
                .id(1L)
                .key("SITE_TITLE")
                .value("Brainwave")
                .type("STRING")
                .build();

        when(systemConfigService.getByKey("SITE_TITLE")).thenReturn(dto);

        mockMvc.perform(get("/api/system-configs/by-key/SITE_TITLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.key").value("SITE_TITLE"))
                .andExpect(jsonPath("$.data.value").value("Brainwave"));
    }

    @Test
    void search_shouldUseDefaultPagingAndReturnPageResponse() throws Exception {
        Page<?> page = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                0
        );

        when(systemConfigService.search(any(), any(PageRequest.class))).thenReturn((Page) page);

        mockMvc.perform(get("/api/system-configs/search").param("key", "SITE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.currentPage").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10));

        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(systemConfigService).search(any(), pageableCaptor.capture());

        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(10, pageableCaptor.getValue().getPageSize());
    }

    @Test
    void create_whenRequestInvalid_shouldReturnBadRequest() throws Exception {
        String invalidJson = """
                {
                  "key": "",
                  "value": "",
                  "type": ""
                }
                """;

        mockMvc.perform(post("/api/system-configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }
}

