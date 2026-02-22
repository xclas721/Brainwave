package com.brainwave.service.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 會員查詢請求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchRequest {

    private String name;
    private String email;
    private String phone;
}
