package com.lzacking.cameraapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.IOException;

public class CameraSurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_surface_view);

        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceView.getHolder().addCallback(this);

        // 打开摄像头并将展示方向旋转90度
        mCamera = Camera.open(0);
        mCamera.setDisplayOrientation(90);
    }

    //------ SurfaceView预览 -------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 在控件创建的时候，进行相应的初始化工作
        try {
            mCamera.setPreviewDisplay(holder);
            // 开始预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 变化时，可以做相应操作
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 释放摄像头
        mCamera.release();
    }

}
