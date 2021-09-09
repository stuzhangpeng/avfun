package com.zhangpeng.avfun.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zhangpeng.avfun.entity.PlayHistory;

import java.util.List;

@Dao
public interface VideoPlayHistoryDao  {
    @Insert
    public void insertPlayHistory(PlayHistory playHistory);
    @Query("SELECT * FROM PlayHistory ORDER BY playtime DESC")
    public List<PlayHistory> queryAllPlayHistory();
    @Query("SELECT * FROM PlayHistory WHERE vid=:vid")
    public List<PlayHistory> queryPlayHistory(Integer vid);
    @Update
    public void updatePlayHistory(PlayHistory playHistory);
    @Query("DELETE FROM PlayHistory")
    public int deleteAllPlayHistory();
    @Query("DELETE FROM PlayHistory where videoTittle=:tittle")
    public void deletePlayHistoryByVideoTittle(String tittle);
}
