package com.leansecurity.core.properties;

import com.leansecurity.core.enums.LoginType;

/**
 * @author wangjiantao
 * @date 2020/4/22 11:18
 */
public class BrowserProperties {

    private String loginPage = "/login.html";// 默认登录页面的路径

    private LoginType loginType = LoginType.JSON;// 默认按json返回

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
