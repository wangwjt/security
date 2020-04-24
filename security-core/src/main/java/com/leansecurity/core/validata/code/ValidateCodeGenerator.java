package com.leansecurity.core.validata.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 生成验证码的接口
 * @author wangjiantao
 * @date 2020/4/24 9:01
 */
public interface ValidateCodeGenerator {

    /**
     * 生成图形验证码
     * @param request
     * @return
     */
    ImageValidataCode createImageCode(HttpServletRequest request);

}
