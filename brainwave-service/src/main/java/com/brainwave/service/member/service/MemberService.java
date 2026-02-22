package com.brainwave.service.member.service;

import com.brainwave.core.base.BaseServiceImpl;
import com.brainwave.core.exception.BusinessException;
import com.brainwave.core.utils.PasswordUtil;
import com.brainwave.service.member.converter.MemberConverter;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.entity.MemberEntity;
import com.brainwave.service.member.repository.MemberRepository;
import com.brainwave.service.member.request.MemberRequest;
import com.brainwave.service.member.request.MemberSearchRequest;
import com.brainwave.service.member.vo.MemberVo;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MemberService extends BaseServiceImpl<MemberEntity, Long, MemberRepository> {

    private final MemberConverter memberConverter;

    public MemberService(MemberRepository repository, MemberConverter memberConverter) {
        super(repository);
        this.memberConverter = memberConverter;
    }

    @Transactional
    public MemberDto createMember(MemberRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new BusinessException("USERNAME_EXISTS", "帳號已存在 " + request.getUsername());
        }
        if (repository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email 已存在 " + request.getEmail());
        }

        MemberEntity entity = memberConverter.toEntityFromRequest(request);
        String rawPwd = request.getPassword() != null ? request.getPassword() : "123456";
        entity.setPassword(PasswordUtil.encode(rawPwd));

        MemberEntity saved = repository.save(entity);
        return memberConverter.toDto(saved);
    }

    public MemberDto login(String username, String password) {
        MemberEntity member = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("AUTH_FAILED", "帳號或密碼錯誤"));

        if (!PasswordUtil.matches(password, member.getPassword())) {
            throw new BusinessException("AUTH_FAILED", "帳號或密碼錯誤");
        }
        return memberConverter.toDto(member);
    }

    public MemberDto getMemberById(Long id) {
        MemberEntity entity = findByIdOrThrow(id);
        return memberConverter.toDto(entity);
    }

    public List<MemberVo> getAllMembers() {
        List<MemberEntity> entities = findAll();
        return memberConverter.toVoList(entities);
    }

    public Page<MemberVo> searchMembers(MemberSearchRequest request, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("pageable must not be null");
        }
        Specification<MemberEntity> spec = buildSearchSpecification(request);
        Page<MemberEntity> page = repository.findAll(spec, pageable);
        return page.map(memberConverter::toVo);
    }

    private Specification<MemberEntity> buildSearchSpecification(MemberSearchRequest request) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (request != null) {
                if (StringUtils.hasText(request.getName())) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("name")),
                                    "%" + request.getName().toLowerCase().trim() + "%"));
                }
                if (StringUtils.hasText(request.getEmail())) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("email")),
                                    "%" + request.getEmail().toLowerCase().trim() + "%"));
                }
                if (StringUtils.hasText(request.getPhone())) {
                    predicate = cb.and(predicate,
                            cb.like(root.get("phone"), "%" + request.getPhone().trim() + "%"));
                }
            }
            return predicate;
        };
    }

    @Transactional
    public MemberDto updateMember(Long id, MemberRequest request) {
        MemberEntity entity = findByIdOrThrow(id);

        if (!entity.getEmail().equals(request.getEmail()) && repository.existsByEmail(request.getEmail())) {
            throw new BusinessException("EMAIL_EXISTS", "Email 已存在 " + request.getEmail());
        }
        if (!entity.getUsername().equals(request.getUsername()) && repository.existsByUsername(request.getUsername())) {
            throw new BusinessException("USERNAME_EXISTS", "帳號已存在 " + request.getUsername());
        }

        memberConverter.updateEntityFromRequest(request, entity);
        if (StringUtils.hasText(request.getPassword())) {
            entity.setPassword(PasswordUtil.encode(request.getPassword()));
        }
        MemberEntity updated = save(entity);
        return memberConverter.toDto(updated);
    }

    @Transactional
    public void deleteMember(Long id) {
        deleteById(id);
    }
}
