package com.brainwave.service.systemconfig.entity;

import com.brainwave.core.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigEntity extends BaseEntity {

    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String key;

    @Column(name = "config_value", nullable = false, length = 1000)
    private String value;

    @Column(name = "config_type", nullable = false, length = 50)
    private String type;

    @Column(name = "description", length = 255)
    private String description;
}

