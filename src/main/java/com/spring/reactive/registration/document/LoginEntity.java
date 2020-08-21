package com.spring.reactive.registration.document;

public class LoginEntity {
    private String loginUser;
    private String loginPassword;

    public LoginEntity(String loginUser, String loginPassword) {
        this.loginUser = loginUser;
        this.loginPassword = loginPassword;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
