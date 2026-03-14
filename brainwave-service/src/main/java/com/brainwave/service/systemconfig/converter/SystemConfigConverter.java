package com.brainwave.service.systemconfig.converter;

import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.entity.SystemConfigEntity;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import com.brainwave.service.systemconfig.request.SystemConfigUpdateRequest;
import com.brainwave.service.systemconfig.vo.SystemConfigVo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SystemConfigConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SystemConfigEntity toEntityFromRequest(SystemConfigRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromUpdateRequest(SystemConfigUpdateRequest request, @MappingTarget SystemConfigEntity entity);

    SystemConfigDto toDto(SystemConfigEntity entity);

    SystemConfigVo toVo(SystemConfigEntity entity);

    List<SystemConfigVo> toVoList(List<SystemConfigEntity> entities);
}

