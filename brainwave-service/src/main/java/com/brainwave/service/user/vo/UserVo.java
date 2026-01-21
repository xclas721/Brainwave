package com.brainwave.service.user.vo;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
}
