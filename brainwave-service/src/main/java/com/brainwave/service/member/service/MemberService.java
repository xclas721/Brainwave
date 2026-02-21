package com.brainwave.service.member.service;

import com.brainwave.core.base.BaseServiceImpl;
import com.brainwave.core.exception.BusinessException;
import com.brainwave.core.utils.PasswordUtil;
import com.brainwave.service.member.converter.MemberConverter;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.entity.MemberEntity;
import com.brainwave.service.member.repository.MemberRepository;
import com.brainwave.service.member.request.MemberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 加密密碼
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
}
