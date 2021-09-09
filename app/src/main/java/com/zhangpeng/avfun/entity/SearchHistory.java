package com.zhangpeng.avfun.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SearchHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "keyword")
    private String keyword;
    @ColumnInfo(name = "time")
    private Long time;
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SearchHistory(String keyword, Long time) {
        this.keyword = keyword;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", time=" + time +
                '}';
    }
}
