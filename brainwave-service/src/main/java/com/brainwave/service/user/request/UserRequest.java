package com.brainwave.service.user.request;

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
public class UserRequest {

    @NotBlank(message = "姓名必填有誤")
    private String name;

    @NotBlank(message = "Email 必填有誤")
    @Email(message = "Email 格式不正確")
    private String email;

    private String phone;
}
