package com.brainwave.service.systemconfig.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemConfigDto {

    Long id;
    String key;
    String value;
    String type;
    String description;
}

