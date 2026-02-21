package com.brainwave.service.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    @NotBlank(message = "帳號必填")
    private String username;

    private String password;

    @NotBlank(message = "姓名必填有誤")
    private String name;

    @NotBlank(message = "Email 必填有誤")
    @Email(message = "Email 格式不正確")
    private String email;

    private String phone;
}
