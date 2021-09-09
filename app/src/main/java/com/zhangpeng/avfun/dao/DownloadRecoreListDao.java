package com.zhangpeng.avfun.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zhangpeng.avfun.entity.DownLoadRecord;

import java.util.List;
@Dao
public interface DownloadRecoreListDao {
    @Insert
    public void insertDownLoadRecord(DownLoadRecord downLoadRecord);
    @Query("SELECT * FROM DOWNLOADRECORD ORDER BY downloadTime DESC")
    public List<DownLoadRecord> queryAllDownLoadRecord();
    @Update
    public void updateDownLoadRecord(DownLoadRecord downLoadRecord);
    @Query("DELETE FROM DOWNLOADRECORD")
    public int deleteAllDownLoadRecord();
    @Query("DELETE FROM DOWNLOADRECORD where vid=:vid")
    public int deleteDownLoadRecord(Integer vid);
    @Query("DELETE FROM DOWNLOADRECORD where videoTittle=:tittle")
    public int deleteDownLoadRecordByTittle(String tittle);
    @Query("SELECT * FROM DOWNLOADRECORD WHERE vid=:vid")
    public List<DownLoadRecord> queryDownLoadRecord(Integer vid);
}
