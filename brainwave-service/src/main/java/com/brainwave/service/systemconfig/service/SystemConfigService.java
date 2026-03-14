package com.brainwave.service.systemconfig.service;

import com.brainwave.core.base.BaseServiceImpl;
import com.brainwave.core.exception.BusinessException;
import com.brainwave.service.systemconfig.converter.SystemConfigConverter;
import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.entity.SystemConfigEntity;
import com.brainwave.service.systemconfig.repository.SystemConfigRepository;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import com.brainwave.service.systemconfig.request.SystemConfigSearchRequest;
import com.brainwave.service.systemconfig.request.SystemConfigUpdateRequest;
import com.brainwave.service.systemconfig.vo.SystemConfigVo;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemConfigService extends BaseServiceImpl<SystemConfigEntity, Long, SystemConfigRepository> {

    private final SystemConfigConverter converter;

    public SystemConfigService(SystemConfigRepository repository, SystemConfigConverter converter) {
        super(repository);
        this.converter = converter;
    }

    @Transactional
    public SystemConfigDto create(SystemConfigRequest request) {
        if (repository.existsByKey(request.getKey())) {
            throw new BusinessException("CONFIG_KEY_EXISTS", "設定鍵已存在：" + request.getKey());
        }
        SystemConfigEntity entity = converter.toEntityFromRequest(request);
        SystemConfigEntity saved = save(entity);
        return converter.toDto(saved);
    }

    @Transactional
    public SystemConfigDto upsert(SystemConfigRequest request) {
        return repository.findByKey(request.getKey())
                .map(existing -> {
                    SystemConfigUpdateRequest updateRequest = new SystemConfigUpdateRequest();
                    updateRequest.setValue(request.getValue());
                    updateRequest.setType(request.getType());
                    updateRequest.setDescription(request.getDescription());
                    converter.updateEntityFromUpdateRequest(updateRequest, existing);
                    SystemConfigEntity updated = save(existing);
                    return converter.toDto(updated);
                })
                .orElseGet(() -> create(request));
    }

    @Transactional
    public SystemConfigDto update(Long id, SystemConfigUpdateRequest request) {
        SystemConfigEntity entity = findByIdOrThrow(id);
        converter.updateEntityFromUpdateRequest(request, entity);
        SystemConfigEntity updated = save(entity);
        return converter.toDto(updated);
    }

    public SystemConfigDto getByKey(String key) {
        SystemConfigEntity entity = repository.findByKey(key)
                .orElseThrow(() -> new BusinessException("CONFIG_NOT_FOUND", "設定不存在：" + key));
        return converter.toDto(entity);
    }

    public SystemConfigVo getById(Long id) {
        SystemConfigEntity entity = findByIdOrThrow(id);
        return converter.toVo(entity);
    }

    public List<SystemConfigVo> findAllList() {
        return converter.toVoList(findAll());
    }

    public Page<SystemConfigVo> search(SystemConfigSearchRequest request, @NonNull Pageable pageable) {
        Specification<SystemConfigEntity> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (request != null) {
                if (request.getKey() != null && !request.getKey().trim().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("key")),
                                    "%" + request.getKey().toLowerCase().trim() + "%"));
                }
                if (request.getType() != null && !request.getType().trim().isEmpty()) {
                    predicate = cb.and(predicate,
                            cb.equal(cb.lower(root.get("type")), request.getType().toLowerCase().trim()));
                }
            }

            return predicate;
        };

        Page<SystemConfigEntity> page = repository.findAll(spec, pageable);
        return page.map(converter::toVo);
    }
}

