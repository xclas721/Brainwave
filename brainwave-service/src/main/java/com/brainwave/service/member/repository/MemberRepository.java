package com.brainwave.service.member.repository;

import com.brainwave.core.base.BaseRepository;
import com.brainwave.service.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends BaseRepository<MemberEntity, Long>, JpaSpecificationExecutor<MemberEntity> {

    Optional<MemberEntity> findByUsername(String username);

    Optional<MemberEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
