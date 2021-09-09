package com.zhangpeng.avfun.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.zhangpeng.avfun.javabean.VideoItem;
@Entity
public class PlayHistory extends VideoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //视频播放时所在时间，用于排序
    @ColumnInfo(name = "playtime")
    private  Long playtime ;
    //视频播放进度
    @ColumnInfo(name = "playtimeProgess")
    private Long playtimeProgess;

    public Long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Long playtime) {
        this.playtime = playtime;
    }

    public Long getPlaytimeProgess() {
        return playtimeProgess;
    }

    public void setPlaytimeProgess(Long playtimeProgess) {
        this.playtimeProgess = playtimeProgess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayHistory(VideoItem videoItem,Long playtime, Long playtimeProgess) {
        this.setCategory(videoItem.getCategory());
        this.setCreateDate(videoItem.getCreateDate());
        this.setImageurl(videoItem.getImageurl());
        this.setPlayUrl(videoItem.getPlayUrl());
        this.setVid(videoItem.getVid());
        this.setVideoTittle(videoItem.getVideoTittle());
        this.setSize(videoItem.getSize());
        this.setDocUrl(videoItem.getDocUrl());
        this.playtime = playtime;
        this.playtimeProgess = playtimeProgess;
    }

    public PlayHistory() {
    }
}
