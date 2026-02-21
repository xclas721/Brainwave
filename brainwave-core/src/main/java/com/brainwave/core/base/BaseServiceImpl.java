package com.brainwave.core.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 基礎 Service 實作，提供通用 CRUD 操作
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID, R extends JpaRepository<T, ID>>
        implements BaseService<T, ID> {

    protected final R repository;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        return repository.save(entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("entities must not be null");
        }
        return repository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.findById(id);
    }

    @Override
    public T findByIdOrThrow(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new com.brainwave.core.exception.ResourceNotFoundException(
                        "RESOURCE_NOT_FOUND",
                        "Resource with id " + id + " not found"
                ));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("pageable must not be null");
        }
        return repository.findAll(pageable);
    }

    @Override
    public void deleteById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        repository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        repository.delete(entity);
    }

    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
