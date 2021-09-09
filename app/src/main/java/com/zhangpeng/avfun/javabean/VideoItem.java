package com.zhangpeng.avfun.javabean;

import androidx.room.TypeConverter;

import java.io.Serializable;
import java.util.Date;

/**
 * "vid": 4371,
 *         "size": "[FHD MP4/4.4GB]",
 *         "imageurl": "http://192.168.0.109:9000/image/df86282.jpg",
 *         "docUrl": "https://aa1805.com/df/23781.html",
 *         "category": "censored",
 *         "": "dasd655 物腰柔爆乳妻信頼上司寝取種付 永井瑪利亞[VIP1196]",
 *         "createDate": 1604419200000,
 *         "playUrl": "https://m3u8.cdnpan.com/sZnJ53Tf.m3u8"
 */
public class VideoItem implements Serializable {
    private int vid;
    private String size;
    private String imageurl;
    private String docUrl;
    private String category;
    private String videoTittle;
    private Date createDate;
    private String playUrl;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVideoTittle() {
        return videoTittle;
    }

    public void setVideoTittle(String videoTittle) {
        this.videoTittle = videoTittle;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "vid=" + vid +
                ", size='" + size + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", docUrl='" + docUrl + '\'' +
                ", category='" + category + '\'' +
                ", videoTittle='" + videoTittle + '\'' +
                ", createDate=" + createDate +
                ", playUrl='" + playUrl + '\'' +
                '}';
    }
}
