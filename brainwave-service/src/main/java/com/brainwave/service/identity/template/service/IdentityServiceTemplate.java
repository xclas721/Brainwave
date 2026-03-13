package com.brainwave.service.identity.template.service;

import com.brainwave.service.identity.template.dto.IdentityDtoTemplate;
import com.brainwave.service.identity.template.request.IdentityRequestTemplate;
import com.brainwave.service.identity.template.request.IdentitySearchRequestTemplate;
import com.brainwave.service.identity.template.request.IdentityUpdateRequestTemplate;
import com.brainwave.service.identity.template.vo.IdentityVoTemplate;
import java.util.List;

/**
 * Identity Service 模板。
 */
public interface IdentityServiceTemplate {

    IdentityDtoTemplate create(IdentityRequestTemplate request);

    IdentityDtoTemplate update(Long id, IdentityUpdateRequestTemplate request);

    IdentityVoTemplate getById(Long id);

    List<IdentityVoTemplate> search(IdentitySearchRequestTemplate request);
}
