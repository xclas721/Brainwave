package com.brainwave.core.base;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基礎 Repository 介面，擴展 JpaRepository 提供通用方法
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    /**
     * 根據 ID 查找，若不存在則拋出例外
     */
    default T findByIdOrThrow(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return findById(id)
                .orElseThrow(() -> new com.brainwave.core.exception.ResourceNotFoundException(
                        "RESOURCE_NOT_FOUND",
                        "Resource with id " + id + " not found"
                ));
    }

    /**
     * 根據 ID 查找，返回 Optional
     */
    default Optional<T> findByIdOptional(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return findById(id);
    }
}
