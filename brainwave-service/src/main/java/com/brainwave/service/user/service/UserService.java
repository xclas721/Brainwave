package com.brainwave.service.user.service;

import com.brainwave.core.base.BaseServiceImpl;
import com.brainwave.core.exception.BusinessException;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.entity.UserEntity;
import com.brainwave.service.user.repository.UserRepository;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.vo.UserVo;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends BaseServiceImpl<UserEntity, Long, UserRepository> {

    private final UserConverter userConverter;

    public UserService(UserRepository repository, UserConverter userConverter) {
        super(repository);
        this.userConverter = userConverter;
    }

    @Transactional
    public UserDto createUser(UserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email 已存在 " + request.getEmail());
        }

        UserEntity entity = userConverter.toEntityFromRequest(request);
        UserEntity saved = save(entity);
        return userConverter.toDto(saved);
    }

    public UserDto getUserById(Long id) {
        UserEntity entity = findByIdOrThrow(id);
        return userConverter.toDto(entity);
    }

    public List<UserVo> getAllUsers() {
        List<UserEntity> entities = findAll();
        return userConverter.toVoList(entities);
    }

    @Transactional
    public UserDto updateUser(Long id, UserRequest request) {
        UserEntity entity = findByIdOrThrow(id);

        if (!entity.getEmail().equals(request.getEmail()) && repository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email 已存在 " + request.getEmail());
        }

        userConverter.updateEntityFromRequest(request, entity);
        UserEntity updated = save(entity);
        return userConverter.toDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        deleteById(id);
    }
}
