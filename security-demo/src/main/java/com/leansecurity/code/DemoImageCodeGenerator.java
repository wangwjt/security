package com.leansecurity.code;

import com.leansecurity.core.validata.code.ImageValidataCode;
import com.leansecurity.core.validata.code.ValidateCodeGenerator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjiantao
 * @date 2020/4/24 9:55
 */
@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageValidataCode createImageCode(HttpServletRequest request) {
        System.out.println("更高级的图形验证码生成");
        return null;
    }
}
