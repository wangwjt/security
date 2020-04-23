package com.leansecurity.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件
 * @author wangjiantao
 * @date 2020/4/22 11:18
 */
@Component
@ConfigurationProperties(prefix = "security.config")//读取配置文件中以security.config开头的
public class SecurityCoreProperties {

    private BrowserProperties browser = new BrowserProperties();

    // 验证码
    private ValidateCodeProperties code = new ValidateCodeProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
