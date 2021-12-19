package com.lzacking.h264decode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Thread mDecodeThread;
    private MediaCodec mMediaCodec;
    private DataInputStream mInputStream;

    private final static String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private final static String H264_FILE = SD_PATH + "/hh264.h264";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取权限
        verifyStoragePermissions(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.SurfaceView);
        // 获取文件输入流
        getFileInputStream();
        // 初始化解码器
        initMediaCodec();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

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

    /**
     * 获取需要解码的文件流
     */
    public void getFileInputStream() {
        try {
            File file = new File(H264_FILE);
            mInputStream = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                mInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 获得可用的字节数组
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1) {
            // 将读取的数据写入到字节输出流
            bos.write(buf, 0, len);
        }
        // 将这个流转换成字节数组
        buf = bos.toByteArray();
        return buf;
    }

    /**
     * 初始化解码器
     */
    private void initMediaCodec() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    // 创建编码器
                    mMediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 使用MediaFormat初始化编码器，设置宽，高
                final MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
                // 设置帧率
                mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 40);
                // 配置编码器
                mMediaCodec.configure(mediaFormat, holder.getSurface(), null, 0);

                startDecodingThread();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    /**
     * 开启解码器并开启读取文件的线程
     */
    private void startDecodingThread() {
        mMediaCodec.start();
        mDecodeThread = new Thread(new DecodeThread());
        mDecodeThread.start();
    }


    private class DecodeThread implements Runnable {
        @Override
        public void run() {
            // 开始解码
            decode();
        }

        private void decode() {
            // 获取一组缓存区(8个)
            ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
            // 解码后的数据，包含每一个buffer的元数据信息
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            // 获取缓冲区的时候，需要等待的时间(单位：毫秒)
            long timeoutUs = 10000;
            byte[] streamBuffer = null;
            try {
                // 返回可用的字节数组
                streamBuffer = getBytes(mInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int bytes_cnt = 0;
            // 得到可用字节数组长度
            bytes_cnt = streamBuffer.length;

            // 没有得到可用数组
            if (bytes_cnt == 0) {
                streamBuffer = null;
            }
            // 每帧的开始位置
            int startIndex = 0;
            // 定义记录剩余字节的变量
            int remaining = bytes_cnt;
            // while(true)大括号内的内容是获取一帧，解码，然后显示；直到获取最后一帧，解码，结束
            while (true) {
                // 当剩余的字节=0或者开始的读取的字节下标大于可用的字节数时  不在继续读取
                if (remaining == 0 || startIndex >= remaining) {
                    break;
                }

                // 寻找帧头部
                int nextFrameStart = findHeadFrame(streamBuffer, startIndex + 2, remaining);

                // 找不到头部返回-1
                if (nextFrameStart == -1) {
                    nextFrameStart = remaining;
                }
                // 得到可用的缓存区
                int inputIndex = mMediaCodec.dequeueInputBuffer(timeoutUs);
                // 有可用缓存区
                if (inputIndex >= 0) {
                    ByteBuffer byteBuffer = inputBuffers[inputIndex];
                    byteBuffer.clear();
                    // 将可用的字节数组(一帧)，传入缓冲区
                    byteBuffer.put(streamBuffer, startIndex, nextFrameStart - startIndex);
                    // 把数据传递给解码器
                    mMediaCodec.queueInputBuffer(inputIndex, 0, nextFrameStart - startIndex, 0, 0);
                    // 指定下一帧的位置
                    startIndex = nextFrameStart;
                } else {
                    continue;
                }

                int outputIndex = mMediaCodec.dequeueOutputBuffer(info, timeoutUs);
                if (outputIndex >= 0) {
                    // 加入try catch的目的是让界面显示的慢一点，这个步骤可以省略
                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 将处理过的数据交给surfaceview显示
                    mMediaCodec.releaseOutputBuffer(outputIndex, true);
                }
            }
        }
    }

    /**
     * 查找帧头部的位置
     * 在实际的H264数据帧中，往往帧前面带有00 00 00 01 或 00 00 01分隔符
     * @param bytes
     * @param start
     * @param totalSize
     * @return
     */
    private int findHeadFrame(byte[] bytes, int start, int totalSize) {
        for (int i = start; i < totalSize - 4; i++) {
            if (((bytes[i] == 0x00) && (bytes[i + 1] == 0x00) && (bytes[i + 2] == 0x00) && (bytes[i + 3] == 0x01)) || ((bytes[i] == 0x00) && (bytes[i + 1] == 0x00) && (bytes[i + 2] == 0x01))) {
                return i;
            }
        }
        return -1;
    }
}
