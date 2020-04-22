package com.leansecurity.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangjiantao
 * @date 2020/4/22 11:18
 */
@Component
@ConfigurationProperties(prefix = "security.config")//读取配置文件中以security.config开头的
public class SecurityCoreProperties {

    private BrowserProperties browser = new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
