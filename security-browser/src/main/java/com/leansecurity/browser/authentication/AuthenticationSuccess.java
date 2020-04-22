package com.leansecurity.browser.authentication;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理
 * @author wangjiantao
 * @date 2020/4/22 14:36
 */
@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {

    // 日志
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 可将对象转化成json
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 登录成功
     * @param request 请求
     * @param response 响应
     * @param authentication 认证信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        logger.info("登录成功");
        response.setContentType("application/json;charset=UTF-8");
        // 将认证信息写到response里返回
        response.getWriter().write(objectMapper.writeValueAsString(authentication));
    }
}
