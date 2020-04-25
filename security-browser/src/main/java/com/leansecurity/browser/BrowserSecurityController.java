package com.leansecurity.browser;

import com.leansecurity.browser.support.SimpleResponse;
import com.leansecurity.core.properties.SecurityCoreProperties;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangjiantao
 * @date 2020/4/22 10:37
 */
@RestController
public class BrowserSecurityController {

    // 拿到引发跳转的请求
    private RequestCache requestCache = new HttpSessionRequestCache();

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 重定向
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // 配置类
    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    /**
     * 当需要身份验证时跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)// 返回状态码
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 拿到引发跳转的请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = null;
        if (savedRequest == null){
            return null;
        }
        targetUrl = savedRequest.getRedirectUrl();
        logger.info("引发跳转的请求:" + targetUrl);
        // 判断引发跳转的是否是html逻辑
        // 判断是否以html结尾
        if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")){
            // 跳转到登陆页
            redirectStrategy.sendRedirect(request, response, securityCoreProperties.getBrowser().getLoginPage());
        }
        return new SimpleResponse("访问的服务需要身份验证，需要引导用户到登陆页");
    }

    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)// 设置状态码401
    public SimpleResponse session(){
        String msg = "session失效";
        return new SimpleResponse(msg);
    }
}
