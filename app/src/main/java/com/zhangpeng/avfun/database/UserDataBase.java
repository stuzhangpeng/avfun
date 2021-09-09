package com.zhangpeng.avfun.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zhangpeng.avfun.dao.DownloadRecoreListDao;
import com.zhangpeng.avfun.dao.FavouriteListDao;
import com.zhangpeng.avfun.dao.SearchHistoryDao;
import com.zhangpeng.avfun.dao.VideoPlayHistoryDao;
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.entity.FavouriteList;
import com.zhangpeng.avfun.entity.PlayHistory;
import com.zhangpeng.avfun.entity.SearchHistory;
import com.zhangpeng.avfun.utils.DateToStringConverter;
@Database(entities = {PlayHistory.class, FavouriteList.class, SearchHistory.class, DownLoadRecord.class},version = 1,exportSchema = false)
@TypeConverters(DateToStringConverter.class)
public abstract class UserDataBase extends RoomDatabase {
    private static UserDataBase instance;
    public static synchronized UserDataBase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), UserDataBase.class, "user_database")
                    .build();
        }
        return instance;
    }
    public abstract FavouriteListDao getFavouriteListDao();
    public abstract SearchHistoryDao getSearchHistoryDao();
    public abstract VideoPlayHistoryDao getVideoPlayHistoryDao();
    public abstract DownloadRecoreListDao getDownLoadRecordDao();

}
