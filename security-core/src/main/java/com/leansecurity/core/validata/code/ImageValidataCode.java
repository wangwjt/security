package com.leansecurity.core.validata.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码类
 * @author wangjiantao
 * @date 2020/4/23 9:00
 */
public class ImageValidataCode {

    // 展示的图片
    private BufferedImage image;

    // 随机数(验证码)
    private String code;

    // 过期时间
    private LocalDateTime expireTime;

    public ImageValidataCode(BufferedImage image, String code, int expireTime) {
        this.image = image;
        this.code = code;
        // 过期时间为当前时间点+传入的过期时间(秒)
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
