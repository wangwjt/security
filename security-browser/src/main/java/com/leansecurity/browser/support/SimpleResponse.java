package com.leansecurity.browser.support;

/**
 * @author wangjiantao
 * @date 2020/4/22 11:03
 */
public class SimpleResponse {

    private Object content;

    public SimpleResponse(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
