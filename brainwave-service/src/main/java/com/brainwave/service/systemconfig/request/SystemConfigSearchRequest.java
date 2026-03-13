package com.brainwave.service.systemconfig.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemConfigSearchRequest {

    String key;
    String type;
}

