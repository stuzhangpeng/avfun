package com.zhangpeng.avfun.javabean;

import java.io.Serializable;

public class VideoCategoryEntity {
    private String code;
    /**
     * @Description 返回信息描述
     */
    private String message;
    /**
     * @Description消息返回实体
     */

    private PageVideoResult data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageVideoResult getData() {
        return data;
    }

    public void setData(PageVideoResult data) {
        this.data = data;
    }
}
