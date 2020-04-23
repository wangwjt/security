package com.leansecurity.core.properties;

/**
 * 图形验证码配置参数
 * @author wangjiantao
 * @date 2020/4/23 15:48
 */
public class ImageCodeProperties {

    // 默认宽度
    private int width = 67;
    // 默认高度
    private int hight = 23;
    // 默认验证码长度
    private int lenth = 4;
    // 默认过期时间
    private int expireIn = 60;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public int getLenth() {
        return lenth;
    }

    public void setLenth(int lenth) {
        this.lenth = lenth;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}
