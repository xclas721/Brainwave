package com.brainwave.service.systemconfig.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemConfigRequest {

    @NotBlank
    @Size(max = 100)
    private String key;

    @NotBlank
    @Size(max = 1000)
    private String value;

    @NotBlank
    @Size(max = 50)
    private String type;

    @Size(max = 255)
    private String description;
}

