package com.zhangpeng.avfun;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.zhangpeng.avfun.adapter.GuessFavouriteAdapter;
import com.zhangpeng.avfun.dao.FavouriteListDao;
import com.zhangpeng.avfun.dao.VideoPlayHistoryDao;
import com.zhangpeng.avfun.entity.DownLoadRecord;
import com.zhangpeng.avfun.entity.FavouriteList;
import com.zhangpeng.avfun.entity.PlayHistory;
import com.zhangpeng.avfun.fragment.IntroductoryViewModel;
import com.zhangpeng.avfun.javabean.VideoItem;
import com.zhangpeng.avfun.network.HttpUtils;
import com.zhangpeng.avfun.repository.SqlRepository;
import com.zhangpeng.avfun.utils.TimeUtils;
import com.zhangpeng.avfun.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.supercharge.shimmerlayout.ShimmerLayout;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayActivity extends FragmentActivity implements GestureDetector.OnGestureListener {
    //???????????????????????????
    private IntroductoryViewModel mViewModel;
    private Toolbar toolbar;
    private TextView textView;
    private TextView category;
    private ImageView imageView;
    private ShimmerLayout shimmerLayout;
    private CardView cardView;
    private RecyclerView guessFavourite;
    private TabLayout menuTablayout;
    //??????
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final int PERMISSION_CODE = 1;
    //??????????????????????????????
    private GestureDetector gestureDetector;
    private SurfaceView surfaceView;
    private IjkMediaPlayer mPlayer;
    private String url;
    private ProgressBar progressBar;
    private volatile SeekBar seekBar;
    private FrameLayout playerFrame;
    private FrameLayout controlFrame;
    private ImageView imageViewControl;
    private ImageView fullScreen;
    private ConstraintLayout controlHeader;
    private ImageView imageViewBack;
    private TextView timeProgessTextView;
    private TextView videoTittleTextView;
    private String durationString;
    private Thread thread;
    private Handler handler;
    long duration;
    private final int PROGESS_CHANGED = 1;
    private final int VIDEO_PREPARED = 2;
    private final int VIDEO_PLAYFINISHED = 3;
    private final int VIDEO_BUFFERUPDATED = 4;
    private final int VIDEO_SEEKFINISHED = 5;
    private final Long NOTIN_PLAYHISTORY = -1L;
    private AudioManager audioManager;
    private VideoItem videoItem;
    private Long playgroress;
    private PlayHistory playHistory;
    //?????????????????????
    private int maxVolume;
    //?????????????????????
    private int currentVolume;
    private VideoPlayHistoryDao playHistoryDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //??????????????????
        videoItem = (VideoItem) getIntent().getSerializableExtra("videodetail");
        url = videoItem.getPlayUrl();

        //?????????????????????ui
        handler = new Handler((message) -> {
            switch (message.what) {
                case PROGESS_CHANGED:
                    //???????????????
                    updateProgessBar();
                    //??????????????????
                    updatetimeBar();
                    break;
                case VIDEO_PREPARED:
                    //??????????????????
                    imageViewControl.setImageResource(R.drawable.pauseicon);
                    //???????????????
                    seekBar.setMax((int) duration);
                    videoTittleTextView.setText(videoItem.getVideoTittle());
                    //???????????????????????????
                    timeProgessTextView.setText(durationString);
                    startCountProgessBar();
                    break;
                case VIDEO_PLAYFINISHED:
                    updateplayIcon();
                    break;
                case VIDEO_SEEKFINISHED:
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                case VIDEO_BUFFERUPDATED:
                    int percent = message.arg1;
                    seekBar.setSecondaryProgress(seekBar.getMax() * percent / 100);
                    break;
                default:
                    break;
            }
            return true;
        });
        if (url != null) {
            this.url = url;
        }
        gestureDetector = new GestureDetector(this, this);
        //??????????????????
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //??????????????????
        //?????????????????????dao
        playHistoryDao = SqlRepository.getPlayHistoryDao(this);
        playHistory = findPlayHistoryPlaygrogess(videoItem.getVid());
        //?????????ijkplayer
        initPlayer();
        //toolbar
        toolbar = findViewById(R.id.video_play_toolbar);
        View share = toolbar.findViewById(R.id.share);
        View comment = toolbar.findViewById(R.id.comment);
        View see_comment = toolbar.findViewById(R.id.see_comment);
        View support = toolbar.findViewById(R.id.support);
        View favourite = toolbar.findViewById(R.id.favourite);
        View download = toolbar.findViewById(R.id.download);
        share.setOnClickListener((view)->{
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoItem.getImageurl());
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "????????????"));
        });
        comment.setOnClickListener((view)->{});
        see_comment.setOnClickListener((view)->{});
        support.setOnClickListener((view)->{});
        favourite.setOnClickListener((view)->{
            FavouriteList favouriteList = new FavouriteList(videoItem, new Date().getTime());
            FavouriteListDao favouriteListDao = SqlRepository.getFavouriteListDao(VideoPlayActivity.this);
            SqlRepository.FavouriteListInsertAsyncTask favouriteListInsertAsyncTask = new SqlRepository.FavouriteListInsertAsyncTask(favouriteListDao);
            AsyncTask<FavouriteList, Void, Void> execute = favouriteListInsertAsyncTask.execute(favouriteList);
            try {
                execute.get();
               // tab.setText("?????????");
                Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_LONG).show();
            } catch (Exception exception) {
                Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_LONG).show();
            }
        });
        download.setOnClickListener((view)->{
            requestAppPermissions();
        });
        videoTittleTextView = findViewById(R.id.textView_videitittle);
        surfaceView = findViewById(R.id.surfaceView);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        playerFrame = findViewById(R.id.playerframe);
        controlFrame = findViewById(R.id.controllerframe);
        imageViewControl = findViewById(R.id.imageView_play);
        controlHeader = findViewById(R.id.controlheader);
        fullScreen = findViewById(R.id.imageView7);
        imageViewBack = findViewById(R.id.imageView_back);
        timeProgessTextView = findViewById(R.id.timecontroltextview);
        //??????????????????????????????
        imageViewBack.setOnClickListener((view) -> {
            //??????????????????????????????
            //??????????????????????????????????????????activity
            //??????????????????
            Configuration cf = VideoPlayActivity.this.getResources().getConfiguration(); //???????????????????????????
            int ori = cf.orientation; //??????????????????
            if (ori == cf.ORIENTATION_LANDSCAPE) {
                toolbar.setVisibility(View.VISIBLE);
                //???????????????
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                //????????????
                finish();
            }
        });
        //?????????????????????
        imageViewControl.setOnClickListener((view) -> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                imageViewControl.setImageResource(R.drawable.playicon);
            } else {
                mPlayer.start();
                imageViewControl.setImageResource(R.drawable.pauseicon);
            }
        });
        //??????????????????
        fullScreen.setOnClickListener((view) -> setUI());
        //???????????????
        controlFrame.setVisibility(View.INVISIBLE);
        controlHeader.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.seekTo(progress);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //??????????????????????????????????????????
        playerFrame.setOnClickListener((view) -> {
            if (controlFrame.getVisibility() == View.INVISIBLE && controlHeader.getVisibility() == View.INVISIBLE) {
                controlFrame.setVisibility(View.VISIBLE);
                controlHeader.setVisibility(View.VISIBLE);
            } else {
                controlFrame.setVisibility(View.INVISIBLE);
                controlHeader.setVisibility(View.INVISIBLE);
            }
        });
        //surfaceView???????????????
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                //????????????
                mPlayer.setDisplay(holder);
                //?????????????????????
                mPlayer.setScreenOnWhilePlaying(true);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
        //???????????????????????????
        mViewModel = new ViewModelProvider(this).get(IntroductoryViewModel.class);
        textView = findViewById(R.id.videotittle);
        imageView = findViewById(R.id.videopicture);
        shimmerLayout = findViewById(R.id.shimerlayout2);
        cardView = findViewById(R.id.cardview);
        shimmerLayout.setShimmerColor(0x55FFFFFF);
        shimmerLayout.setShimmerAngle(0);
        shimmerLayout.startShimmerAnimation();
        Glide.with(cardView).load(videoItem.getImageurl()).placeholder(R.drawable.placeholderimage)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //????????????
                        if (shimmerLayout != null) {
                            shimmerLayout.stopShimmerAnimation();
                        }
                        return false;
                    }
                })
                .into(imageView);
        textView.setText(videoItem.getVideoTittle());
/*        menuTablayout = findViewById(R.id.menutablayout);
        menuTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String text = (String) tab.getText();
                switch (text) {
                    case "??????":
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, videoItem.getImageurl());
                        shareIntent.setType("image/jpeg");
                        startActivity(Intent.createChooser(shareIntent, "????????????"));
                        break;
                    case "??????":
                        FavouriteList favouriteList = new FavouriteList(videoItem, new Date().getTime());
                        FavouriteListDao favouriteListDao = SqlRepository.getFavouriteListDao(VideoPlayActivity.this);
                        SqlRepository.FavouriteListInsertAsyncTask favouriteListInsertAsyncTask = new SqlRepository.FavouriteListInsertAsyncTask(favouriteListDao);
                        AsyncTask<FavouriteList, Void, Void> execute = favouriteListInsertAsyncTask.execute(favouriteList);
                        try {
                            execute.get();
                            tab.setText("?????????");
                            Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_LONG).show();
                        } catch (Exception exception) {
                            Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "?????????":
                        //????????????
                        FavouriteListDao dao = SqlRepository.getFavouriteListDao(VideoPlayActivity.this);
                        SqlRepository.FavouriteListDeleteAsyncTask favouriteListDeleteAsyncTask = new SqlRepository.FavouriteListDeleteAsyncTask(dao);
                        AsyncTask<Integer, Void, Integer> result = favouriteListDeleteAsyncTask.execute(videoItem.getVid());
                        try {
                            result.get();
                            tab.setText("??????");
                            Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                        } catch (Exception exception) {
                            Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "??????":
                        //??????
                        requestAppPermissions();
                        break;
                    case "?????????":
                        //??????
                        Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String text = (String) tab.getText();
                switch (text) {
                    case "??????":
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, videoItem.getImageurl());
                        startActivity(Intent.createChooser(shareIntent, "????????????"));
                        break;
                    case "??????":
                        FavouriteList favouriteList = new FavouriteList(videoItem, new Date().getTime());
                        FavouriteListDao favouriteListDao = SqlRepository.getFavouriteListDao(VideoPlayActivity.this);
                        SqlRepository.FavouriteListInsertAsyncTask favouriteListInsertAsyncTask = new SqlRepository.FavouriteListInsertAsyncTask(favouriteListDao);
                        AsyncTask<FavouriteList, Void, Void> execute = favouriteListInsertAsyncTask.execute(favouriteList);
                        try {
                            execute.get();
                            tab.setText("?????????");
                            Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        } catch (Exception exception) {
                            Toast.makeText(VideoPlayActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "?????????":
                        //????????????
                        FavouriteListDao dao = SqlRepository.getFavouriteListDao(VideoPlayActivity.this);
                        SqlRepository.FavouriteListDeleteAsyncTask favouriteListDeleteAsyncTask = new SqlRepository.FavouriteListDeleteAsyncTask(dao);
                        AsyncTask<Integer, Void, Integer> result = favouriteListDeleteAsyncTask.execute(videoItem.getVid());
                        try {
                            result.get();
                            tab.setText("??????");
                            Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                        } catch (Exception exception) {
                            Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "??????":
                        //??????
                        requestAppPermissions();
                        break;
                    case "?????????":
                        //??????
                        Toast.makeText(VideoPlayActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                ;

            }
        });*/
        category = findViewById(R.id.textview_category);
        StringBuilder stringBuilder = new StringBuilder();
        String formart = TimeUtils.formartTimes(videoItem.getCreateDate(), "YYYY-MM-dd");
        stringBuilder.append(formart).append("/").append(videoItem.getCategory());
        category.setText(stringBuilder.toString());
        guessFavourite = findViewById(R.id.guessfavourite);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoPlayActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        guessFavourite.setLayoutManager(linearLayoutManager);
        GuessFavouriteAdapter adapter = new GuessFavouriteAdapter(VideoPlayActivity.this, new GuessFavouriteAdapter.CallBack() {
            @Override
            public void callback(VideoItem videoItem) {
                Intent intent = new Intent(VideoPlayActivity.this, VideoPlayActivity.class);
                intent.putExtra("videodetail", videoItem);
                startActivity(intent);
            }
        });
        guessFavourite.setAdapter(adapter);
        //?????????????????????
        FavouriteListDao favouriteListDao = SqlRepository.getFavouriteListDao(this);
        SqlRepository.FavouriteListQueryAsyncTask favouriteListQueryAsyncTask = new SqlRepository.FavouriteListQueryAsyncTask(favouriteListDao);
        AsyncTask<Integer, Void, List<FavouriteList>> execute = favouriteListQueryAsyncTask.execute(videoItem.getVid());
        try {
            List<FavouriteList> favouriteLists = execute.get(3L, TimeUnit.SECONDS);
            if (favouriteLists != null && favouriteLists.size() > 0) {
                //?????????
                TabLayout.Tab tabAt = menuTablayout.getTabAt(0);
                tabAt.setText("?????????");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        mViewModel.getListLiveData().observe(this, new Observer<List<VideoItem>>() {
            @Override
            public void onChanged(List<VideoItem> videoItems) {
                adapter.submitList(videoItems);
            }
        });
        //??????page
        int randompage = new Random().nextInt(20) + 1;
        //???????????????url
        String url = UrlUtils.getUrlByType(UrlUtils.RequireType.category, videoItem.getCategory(), 10, randompage);
        if (mViewModel.getListLiveData().getValue() == null) {
            //????????????
            mViewModel.requestData(this, url, HttpUtils.RequestType.GET);
        }
    }

    private void initPlayer() {
        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
        }
        try {
            if (videoItem instanceof DownLoadRecord) {
                DownLoadRecord downLoadRecord = (DownLoadRecord) videoItem;
                mPlayer.setDataSource(downLoadRecord.getFilepath() + File.separator + downLoadRecord.getVideoTittle() + ".ts");
                System.out.println("filepath:________________________" + downLoadRecord.getFilepath() + File.separator + downLoadRecord.getVideoTittle() + ".ts");
            } else {
                mPlayer.setDataSource(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //????????????
        mPlayer.prepareAsync();
        //???????????????
        mPlayer.setOnPreparedListener((iMediaPlayer) -> {
            if (playHistory != null) {
                iMediaPlayer.seekTo(playHistory.getPlaytimeProgess());
            } else {
                //?????????????????????
                progressBar.setVisibility(View.INVISIBLE);
            }
            //????????????
            iMediaPlayer.setVolume(0, 0);
            //????????????
            iMediaPlayer.setLooping(true);
            //??????????????????
            duration = iMediaPlayer.getDuration();
            durationString = TimeUtils.parseMillTime(duration);
            //????????????
            iMediaPlayer.start();
            //???????????????????????????ui
            Message message = new Message();
            message.what = VIDEO_PREPARED;
            handler.sendMessage(message);

        });
        //?????????????????????
        mPlayer.setOnBufferingUpdateListener((iMediaPlayer, percent) ->
                {
                    //?????????????????????
                    Message message = new Message();
                    message.what = VIDEO_BUFFERUPDATED;
                    message.arg1 = percent;
                    handler.sendMessage(message);
                }
        );
        mPlayer.setOnSeekCompleteListener((iMediaPlayer) -> {
                    //????????????????????????????????????
                    Message message = new Message();
                    message.what = VIDEO_SEEKFINISHED;
                    handler.sendMessage(message);

                }
        );
        //????????????
        mPlayer.setOnCompletionListener((iMediaPlayer) -> {
            Message message = new Message();
            message.what = VIDEO_PLAYFINISHED;
            handler.sendMessage(message);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((!mPlayer.isPlaying()) && mPlayer.isPlayable()) {
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //????????????
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //??????????????????????????????
        relaseResource();
    }

    public void relaseResource() {
        if (thread != null) {
            thread.interrupt();
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        //??????mediaplayer
        mPlayer.stop();
        //??????????????????
        if (mPlayer.getCurrentPosition() >= 1000) {
            insertOrUpdatePlayHistory(mPlayer.getCurrentPosition());
        }
        //????????????
        mPlayer.release();
        mPlayer = null;
    }

    public void setUI() {
        //??????????????????
        Configuration cf = VideoPlayActivity.this.getResources().getConfiguration(); //???????????????????????????
        int ori = cf.orientation; //??????????????????
        if (ori == cf.ORIENTATION_LANDSCAPE) {
            //???????????????
            toolbar.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            //???????????????
            toolbar.setVisibility(View.INVISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //???????????????
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //??????????????????
            mPlayer.setVolume(0.5f, 1f);
        }
    }

    //????????????????????????
    public void startCountProgessBar() {
        thread = new Thread(() -> {
            //???????????????
            while (!Thread.currentThread().isInterrupted()) {
                if (mPlayer.getCurrentPosition() == mPlayer.getDuration()) {
                    return;
                }
                //??????????????????????????????
                Message message = new Message();
                message.what = PROGESS_CHANGED;
                handler.sendMessage(message);
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //??????????????????????????????
                    Thread.currentThread().interrupt();
                }

            }
        });
        //????????????
        thread.start();
    }

    //????????????????????????
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    //???????????????
    public void updateProgessBar() {
        seekBar.setProgress((int) mPlayer.getCurrentPosition());
    }

    //???????????????????????????
    public void updateplayIcon() {
        imageViewControl.setImageResource(R.drawable.loop);
    }

    //??????????????????
    public void updatetimeBar() {
        long currentPosition = mPlayer.getCurrentPosition();
        String currentTime = TimeUtils.parseMillTime(currentPosition);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTime).append("/").append(durationString);
        timeProgessTextView.setText(stringBuilder.toString());
    }

    //????????????
    public void updateVolume() {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * ????????????????????????
     *
     * @return ??????????????????????????????????????????-1L
     */
    public PlayHistory findPlayHistoryPlaygrogess(Integer vid) {
        SqlRepository.QueryAsyncTask queryAsyncTask = new SqlRepository.QueryAsyncTask(playHistoryDao);
        AsyncTask<Integer, Void, List<PlayHistory>> execute = queryAsyncTask.execute(vid);
        try {
            List<PlayHistory> playHistories = execute.get();
            if (playHistories != null && playHistories.size() > 0) {
                //???????????????
                return playHistories.get(0);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //??????????????????????????????
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //????????????
                menuTablayout.getTabAt(1).setText("?????????");
                Intent intent = new Intent(this, DownloadTaskListActivity.class);
                intent.putExtra("videodetail", videoItem);
                startActivity(intent);
            } else {
                Toast.makeText(this, "????????????,?????????????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ???????????????????????????
     */

    public void insertOrUpdatePlayHistory(Long playTimeGrogess) {
        if (playHistory == null) {
            //??????????????????,??????????????????
            SqlRepository.InsertAsyncTask insertAsyncTask = new SqlRepository.InsertAsyncTask(playHistoryDao);
            PlayHistory playHistory = new PlayHistory(videoItem, new Date().getTime(), playTimeGrogess);
            //????????????
            insertAsyncTask.execute(playHistory);
        } else {
            //??????????????????
            playHistory.setPlaytime(new Date().getTime());
            playHistory.setPlaytimeProgess(playTimeGrogess);
            SqlRepository.UpadteAsyncTask upadteAsyncTask = new SqlRepository.UpadteAsyncTask(playHistoryDao);
            upadteAsyncTask.execute(playHistory);
        }
    }

    private void requestAppPermissions() {
        //api29?????????????????????????????????????????????
        if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS, PERMISSION_CODE);
        } else {
            menuTablayout.getTabAt(1).setText("?????????");
            Intent intent = new Intent(this, DownloadTaskListActivity.class);
            intent.putExtra("videodetail", videoItem);
            startActivity(intent);
        }
    }
}