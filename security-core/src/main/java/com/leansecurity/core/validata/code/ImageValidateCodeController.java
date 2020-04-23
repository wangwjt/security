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


    @GetMapping("/code/image")
    public void createImageValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageValidataCode imageValidataCode = createImageCode(request);
        // 将随机数存到session中
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KRY, imageValidataCode);
        // 将图片写到响应输出流里去
        ImageIO.write(imageValidataCode.getImage(), "JPEG", response.getOutputStream());

    }

    /**
     * 创建图片验证码
     * @param request
     * @return
     */
    private ImageValidataCode createImageCode(HttpServletRequest request) {
        // width和height的值借助ServletRequestUtils从request里取，如果取不到就从配置文件里取
        int width = ServletRequestUtils.getIntParameter(request,
                "width",
                securityCoreProperties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(request,
                "hight",
                securityCoreProperties.getCode().getImage().getHight());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        // 验证码长度相关逻辑；同样借助ServletRequestUtils从request里取，如果取不到就从配置文件里取
        for (int i = 0; i < ServletRequestUtils.getIntParameter(request,
                "lenth", securityCoreProperties.getCode().getImage().getLenth()); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 +
                    random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageValidataCode(image, sRand, securityCoreProperties.getCode().getImage().getExpireIn());
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
