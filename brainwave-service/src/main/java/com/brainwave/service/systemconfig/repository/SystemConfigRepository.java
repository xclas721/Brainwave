package com.brainwave.service.systemconfig.repository;

import com.brainwave.core.base.BaseRepository;
import com.brainwave.service.systemconfig.entity.SystemConfigEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends BaseRepository<SystemConfigEntity, Long>,
        JpaSpecificationExecutor<SystemConfigEntity> {

    Optional<SystemConfigEntity> findByKey(String key);

    boolean existsByKey(String key);
}

