package com.zhangpeng.avfun.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zhangpeng.avfun.javabean.VideoItem;

/**
 * 下载记录
 */
@Entity
public class DownLoadRecord extends VideoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long downloadTime;
    private String filepath;
    private String filesize;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }
    public void initData(VideoItem videoItem){
        this.setCategory(videoItem.getCategory());
        this.setCreateDate(videoItem.getCreateDate());
        this.setImageurl(videoItem.getImageurl());
        this.setPlayUrl(videoItem.getPlayUrl());
        this.setVid(videoItem.getVid());
        this.setVideoTittle(videoItem.getVideoTittle());
        this.setSize(videoItem.getSize());
        this.setDocUrl(videoItem.getDocUrl());
    }

    @Override
    public String toString() {
        return "DownLoadRecord{" +
                "id=" + id +
                ", downloadTime=" + downloadTime +
                ", filepath='" + filepath + '\'' +
                ", filesize='" + filesize + '\'' +
                '}';
    }
}
