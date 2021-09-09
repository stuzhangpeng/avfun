package com.zhangpeng.avfun.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.zhangpeng.avfun.entity.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao{
    @Insert
    public void insertSearchHistory(SearchHistory searchHistory);
    @Query("SELECT * FROM SEARCHHISTORY ORDER BY time DESC")
    public List<SearchHistory> queryAllSearchHistory();
    @Query("SELECT * FROM SEARCHHISTORY WHERE keyword= :keyword")
    public List<SearchHistory> querySearchHistory(String keyword);
    @Update
    public void updateSearchHistory(SearchHistory searchHistory);
    @Query("DELETE FROM SEARCHHISTORY")
    public void deleteAllSearchHistory();
    @Query("DELETE FROM SEARCHHISTORY where keyword= :keyword")
    public void deleteSearchHistoryByKeyword(String keyword);
}
