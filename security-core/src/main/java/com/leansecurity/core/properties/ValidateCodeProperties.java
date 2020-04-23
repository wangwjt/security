package com.leansecurity.core.properties;

/**
 * 验证码类
 * 包含图片验证码和短信验证码
 * @author wangjiantao
 * @date 2020/4/23 15:52
 */
public class ValidateCodeProperties {

    // 图片验证码
    private ImageCodeProperties image = new ImageCodeProperties();

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}
