package com.lzacking.cameraapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;

import java.io.IOException;

public class CameraTextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_texture_view);

        mTextureView = findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        // 打开摄像头并将展示方向旋转90度
        mCamera = Camera.open(0);
        mCamera.setDisplayOrientation(90);
    }

    //------ TextureView预览 -------
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // 在控件创建的时候，进行相应的初始化工作
        try {
            mCamera.setPreviewTexture(surface);
            // 开始预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // 变化时，可以做相应操作
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // 释放相机
        mCamera.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
