package com.leansecurity.browser.authentication;

import com.leansecurity.browser.support.SimpleResponse;
import com.leansecurity.core.enums.LoginType;
import com.leansecurity.core.properties.SecurityCoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangjiantao
 * @date 2020/4/22 15:21
 */
@Component
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    // 日志
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 可将对象转化成json
    private ObjectMapper objectMapper = new ObjectMapper();

    // 配置类
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    /**
     *
     * @param request 请求
     * @param response 相应
     * @param exception 认证失败的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        logger.info("登录失败");
        // 如果配置的是json，就按json返回
        if (LoginType.JSON.equals(securityCoreProperties.getBrowser().getLoginType())){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            // 将认证信息写到response里返回
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));

        } else {
            // 如果配置的是跳转，就按跳转返回
            // 调用父类的方法可实现跳转
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
