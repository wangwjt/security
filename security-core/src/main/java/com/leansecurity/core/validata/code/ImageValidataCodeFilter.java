package com.leansecurity.core.validata.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验验证码过滤器
 * @author wangjiantao
 * @date 2020/4/23 10:53
 */
public class ImageValidataCodeFilter extends OncePerRequestFilter {
    
    // session key
    private static final String SESSION_KRY = "IMAGE_CODE";
    
    // 认证失败处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 判断是登陆请求才进行验证码校验
        if (StringUtils.equals("/authentication/form",request.getRequestURI())
                && StringUtils.equalsIgnoreCase("post", request.getMethod())){
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

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
