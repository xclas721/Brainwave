package com.brainwave.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新使用者請求（不含密碼，改密碼另用 API）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "帳號必填")
    private String username;

    @NotBlank(message = "姓名必填")
    private String name;

    @NotBlank(message = "Email 必填")
    @Email(message = "Email 格式不正確")
    private String email;

    private String phone;
}
