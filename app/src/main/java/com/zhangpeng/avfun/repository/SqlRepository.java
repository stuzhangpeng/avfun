package com.zhangpeng.avfun.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.zhangpeng.avfun.dao.DownloadRecoreListDao;
import com.zhangpeng.avfun.dao.FavouriteListDao;
import com.zhangpeng.avfun.dao.SearchHistoryDao;
import com.zhangpeng.avfun.dao.VideoPlayHistoryDao;
import com.zhangpeng.avfun.database.UserDataBase;
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.entity.FavouriteList;
import com.zhangpeng.avfun.entity.PlayHistory;
import com.zhangpeng.avfun.entity.SearchHistory;

import java.util.List;

/**
 * sql仓库，用于对数据库crud
 */
public class SqlRepository {
    //播放历史
    public static class InsertAsyncTask extends AsyncTask<PlayHistory, Void, Void> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public InsertAsyncTask(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }

        @Override
        protected Void doInBackground(PlayHistory... playHistories) {
            videoPlayHistoryDao.insertPlayHistory(playHistories[0]);
            return null;
        }
    }

    public static class QueryAllAsyncTask extends AsyncTask<Void, Void, List<PlayHistory>> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public QueryAllAsyncTask(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }

        @Override
        protected List<PlayHistory> doInBackground(Void... voids) {
            return videoPlayHistoryDao.queryAllPlayHistory();
        }
    }

    public static class QueryAsyncTask extends AsyncTask<Integer, Void, List<PlayHistory>> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public QueryAsyncTask(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }

        @Override
        protected List<PlayHistory> doInBackground(Integer... ids) {
            return videoPlayHistoryDao.queryPlayHistory(ids[0]);
        }
    }

    public static class UpadteAsyncTask extends AsyncTask<PlayHistory, Void, Void> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public UpadteAsyncTask(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }


        @Override
        protected Void doInBackground(PlayHistory... playHistories) {
            videoPlayHistoryDao.updatePlayHistory(playHistories[0]);
            return null;
        }
    }

    public static class DeleteAllSearchHistory extends AsyncTask<Void, Void, Integer> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public DeleteAllSearchHistory(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return videoPlayHistoryDao.deleteAllPlayHistory();

        }
    }
    public static class DeleteSearchHistoryByVideoTittle extends AsyncTask<String, Void, Void> {
        private VideoPlayHistoryDao videoPlayHistoryDao;

        public DeleteSearchHistoryByVideoTittle(VideoPlayHistoryDao videoPlayHistoryDao) {
            this.videoPlayHistoryDao = videoPlayHistoryDao;
        }

        @Override
        protected Void doInBackground(String ... voids) {
             videoPlayHistoryDao.deletePlayHistoryByVideoTittle(voids[0]);
             return null;
        }
    }

    public static VideoPlayHistoryDao getPlayHistoryDao(Context context) {
        UserDataBase database = UserDataBase.getInstance(context);
        return database.getVideoPlayHistoryDao();
    }

    public static FavouriteListDao getFavouriteListDao(Context context) {
        UserDataBase database = UserDataBase.getInstance(context);
        return database.getFavouriteListDao();
    }

    public static SearchHistoryDao getSearchHistoryDao(Context context) {
        UserDataBase database = UserDataBase.getInstance(context);
        return database.getSearchHistoryDao();
    }

    public static DownloadRecoreListDao getDownloadRecoreListDao(Context context) {
        UserDataBase database = UserDataBase.getInstance(context);
        return database.getDownLoadRecordDao();
    }

    //搜索历史
    public static class SearchHistoryInsertAsyncTask extends AsyncTask<SearchHistory, Void, Void> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryInsertAsyncTask(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }

        @Override
        protected Void doInBackground(SearchHistory... searchHistories) {
            searchHistoryDao.insertSearchHistory(searchHistories[0]);
            return null;
        }
    }

    //搜索历史
    public static class SearchHistoryQueryAllAsyncTask extends AsyncTask<Void, Void, List<SearchHistory>> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryQueryAllAsyncTask(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }


        @Override
        protected List<SearchHistory> doInBackground(Void... voids) {
            return searchHistoryDao.queryAllSearchHistory();
        }
    }

    public static class SearchHistoryQueryAsyncTask extends AsyncTask<String, Void, List<SearchHistory>> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryQueryAsyncTask(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }

        @Override
        protected List<SearchHistory> doInBackground(String... strings) {
            return searchHistoryDao.querySearchHistory(strings[0]);
        }
    }

    public static class SearchHistoryUpadteAsyncTask extends AsyncTask<SearchHistory, Void, Void> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryUpadteAsyncTask(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }

        @Override
        protected Void doInBackground(SearchHistory... searchHistories) {
            searchHistoryDao.updateSearchHistory(searchHistories[0]);
            return null;
        }
    }

    public static class SearchHistoryDeleteAllSearchHistory extends AsyncTask<Void, Void, Void> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryDeleteAllSearchHistory(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchHistoryDao.deleteAllSearchHistory();
            return null;
        }
    }
    public static class SearchHistoryDeleteSearchHistoryByKeyword extends AsyncTask<String, Void, Void> {
        private SearchHistoryDao searchHistoryDao;

        public SearchHistoryDeleteSearchHistoryByKeyword(SearchHistoryDao searchHistoryDao) {
            this.searchHistoryDao = searchHistoryDao;
        }

        @Override
        protected Void doInBackground(String... voids) {
            searchHistoryDao.deleteSearchHistoryByKeyword(voids[0]);
            return null;
        }
    }

    //myfavourite
    public static class FavouriteListQueryAllAsyncTask extends AsyncTask<Void, Void, List<FavouriteList>> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListQueryAllAsyncTask(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }


        @Override
        protected List<FavouriteList> doInBackground(Void... voids) {
            return favouriteListDao.queryAllFavouriteList();
        }
    }

    public static class FavouriteListUpadteAsyncTask extends AsyncTask<FavouriteList, Void, Void> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListUpadteAsyncTask(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }

        @Override
        protected Void doInBackground(FavouriteList... searchHistories) {
            favouriteListDao.updateFavouriteList(searchHistories[0]);
            return null;
        }
    }

    public static class FavouriteListDeleteAllTask extends AsyncTask<Void, Void, Integer> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListDeleteAllTask(FavouriteListDao searchHistoryDao) {
            this.favouriteListDao = searchHistoryDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return favouriteListDao.deleteAllFavouriteList();

        }
    }

    public static class FavouriteListInsertAsyncTask extends AsyncTask<FavouriteList, Void, Void> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListInsertAsyncTask(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }

        @Override
        protected Void doInBackground(FavouriteList... favouriteLists) {
            favouriteListDao.insertFavouriteList(favouriteLists[0]);
            return null;
        }
    }

    public static class FavouriteListQueryAsyncTask extends AsyncTask<Integer, Void, List<FavouriteList>> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListQueryAsyncTask(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }

        @Override
        protected List<FavouriteList> doInBackground(Integer... vids) {
            return favouriteListDao.queryFavouriteList(vids[0]);
        }
    }

    public static class FavouriteListDeleteAsyncTask extends AsyncTask<Integer, Void, Integer> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListDeleteAsyncTask(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return favouriteListDao.deleteFavouriteList(integers[0]);
        }
    }
    public static class FavouriteListDeleteAsyncTaskByTittle extends AsyncTask<String, Void, Void> {
        private FavouriteListDao favouriteListDao;

        public FavouriteListDeleteAsyncTaskByTittle(FavouriteListDao favouriteListDao) {
            this.favouriteListDao = favouriteListDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            favouriteListDao.deleteFavouriteListByTittle(strings[0]);
            return null;
        }
    }

    //downloadrecord
    public static class DownloadRecoreListQueryAllAsyncTask extends AsyncTask<Void, Void, List<DownLoadRecord>> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListQueryAllAsyncTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected List<DownLoadRecord> doInBackground(Void... voids) {
            return downloadRecoreListDao.queryAllDownLoadRecord();
        }
    }

    public static class DownloadRecoreListUpadteAsyncTask extends AsyncTask<DownLoadRecord, Void, Void> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListUpadteAsyncTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected Void doInBackground(DownLoadRecord... downLoadRecords) {
            downloadRecoreListDao.updateDownLoadRecord(downLoadRecords[0]);
            return null;
        }
    }

    public static class DownloadRecoreListDeleteAllTask extends AsyncTask<Void, Void, Integer> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListDeleteAllTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return downloadRecoreListDao.deleteAllDownLoadRecord();

        }
    }
    public static class DownloadRecoreListDeleteTaskByTittle extends AsyncTask<String, Void, Void> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public  DownloadRecoreListDeleteTaskByTittle(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected Void doInBackground(String...tittles) {
           downloadRecoreListDao.deleteDownLoadRecordByTittle(tittles[0]);
            return  null;
        }
    }

    public static class DownloadRecoreListInsertAsyncTask extends AsyncTask<DownLoadRecord, Void, Void> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListInsertAsyncTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected Void doInBackground(DownLoadRecord... downLoadRecords) {
            downloadRecoreListDao.insertDownLoadRecord(downLoadRecords[0]);
            return null;
        }
    }

    public static class DownloadRecoreListQueryAsyncTask extends AsyncTask<Integer, Void, List<DownLoadRecord>> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListQueryAsyncTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected List<DownLoadRecord> doInBackground(Integer... vids) {
            return downloadRecoreListDao.queryDownLoadRecord(vids[0]);
        }
    }

    public static class DownloadRecoreListDeleteAsyncTask extends AsyncTask<Integer, Void, Integer> {
        private DownloadRecoreListDao downloadRecoreListDao;

        public DownloadRecoreListDeleteAsyncTask(DownloadRecoreListDao downloadRecoreListDao) {
            this.downloadRecoreListDao = downloadRecoreListDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return downloadRecoreListDao.deleteDownLoadRecord(integers[0]);
        }
    }
}
