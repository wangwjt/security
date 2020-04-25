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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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

    // 数据源
    // 数据源的信息是置在置文件里的
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 密码加密配置
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     *  配置TokenRepository
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 设置数据源
        tokenRepository.setDataSource(dataSource);
        // 创建表
//        tokenRepository.setCreateTableOnStartup(true);// 第一次启动后要注释掉，
                              // 要不每次运行都创建表，但是DB中已经存在，会报错
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 验证码过滤器
        ImageValidataCodeFilter validataCodeFilter = new ImageValidataCodeFilter();
        // 为验证码过滤器配置认证失败处理机制
        validataCodeFilter.setAuthenticationFailureHandler(authenticationFailure);
        // 配置传进去
        validataCodeFilter.setSecurityCoreProperties(securityCoreProperties);
        // 调初始化方法
        validataCodeFilter.afterPropertiesSet();

        // 任何请求都需要身份认证
        // 在'用户名密码认证过滤器'前添加'验证码过滤器'
        http.addFilterBefore(validataCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()// 表单登陆
                    .loginPage("/authentication/require")// 跳转到处理登录页面的controller Url
                    .loginProcessingUrl("/authentication/form")// 配置登录表单提交的URL
                    .successHandler(authenticationSuccess)// 设置登录成功处理器使用自己配的
                    .failureHandler(authenticationFailure)// 设置登录失败处理器使用自己配的
                .and()
                    .rememberMe()// “记住我”的相关配置
                    .tokenRepository(persistentTokenRepository())// 配置tokenRepository
                    .tokenValiditySeconds(securityCoreProperties.getBrowser().getRememberMeSeconds())//配置过期秒数
                    .userDetailsService(userDetailsService)// 拿到用户名后用他作登陆
                .and()
                .sessionManagement()
                    .invalidSessionUrl("/session/invalid")// session失效后跳转的地址
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/authentication/require",
                        securityCoreProperties.getBrowser().getLoginPage(),
                        "/code/image",//图形验证码
                        "/session/invalid")// session路径
                .permitAll()// 配置'login.html','配置的登录页面'，'图形验证码'不需要身份认证
                .anyRequest()// 任何请求
                .authenticated()//身份认证
                .and()
                .csrf().disable();//暂时关掉csrf
    }
}
