package com.leansecurity.core.validata.code;

import com.leansecurity.core.properties.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author wangjiantao
 * @date 2020/4/23 9:10
 */
@RestController
public class ImageValidateCodeController {

    // 操作session的类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    // session key
    private static final String SESSION_KRY = "IMAGE_CODE";

    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createImageValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 根据request拿到图形验证码类
        ImageValidataCode imageValidataCode = imageCodeGenerator.createImageCode(request);
        // 将随机数存到session中
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KRY, imageValidataCode);
        // 将图片写到响应输出流里去
        ImageIO.write(imageValidataCode.getImage(), "JPEG", response.getOutputStream());

    }
}
