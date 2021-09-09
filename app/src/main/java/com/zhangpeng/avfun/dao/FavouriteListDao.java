package com.zhangpeng.avfun.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zhangpeng.avfun.entity.FavouriteList;
import com.zhangpeng.avfun.entity.PlayHistory;
import java.util.List;
@Dao
public interface FavouriteListDao {
    @Insert
    public void insertFavouriteList(FavouriteList favouriteList);
    @Query("SELECT * FROM favouritelist ORDER BY favouritetime DESC")
    public List<FavouriteList> queryAllFavouriteList();
    @Update
    public void updateFavouriteList(FavouriteList favouriteList);
    @Query("DELETE FROM favouritelist")
    public int deleteAllFavouriteList();
    @Query("DELETE FROM favouritelist where vid=:vid")
    public int deleteFavouriteList(Integer vid);
    @Query("SELECT * FROM favouritelist WHERE vid=:vid")
    public List<FavouriteList> queryFavouriteList(Integer vid);
    @Query("DELETE FROM favouritelist where videoTittle=:tittle")
    public int deleteFavouriteListByTittle(String tittle);
}
