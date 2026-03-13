package com.brainwave.service.identity.template.repository;

import com.brainwave.service.identity.template.entity.IdentityEntityTemplate;
import java.util.Optional;

/**
 * Identity Repository 模板。
 * 實作時可改為 extends JpaRepository<IdentityEntity, Long>。
 */
public interface IdentityRepositoryTemplate {

    Optional<IdentityEntityTemplate> findByAccount(String account);

    boolean existsByAccount(String account);
}
