package com.lzacking.cameraapidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSurfaceView;
    private Button mTextureView;
    private Button mNv21DataCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // SurfaceView预览camera数据
        mSurfaceView = findViewById(R.id.btn_surfaceview);
        mSurfaceView.setOnClickListener(this);

        // TextureView预览camera数据
        mTextureView = findViewById(R.id.btn_textureview);
        mTextureView.setOnClickListener(this);

        // 获取的NV21数据进行回调
        mNv21DataCallback = findViewById(R.id.btn_nv21data_callback);
        mNv21DataCallback.setOnClickListener(this);

        // 申请权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("info", "onRequestPermissionsResult: " + "权限已经申请");
                } else {
                    Toast.makeText(this, "你需要打开相机权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_surfaceview:
                Intent intentSurfaceView = new Intent(this, CameraSurfaceViewActivity.class);
                startActivity(intentSurfaceView);
                break;

            case R.id.btn_textureview:
                Intent intentTextureView = new Intent(this, CameraTextureViewActivity.class);
                startActivity(intentTextureView);
                break;

            case R.id.btn_nv21data_callback:
                Intent intentNV21DataCallback = new Intent(this, NV21DataCallbackActivity.class);
                startActivity(intentNV21DataCallback);
                break;

            default:
                break;

        }
    }

}
