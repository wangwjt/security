package com.leansecurity.core.validata.code;

import com.leansecurity.core.properties.SecurityCoreProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验验证码过滤器
 * InitializingBean接口为bean提供初始化方法
 * 如果bean实现了InitializingBean接口，会自动调用afterPropertiesSet方法
 * @author wangjiantao
 * @date 2020/4/23 10:53
 */
public class ImageValidataCodeFilter extends OncePerRequestFilter implements InitializingBean {
    
    // session key
    private static final String SESSION_KRY = "IMAGE_CODE";
    
    // 认证失败处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    // 存url的集合
    private Set<String> urlSet = new HashSet<>();

    private SecurityCoreProperties securityCoreProperties;

    // 匹配Url的工具类
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 判断是登陆请求才进行验证码校验
        boolean action = false;
        // 循环比较：配置的url能否和请求的url相匹配
        for (String url: urlSet) {
            if (antPathMatcher.match(url, request.getRequestURI())){
                action = true;
            }
        }

        if (action){
            try {
                // 校验验证码
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                // 认证失败，用security认证失败处理机制处理
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 不是登录请求，则跳过，调用后边的过滤器
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     * @param request
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ImageValidataCode codeInSession = (ImageValidataCode)sessionStrategy.getAttribute(request, SESSION_KRY);
        // imageCode:html标签的name值
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
        // 判断request里的验证码是否为空
        if (StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码不能为空");
        }
        // 判断session里的验证码是否为空
        if (codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }
        // 判断session里的验证码是否过期
        if (codeInSession.isExpried()){
            sessionStrategy.removeAttribute(request, SESSION_KRY);
            throw new ValidateCodeException("验证码已过期");
        }
        //
        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, SESSION_KRY);
    }

    /**
     *
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String []urls = StringUtils.split(securityCoreProperties.getCode().getImage().getUrl(),",");
        for (String url : urls) {
            urlSet.add(url);
        }
        urlSet.add("/authentication/form");
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public Set<String> getUrlSet() {
        return urlSet;
    }

    public void setUrlSet(Set<String> urlSet) {
        this.urlSet = urlSet;
    }

    public SecurityCoreProperties getSecurityCoreProperties() {
        return securityCoreProperties;
    }

    public void setSecurityCoreProperties(SecurityCoreProperties securityCoreProperties) {
        this.securityCoreProperties = securityCoreProperties;
    }
}
