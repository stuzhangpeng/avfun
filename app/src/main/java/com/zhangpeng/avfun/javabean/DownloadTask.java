package com.zhangpeng.avfun.javabean;
import jaygoo.library.m3u8downloader.bean.M3U8Task;
/**
 * 下载任务实体
 */
public class DownloadTask {
    private M3U8Task m3U8Task;
    private VideoItem videoItem;

    public DownloadTask() {
    }

    public M3U8Task getM3U8Task() {
        return m3U8Task;
    }

    public void setM3U8Task(M3U8Task m3U8Task) {
        this.m3U8Task = m3U8Task;
    }

    public VideoItem getVideoItem() {
        return videoItem;
    }

    public void setVideoItem(VideoItem videoItem) {
        this.videoItem = videoItem;
    }

    public DownloadTask(M3U8Task m3U8Task, VideoItem videoItem) {
        this.m3U8Task = m3U8Task;
        this.videoItem = videoItem;
    }
}
