package com.lzacking.camerabasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class TakeVideoActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private static final int TAKE_VIDEO = 0;
    private Button mTakeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_video);
        mTakeVideo = findViewById(R.id.take_video);
        mVideoView = findViewById(R.id.videoView);

        mTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_VIDEO);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri videoUri = intent.getData();
                    mVideoView.setVideoURI(videoUri);
                    mVideoView.requestFocus();
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);// 设置视频重复播放
                        }
                    });
                    mVideoView.start();// 播放视频
                    MediaController mediaController = new MediaController(this);// 显示控制条
                    mVideoView.setMediaController(mediaController);
                    mediaController.setMediaPlayer(mVideoView);// 设置控制的对象
                    mediaController.show();
                }
                break;
            default:
                break;
        }
    }

}
