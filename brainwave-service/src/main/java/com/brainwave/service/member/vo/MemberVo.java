package com.brainwave.service.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
}
