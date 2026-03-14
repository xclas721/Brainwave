package com.brainwave.backend.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brainwave.core.exception.GlobalExceptionHandler;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.request.UserSearchRequest;
import com.brainwave.service.user.service.UserService;
import com.brainwave.service.user.vo.UserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserConverter userConverter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, userConverter))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getUser_shouldReturnUserVo() throws Exception {
        UserDto dto = UserDto.builder()
                .id(1L)
                .username("amy")
                .name("Amy")
                .email("amy@example.com")
                .phone("0912345678")
                .build();

        UserVo vo = UserVo.builder()
                .id(1L)
                .username("amy")
                .name("Amy")
                .email("amy@example.com")
                .phone("0912345678")
                .build();

        when(userService.getUserById(1L)).thenReturn(dto);
        when(userConverter.toVoFromDto(dto)).thenReturn(vo);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("amy"));
    }

    @Test
    void searchUsers_shouldUseDefaultPagingAndReturnPageResponse() throws Exception {
        UserVo vo = UserVo.builder()
                .id(2L)
                .username("john")
                .name("John")
                .email("john@example.com")
                .build();

        Page<UserVo> page = new PageImpl<>(
                java.util.List.of(vo),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                1
        );

        when(userService.searchUsers(any(UserSearchRequest.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users/search").param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].username").value("john"))
                .andExpect(jsonPath("$.data.currentPage").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10));

        ArgumentCaptor<UserSearchRequest> requestCaptor = ArgumentCaptor.forClass(UserSearchRequest.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(userService).searchUsers(requestCaptor.capture(), pageableCaptor.capture());

        assertEquals("john", requestCaptor.getValue().getName());
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(10, pageableCaptor.getValue().getPageSize());
        Sort.Order order = pageableCaptor.getValue().getSort().getOrderFor("id");
        assertNotNull(order);
        assertEquals(Sort.Direction.ASC, order.getDirection());
    }

    @Test
    void createUser_whenRequestInvalid_shouldReturnBadRequest() throws Exception {
        String invalidJson = """
                {
                  "username": "",
                  "name": "",
                  "email": "invalid-email"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));

        verifyNoInteractions(userService);
    }
}
