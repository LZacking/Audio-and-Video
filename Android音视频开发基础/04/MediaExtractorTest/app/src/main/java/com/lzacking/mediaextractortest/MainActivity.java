package com.lzacking.mediaextractortest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    Button mExtractorVideo;
    Button mExtractorAudio;
    Button mMuxerVideoAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取权限
        verifyStoragePermissions(this);

        // 分离视频
        mExtractorVideo = (Button) findViewById(R.id.btn_extractor_video);

        // 分离音频
        mExtractorAudio = (Button) findViewById(R.id.btn_extractor_audio);

        // 合成音视频
        mMuxerVideoAudio = (Button) findViewById(R.id.btn_muxer_videoaudio);

        mExtractorVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractorVideo();
            }
        });

        mExtractorAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractorAudio();
            }
        });

        mMuxerVideoAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muxerVideoAudio();
            }
        });

    }


    /**
     * 分离视频
     */
    private void extractorVideo() {
        // 创建MediaExtractor实例
        MediaExtractor mediaExtractor = new MediaExtractor();
        // 初始化MediaMuxer
        MediaMuxer mediaMuxer = null;
        // 轨道索引
        int videoIndex = -1;

        try {
            // 设置数据源
            mediaExtractor.setDataSource(SDCARD_PATH + "/input.mp4");
            // 数据源的轨道数
            int trackCount = mediaExtractor.getTrackCount();

            for (int i = 0; i < trackCount; i++) {
                // 视频轨道格式信息
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String mimeType = trackFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    // 该轨道是视频轨道
                    videoIndex = i;
                }
            }

            // 切换到想要的轨道
            mediaExtractor.selectTrack(videoIndex);

            // 视频轨道格式信息
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(videoIndex);

            // 创建MediaMuxer实例，通过new MediaMuxer(String path, int format)指定视频文件输出路径和文件格式
            mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output_video.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // 添加媒体通道
            int trackIndex = mediaMuxer.addTrack(trackFormat);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 500);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

            // 添加完所有track后调用start方法，开始音视频合成
            mediaMuxer.start();

            // 获取帧之间的间隔时间
            long videoSampleTime;
            {
                // 将样本数据存储到字节缓存区
                mediaExtractor.readSampleData(byteBuffer, 0);
                // skip first I frame
                if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC)
                    // 读取下一帧数据
                    mediaExtractor.advance();

                mediaExtractor.readSampleData(byteBuffer, 0);
                long firstVideoPTS = mediaExtractor.getSampleTime();

                mediaExtractor.advance();
                mediaExtractor.readSampleData(byteBuffer, 0);
                long SecondVideoPTS = mediaExtractor.getSampleTime();

                videoSampleTime = Math.abs(SecondVideoPTS - firstVideoPTS);
                Log.d("fuck", "videoSampleTime is " + videoSampleTime);
            }

            mediaExtractor.unselectTrack(videoIndex);
            mediaExtractor.selectTrack(videoIndex);

            while (true) {
                // 将样本数据存储到字节缓存区
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                // 将样本数据存储到字节缓存区
                if (readSampleSize < 0) {
                    break;
                }
                // 读取下一帧数据
                mediaExtractor.advance();

                bufferInfo.size = readSampleSize;
                bufferInfo.offset = 0;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.presentationTimeUs += videoSampleTime;

                // 调用MediaMuxer.writeSampleData()向mp4文件中写入数据了
                mediaMuxer.writeSampleData(trackIndex, byteBuffer, bufferInfo);
            }

            mediaMuxer.stop();
            mediaExtractor.release();
            mediaMuxer.release();

            Toast.makeText(this, "分离视频完成", Toast.LENGTH_LONG).show();
            Log.i("info", "分离视频finish++++++++++++++++++++++++++++++++++++++");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "分离视频失败", Toast.LENGTH_LONG).show();
            Log.i("info", "分离视频失败++++++++++++++++++++++++++++++++++++++" + e.toString());
        }

    }

    /**
     * 分离音频
     */
    private void extractorAudio() {
        // 创建MediaExtractor实例
        MediaExtractor mediaExtractor = new MediaExtractor();
        // 初始化MediaMuxer
        MediaMuxer mediaMuxer = null;
        // 轨道索引
        int audioIndex = -1;
        try {
            mediaExtractor.setDataSource(SDCARD_PATH + "/input.mp4");
            int trackCount = mediaExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                if (trackFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    audioIndex = i;
                }
            }
            mediaExtractor.selectTrack(audioIndex);

            MediaFormat trackFormat = mediaExtractor.getTrackFormat(audioIndex);
            mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output_audio.mp3", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeAudioIndex = mediaMuxer.addTrack(trackFormat);
            mediaMuxer.start();

            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

            long stampTime = 0;
            // 获取帧之间的间隔时间
            {
                mediaExtractor.readSampleData(byteBuffer, 0);
                if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    mediaExtractor.advance();
                }

                mediaExtractor.readSampleData(byteBuffer, 0);
                long secondTime = mediaExtractor.getSampleTime();
                mediaExtractor.advance();

                mediaExtractor.readSampleData(byteBuffer, 0);
                long thirdTime = mediaExtractor.getSampleTime();
                stampTime = Math.abs(thirdTime - secondTime);
                Log.e("fuck", stampTime + "");
            }

            mediaExtractor.unselectTrack(audioIndex);
            mediaExtractor.selectTrack(audioIndex);

            while (true) {
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                if (readSampleSize < 0) {
                    break;
                }
                mediaExtractor.advance();

                bufferInfo.size = readSampleSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs += stampTime;

                mediaMuxer.writeSampleData(writeAudioIndex, byteBuffer, bufferInfo);
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaExtractor.release();
            Toast.makeText(this, "分离音频完成", Toast.LENGTH_LONG).show();
            Log.i("info", "分离音频完成++++++++++++++++++++++++++++++++++++++");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "分离音频失败", Toast.LENGTH_LONG).show();
            Log.i("info", "分离音频失败++++++++++++++++++++++++++++++++++++++" + e.toString());
        }
    }

    /**
     * 合成音视频
     */
    private void muxerVideoAudio() {
        try {
            // 以下过程是找到output_video.mp4中视频轨道
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(SDCARD_PATH + "/output_video.mp4");
            MediaFormat videoFormat = null;
            int videoTrackIndex = -1;
            int videoTrackCount = videoExtractor.getTrackCount();
            for (int i = 0; i < videoTrackCount; i++) {
                videoFormat = videoExtractor.getTrackFormat(i);
                String mimeType = videoFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    videoTrackIndex = i;
                    break;
                }
            }

            // 以下过程是找到output_audio.mp3中音频轨道
            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(SDCARD_PATH + "/output_audio.mp3");
            MediaFormat audioFormat = null;
            int audioTrackIndex = -1;
            int audioTrackCount = audioExtractor.getTrackCount();
            for (int i = 0; i < audioTrackCount; i++) {
                audioFormat = audioExtractor.getTrackFormat(i);
                String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("audio/")) {
                    audioTrackIndex = i;
                    break;
                }
            }

            videoExtractor.selectTrack(videoTrackIndex);
            audioExtractor.selectTrack(audioTrackIndex);

            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();

            // 通过new MediaMuxer(String path, int format)指定视频文件输出路径和文件格式
            MediaMuxer mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output-composite.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // MediaMuxer添加媒体通道(视频)
            int writeVideoTrackIndex = mediaMuxer.addTrack(videoFormat);
            // MediaMuxer添加媒体通道(音频)
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
            // 开始音视频合成
            mediaMuxer.start();

            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            long sampleTime = 0;
            {
                videoExtractor.readSampleData(byteBuffer, 0);
                if (videoExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    videoExtractor.advance();
                }

                videoExtractor.readSampleData(byteBuffer, 0);
                long secondTime = videoExtractor.getSampleTime();
                videoExtractor.advance();

                long thirdTime = videoExtractor.getSampleTime();
                sampleTime = Math.abs(thirdTime - secondTime);
            }

            videoExtractor.unselectTrack(videoTrackIndex);
            videoExtractor.selectTrack(videoTrackIndex);

            while (true) {
                int readVideoSampleSize = videoExtractor.readSampleData(byteBuffer, 0);
                if (readVideoSampleSize < 0) {
                    break;
                }

                videoBufferInfo.size = readVideoSampleSize;
                videoBufferInfo.presentationTimeUs += sampleTime;
                videoBufferInfo.offset = 0;
                videoBufferInfo.flags = videoExtractor.getSampleFlags();

                mediaMuxer.writeSampleData(writeVideoTrackIndex, byteBuffer, videoBufferInfo);
                videoExtractor.advance();
            }

            while (true) {
                int readAudioSampleSize = audioExtractor.readSampleData(byteBuffer, 0);
                if (readAudioSampleSize < 0) {
                    break;
                }

                audioBufferInfo.size = readAudioSampleSize;
                audioBufferInfo.presentationTimeUs += sampleTime;
                audioBufferInfo.offset = 0;
                audioBufferInfo.flags = videoExtractor.getSampleFlags();

                mediaMuxer.writeSampleData(writeAudioTrackIndex, byteBuffer, audioBufferInfo);
                audioExtractor.advance();
            }

            mediaMuxer.stop();
            mediaMuxer.release();
            videoExtractor.release();
            audioExtractor.release();

            Toast.makeText(this, "合成音视频完成", Toast.LENGTH_LONG).show();
            Log.i("info", "合成音视频完成++++++++++++++++++++++++++++++++++++++");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "合成音视频失败", Toast.LENGTH_LONG).show();
            Log.i("info", "合成音视频失败++++++++++++++++++++++++++++++++++++++" + e.toString());
        }
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            // 检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
