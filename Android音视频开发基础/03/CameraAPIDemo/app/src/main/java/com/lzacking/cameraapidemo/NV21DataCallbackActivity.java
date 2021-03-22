package com.lzacking.cameraapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NV21DataCallbackActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceView mSurfaceView;

    private Camera.Size mPreviewSize; // 预览尺寸大小
    private ByteArrayOutputStream mBaos;
    private byte[] mImageBytes;
    private Bitmap mBitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv21data_callback);
        mImageView = findViewById(R.id.iv_imageview);

        mSurfaceView = findViewById(R.id.surfaceView);
        mSurfaceView.getHolder().addCallback(this);

        // 打开摄像头并将展示方向旋转90度
        mCamera = Camera.open(0);
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    // 窗口改变
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        doChange(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release();
    }


    // 当我们的程序开始运行，surFaceView显示当前摄像头获取的内容，获取的NV21数据显示在ImageView控件上
    private void doChange(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);// 设置摄像机的预览界面
            // 设置surfaceView旋转的角度，系统默认的录制是横向的画面
            mCamera.setDisplayOrientation(getDegree());

            if (mCamera != null ) {
                try {
                    Camera.Parameters parameters = mCamera.getParameters(); // 获取摄像头参数

                    // 可以根据情况设置参数
                    // 镜头缩放
                    // parameters.setZoom();

                    // 设置预览照片的大小
                    // parameters.setPreviewSize(200, 200);

                    // 设置预览照片时每秒显示多少帧的最小值和最大值
                    // parameters.setPreviewFpsRange(4, 10);

                    // 设置图片格式
                    // parameters.setPictureFormat(ImageFormat.JPEG);

                    // 设置JPG照片的质量  图片的质量[0-100],100最高
                    // parameters.set("jpeg-quality", 85);

                    // 设置照片的大小
                    // parameters.setPictureSize(200, 200);

                    // 设置预览图片的图像格式
                    parameters.setPreviewFormat(ImageFormat.NV21);

                    mCamera.setParameters(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // 处理data，这里面的data数据就是NV21格式的数据，将数据显示在ImageView控件上面
                    mPreviewSize = camera.getParameters().getPreviewSize();// 获取尺寸,格式转换的时候要用到
                    // 取发YUVIMAGE
                    YuvImage yuvimage = new YuvImage(
                            data,
                            ImageFormat.NV21,
                            mPreviewSize.width,
                            mPreviewSize.height,
                            null);
                    mBaos = new ByteArrayOutputStream();
                    // yuvimage转换成jpg格式
                    yuvimage.compressToJpeg(new Rect(0, 0, mPreviewSize.width, mPreviewSize.height), 100, mBaos);// 80--JPG图片的质量[0-100],100最高
                    mImageBytes = mBaos.toByteArray();

                    // 将mImageBytes转换成bitmap
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    mBitmap = BitmapFactory.decodeByteArray(mImageBytes, 0, mImageBytes.length, options);
                    mImageView.setImageBitmap(rotateBitmap(mBitmap, getDegree()));
                }
            });

            mCamera.startPreview();// 开始预览

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getDegree() {
        // 获取当前屏幕旋转的角度
        int rotating = this.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;// 度数
        // 根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }


    /**
     * 选择变换
     * @param origin 原图
     * @param degree  旋转角度，可正可负
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap origin, float degree) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

}
