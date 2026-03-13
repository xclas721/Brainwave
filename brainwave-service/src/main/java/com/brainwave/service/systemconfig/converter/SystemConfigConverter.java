package com.brainwave.service.systemconfig.converter;

import com.brainwave.service.systemconfig.dto.SystemConfigDto;
import com.brainwave.service.systemconfig.entity.SystemConfigEntity;
import com.brainwave.service.systemconfig.request.SystemConfigRequest;
import com.brainwave.service.systemconfig.request.SystemConfigUpdateRequest;
import com.brainwave.service.systemconfig.vo.SystemConfigVo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SystemConfigConverter {

    SystemConfigEntity toEntityFromRequest(SystemConfigRequest request);

    void updateEntityFromUpdateRequest(SystemConfigUpdateRequest request, @MappingTarget SystemConfigEntity entity);

    SystemConfigDto toDto(SystemConfigEntity entity);

    SystemConfigVo toVo(SystemConfigEntity entity);

    List<SystemConfigVo> toVoList(List<SystemConfigEntity> entities);
}

