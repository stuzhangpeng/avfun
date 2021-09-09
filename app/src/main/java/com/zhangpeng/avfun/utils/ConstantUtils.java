package com.zhangpeng.avfun.utils;

/**
 * 视频类型枚举
 */
public enum ConstantUtils {
    search("keyword"),uncensored("uncensored"),censored("censored"),selfphoto("self-photo"),chinesefont("censored-chinese");
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    ConstantUtils(String type) {
        category=type;
    }

}
