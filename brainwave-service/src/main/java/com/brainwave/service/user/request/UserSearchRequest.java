package com.brainwave.service.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 使用者查詢請求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {

    /**
     * 姓名（模糊查詢）
     */
    private String name;

    /**
     * Email（模糊查詢）
     */
    private String email;

    /**
     * 電話（模糊查詢）
     */
    private String phone;
}
