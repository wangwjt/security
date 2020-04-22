package com.leansecurity.browser;

import com.leansecurity.browser.authentication.AuthenticationSuccess;
import com.leansecurity.core.properties.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author wangjiantao
 * @date 2020/4/21 15:43
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    // 配置类
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    @Autowired
    private AuthenticationSuccess authenticationSuccess;

    /**
     * 密码加密配置
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 任何请求都需要身份认证
        http.formLogin()// 表单登陆
                .loginPage("/authentication/require")// 跳转到处理登录页面的controller Url
                .loginProcessingUrl("/authentication/form")// 配置登录表单提交的URL
                .successHandler(authenticationSuccess)// 设置登录成功处理器使用自己配的
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/authentication/require", securityCoreProperties.getBrowser().getLoginPage())
                .permitAll()// 配置'login.html'和'配置的登录页面'不需要身份认证
                .anyRequest()// 任何请求
                .authenticated()//身份认证
                .and()
                .csrf().disable();//暂时关掉csrf
    }
}
