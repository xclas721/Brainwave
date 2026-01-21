package com.brainwave.service.user.converter;

import com.brainwave.core.converter.BaseConverter;
import com.brainwave.service.user.dto.UserDto;
import com.brainwave.service.user.entity.UserEntity;
import com.brainwave.service.user.request.UserRequest;
import com.brainwave.service.user.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter extends BaseConverter<UserEntity, UserDto, UserVo, UserRequest> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntityFromRequest(UserRequest request);
}
