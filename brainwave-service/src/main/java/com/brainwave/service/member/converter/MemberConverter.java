package com.brainwave.service.member.converter;

import com.brainwave.core.converter.BaseConverter;
import com.brainwave.service.member.dto.MemberDto;
import com.brainwave.service.member.entity.MemberEntity;
import com.brainwave.service.member.request.MemberRequest;
import com.brainwave.service.member.vo.MemberVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberConverter extends BaseConverter<MemberEntity, MemberDto, MemberVo, MemberRequest> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MemberEntity toEntityFromRequest(MemberRequest request);
}
