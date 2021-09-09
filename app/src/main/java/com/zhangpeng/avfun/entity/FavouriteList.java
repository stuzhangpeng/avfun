package com.zhangpeng.avfun.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.zhangpeng.avfun.javabean.VideoItem;
@Entity
public class FavouriteList extends VideoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**
     * 收藏世间
     */
    @ColumnInfo(name = "favouritetime")
    private Long favouriteTime;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Long getFavouriteTime() {
        return favouriteTime;
    }
    public FavouriteList(VideoItem videoItem,Long favouriteTime) {
        this.setCategory(videoItem.getCategory());
        this.setCreateDate(videoItem.getCreateDate());
        this.setImageurl(videoItem.getImageurl());
        this.setPlayUrl(videoItem.getPlayUrl());
        this.setVid(videoItem.getVid());
        this.setVideoTittle(videoItem.getVideoTittle());
        this.setSize(videoItem.getSize());
        this.setDocUrl(videoItem.getDocUrl());
        this.favouriteTime=favouriteTime;
    }

    public FavouriteList() {
    }

    public void setFavouriteTime(Long favouriteTime) {
        this.favouriteTime = favouriteTime;
    }
}
