package com.leansecurity.core.properties;

/**
 * @author wangjiantao
 * @date 2020/4/22 11:18
 */
public class BrowserProperties {

    private String loginPage = "/login.html";// 默认登录页面的路径

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
