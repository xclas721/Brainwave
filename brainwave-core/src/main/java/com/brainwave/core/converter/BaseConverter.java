package com.brainwave.core.converter;

import java.util.List;
import org.mapstruct.MappingTarget;

/**
 * 基礎轉換器介面，定義通用的 DTO/VO/REQ 轉換方法
 *
 * 注意：實際的轉換器應使用 @Mapper 註解，並繼承此介面
 *
 * @param <E> Entity 類型
 * @param <D> DTO 類型
 * @param <V> VO 類型
 * @param <R> Request 類型
 */
public interface BaseConverter<E, D, V, R> {

    /**
     * Entity 轉 DTO
     */
    D toDto(E entity);

    /**
     * DTO 轉 Entity
     */
    E toEntity(D dto);

    /**
     * Entity 轉 VO
     */
    V toVo(E entity);

    /**
     * DTO 轉 VO
     */
    V toVoFromDto(D dto);

    /**
     * Request 轉 Entity
     */
    E toEntityFromRequest(R request);

    /**
     * Request 轉 DTO
     */
    D toDtoFromRequest(R request);

    /**
     * 更新 Entity（從 DTO）
     */
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    /**
     * 更新 Entity（從 Request）
     */
    void updateEntityFromRequest(R request, @MappingTarget E entity);

    /**
     * Entity 列表轉 DTO 列表
     */
    List<D> toDtoList(List<E> entities);

    /**
     * DTO 列表轉 Entity 列表
     */
    List<E> toEntityList(List<D> dtos);

    /**
     * Entity 列表轉 VO 列表
     */
    List<V> toVoList(List<E> entities);

    /**
     * DTO 列表轉 VO 列表
     */
    List<V> toVoListFromDto(List<D> dtos);
}
