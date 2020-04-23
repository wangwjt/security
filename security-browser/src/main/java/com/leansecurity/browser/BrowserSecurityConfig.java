package com.leansecurity.browser;

import com.leansecurity.browser.authentication.AuthenticationFailure;
import com.leansecurity.browser.authentication.AuthenticationSuccess;
import com.leansecurity.core.properties.SecurityCoreProperties;
import com.leansecurity.core.validata.code.ImageValidataCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Autowired
    private AuthenticationFailure authenticationFailure;

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

        // 验证码过滤器
        ImageValidataCodeFilter validataCodeFilter = new ImageValidataCodeFilter();
        // 为验证码过滤器配置认证失败处理机制
        validataCodeFilter.setAuthenticationFailureHandler(authenticationFailure);

        // 任何请求都需要身份认证
        // 在'用户名密码认证过滤器'前添加'验证码过滤器'
        http.addFilterBefore(validataCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()// 表单登陆
                .loginPage("/authentication/require")// 跳转到处理登录页面的controller Url
                .loginProcessingUrl("/authentication/form")// 配置登录表单提交的URL
                .successHandler(authenticationSuccess)// 设置登录成功处理器使用自己配的
                .failureHandler(authenticationFailure)// 设置登录失败处理器使用自己配的
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/authentication/require",
                        securityCoreProperties.getBrowser().getLoginPage(),
                        "/code/image")//图形验证码
                .permitAll()// 配置'login.html','配置的登录页面'，'图形验证码'不需要身份认证
                .anyRequest()// 任何请求
                .authenticated()//身份认证
                .and()
                .csrf().disable();//暂时关掉csrf
    }
}
