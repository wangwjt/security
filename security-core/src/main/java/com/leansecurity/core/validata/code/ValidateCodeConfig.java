package com.leansecurity.core.validata.code;

import com.leansecurity.core.properties.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置类
 * @author wangjiantao
 * @date 2020/4/24 9:13
 */
@Configuration
public class ValidateCodeConfig {

    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    /**
     * 如果不考虑@ConditionalOnMissingBean(name = "imageCodeGenerator")注解的话，
     * 下面的作用和直接在ImageCodeGenerator类上加@Component注解等同，目的交给容器管理
     * 而@ConditionalOnMissingBean的作用是如果容器中没有imageCodeGenerator这个bean就创建，
     * 如果有就不创建
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityCoreProperties(securityCoreProperties);
        return codeGenerator;
    }

}
