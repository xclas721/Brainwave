package com.brainwave.core.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 基礎 Service 介面，定義通用 CRUD 操作
 */
public interface BaseService<T extends BaseEntity, ID> {

    /**
     * 儲存實體
     */
    T save(T entity);

    /**
     * 批量儲存
     */
    List<T> saveAll(Iterable<T> entities);

    /**
     * 根據 ID 查找
     */
    Optional<T> findById(ID id);

    /**
     * 根據 ID 查找，若不存在則拋出例外
     */
    T findByIdOrThrow(ID id);

    /**
     * 查找所有
     */
    List<T> findAll();

    /**
     * 分頁查找
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 根據 ID 刪除
     */
    void deleteById(ID id);

    /**
     * 刪除實體
     */
    void delete(T entity);

    /**
     * 檢查是否存在
     */
    boolean existsById(ID id);

    /**
     * 計算總數
     */
    long count();
}
