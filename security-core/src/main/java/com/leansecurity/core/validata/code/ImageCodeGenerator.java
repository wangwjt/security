package com.leansecurity.core.validata.code;

import com.leansecurity.core.properties.SecurityCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author wangjiantao
 * @date 2020/4/24 9:03
 */
public class ImageCodeGenerator implements ValidateCodeGenerator{

    @Autowired
    private SecurityCoreProperties securityCoreProperties;

    @Override
    public ImageValidataCode createImageCode(HttpServletRequest request) {
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

    public SecurityCoreProperties getSecurityCoreProperties() {
        return securityCoreProperties;
    }

    public void setSecurityCoreProperties(SecurityCoreProperties securityCoreProperties) {
        this.securityCoreProperties = securityCoreProperties;
    }
}
