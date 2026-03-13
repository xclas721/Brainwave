package com.brainwave.service.systemconfig.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemConfigVo {

    Long id;
    String key;
    String value;
    String type;
    String description;
}

