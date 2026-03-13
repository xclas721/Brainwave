package com.brainwave.service.identity.template.request;

/**
 * Identity 更新請求模板（不含密碼）。
 */
public class IdentityUpdateRequestTemplate {

    private String account;
    private String displayName;
    private String email;
    private String phone;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
