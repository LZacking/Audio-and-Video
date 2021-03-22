package com.lzacking.mediacodecaac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button mBtnAudioChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取权限
        verifyStoragePermissions(this);

        mBtnAudioChange = (Button)findViewById(R.id.btn_audio_change);
        mBtnAudioChange.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audio_change:
                Log.e(TAG,"点击了按钮");
                // 首先将aac解码成PCM，再将PCM编码成aac格式的音频文件
                // aac文件（初始文件）
                final String aacPath = Environment.getExternalStorageDirectory().getPath() + "/The Dawn_clip.aac";
                // pcm文件
                final String pcmPath = Environment.getExternalStorageDirectory().getPath() + "/The Dawn_clip.pcm";
                // aac文件（结果文件）
                final String aacResultPath = Environment.getExternalStorageDirectory().getPath() + "/The Dawn_clip1.aac";

                AudioCodec.getPCMFromAudio(aacPath, pcmPath, new AudioCodec.AudioDecodeListener() {
                    @Override
                    public void decodeOver() {
                        Log.e(TAG,"音频解码完成" + pcmPath);

                        // 解码成功之后，开始编码
                        AudioCodec.PcmToAudio(pcmPath, aacResultPath, new AudioCodec.AudioDecodeListener() {
                            @Override
                            public void decodeOver() {
                                Log.e(TAG,"音频编码完成");
                            }

                            @Override
                            public void decodeFail() {
                                Log.e(TAG,"音频编码失败");
                            }
                        });
                    }

                    @Override
                    public void decodeFail() {
                        Log.e(TAG,"音频解码失败");
                    }

                });
                break;

            default:
                break;
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
