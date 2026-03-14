package com.brainwave.service.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brainwave.core.exception.BusinessException;
import com.brainwave.core.utils.PasswordUtil;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.entity.UserEntity;
import com.brainwave.service.user.repository.UserRepository;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.request.UserSearchRequest;
import com.brainwave.service.user.vo.UserVo;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"null", "unchecked"})
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldUseDefaultPasswordWhenMissing() {
        UserRequest request = UserRequest.builder()
                .username("amy")
                .name("Amy")
                .email("amy@example.com")
                .build();

        UserEntity mappedEntity = new UserEntity();
        mappedEntity.setUsername("amy");
        mappedEntity.setName("Amy");
        mappedEntity.setEmail("amy@example.com");

        UserDto expectedDto = UserDto.builder()
                .id(1L)
                .username("amy")
                .name("Amy")
                .email("amy@example.com")
                .build();

        when(userRepository.existsByEmail("amy@example.com")).thenReturn(false);
        when(userRepository.findByUsername("amy")).thenReturn(Optional.empty());
        when(userConverter.toEntityFromRequest(request)).thenReturn(mappedEntity);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(expectedDto);

        UserDto result = userService.createUser(request);

        assertEquals(expectedDto, result);

        ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(entityCaptor.capture());
        assertTrue(PasswordUtil.matches("123456", entityCaptor.getValue().getPassword()));
    }

    @Test
    void createUser_whenEmailExists_shouldThrowBusinessException() {
        UserRequest request = UserRequest.builder()
                .username("amy")
                .name("Amy")
                .email("amy@example.com")
                .build();

        when(userRepository.existsByEmail("amy@example.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.createUser(request));
    }

    @Test
    void searchUsers_whenPageableIsNull_shouldThrowIllegalArgumentException() {
        UserSearchRequest request = UserSearchRequest.builder().name("amy").build();

        assertThrows(IllegalArgumentException.class, () -> userService.searchUsers(request, null));
    }

    @Test
    void searchUsers_shouldMapEntityPageToVoPage() {
        UserSearchRequest request = UserSearchRequest.builder().name("amy").build();
        PageRequest pageable = PageRequest.of(0, 10);

        UserEntity entity = new UserEntity();
        entity.setUsername("amy");
        entity.setName("Amy");
        entity.setEmail("amy@example.com");

        UserVo vo = UserVo.builder().username("amy").name("Amy").email("amy@example.com").build();

        Page<UserEntity> entityPage = new PageImpl<>(java.util.List.of(entity), pageable, 1);
        when(userRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(entityPage);
        when(userConverter.toVo(entity)).thenReturn(vo);

        Page<UserVo> result = userService.searchUsers(request, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("amy", result.getContent().get(0).getUsername());
    }
}
