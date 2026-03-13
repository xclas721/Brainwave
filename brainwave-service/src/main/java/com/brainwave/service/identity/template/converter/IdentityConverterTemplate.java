package com.brainwave.service.identity.template.converter;

import com.brainwave.service.identity.template.dto.IdentityDtoTemplate;
import com.brainwave.service.identity.template.entity.IdentityEntityTemplate;
import com.brainwave.service.identity.template.request.IdentityRequestTemplate;
import com.brainwave.service.identity.template.request.IdentityUpdateRequestTemplate;
import com.brainwave.service.identity.template.vo.IdentityVoTemplate;

/**
 * Identity Converter 模板。
 */
public interface IdentityConverterTemplate {

    IdentityEntityTemplate toEntity(IdentityRequestTemplate request);

    IdentityDtoTemplate toDto(IdentityEntityTemplate entity);

    IdentityVoTemplate toVo(IdentityEntityTemplate entity);

    void merge(IdentityUpdateRequestTemplate request, IdentityEntityTemplate entity);
}
