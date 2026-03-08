package com.brainwave.service.user.service;

import com.brainwave.core.base.BaseServiceImpl;
import com.brainwave.core.exception.BusinessException;
import com.brainwave.core.utils.PasswordUtil;
import com.brainwave.service.user.converter.UserConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.entity.UserEntity;
import com.brainwave.service.user.repository.UserRepository;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.request.UserSearchRequest;
import com.brainwave.service.user.request.UserUpdateRequest;
import com.brainwave.service.user.vo.UserVo;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("USERNAME_EXISTS", "帳號 已存在 " + request.getUsername());
        }

        UserEntity entity = userConverter.toEntityFromRequest(request);
        // 加密密碼 (預設 123456 如果沒給，或者強制要求)
        String rawPwd = request.getPassword() != null ? request.getPassword() : "123456";
        entity.setPassword(PasswordUtil.encode(rawPwd));

        UserEntity saved = save(entity);
        return userConverter.toDto(saved);
    }

    public UserDto login(String username, String password) {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("AUTH_FAILED", "帳號或密碼錯誤"));

        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException("AUTH_FAILED", "帳號或密碼錯誤");
        }
        return userConverter.toDto(user);
    }

    public UserDto getUserById(Long id) {
        UserEntity entity = findByIdOrThrow(id);
        return userConverter.toDto(entity);
    }

    public List<UserVo> getAllUsers() {
        List<UserEntity> entities = findAll();
        return userConverter.toVoList(entities);
    }

    /**
     * 分頁查詢使用者（支援條件查詢）
     */
    public Page<UserVo> searchUsers(UserSearchRequest request, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("pageable must not be null");
        }
        Specification<UserEntity> spec = buildSearchSpecification(request);
        Page<UserEntity> page = repository.findAll(spec, pageable);
        return page.map(userConverter::toVo);
    }

    /**
     * 建立查詢條件
     */
    private Specification<UserEntity> buildSearchSpecification(UserSearchRequest request) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (request != null) {
                if (request.getName() != null && !request.getName().trim().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("name")),
                                    "%" + request.getName().toLowerCase().trim() + "%"));
                }

                if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("email")),
                                    "%" + request.getEmail().toLowerCase().trim() + "%"));
                }

                if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.like(root.get("phone"), "%" + request.getPhone().trim() + "%"));
                }
            }

            return predicate;
        };
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        UserEntity entity = findByIdOrThrow(id);

        if (!Objects.equals(entity.getEmail(), request.getEmail()) && repository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email 已存在 " + request.getEmail());
        }

        userConverter.updateEntityFromUpdateRequest(request, entity);
        UserEntity updated = save(entity);
        return userConverter.toDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        deleteById(id);
    }
}
