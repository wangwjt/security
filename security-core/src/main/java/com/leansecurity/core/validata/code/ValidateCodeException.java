package com.leansecurity.core.validata.code;


import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常
 * AuthenticationException是Security所有认证异常的基类
 * @author wangjiantao
 * @date 2020/4/23 11:08
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
