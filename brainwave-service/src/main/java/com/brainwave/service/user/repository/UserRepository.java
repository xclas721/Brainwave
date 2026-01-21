package com.brainwave.service.user.repository;

import com.brainwave.core.base.BaseRepository;
import com.brainwave.service.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
