package com.lzacking.mediacodecaac;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音频解码过程
 */
public class AudioDecodeRunnable implements Runnable {

    private static final String TAG = "AudioDecodeRunnable";
    final static int TIMEOUT_USEC = 0;
    private MediaExtractor extractor;
    private int audioTrack;
    private AudioCodec.DecodeOverListener mListener;
    private String mPcmFilePath;

    public AudioDecodeRunnable(MediaExtractor extractor, int trackIndex, String savePath, AudioCodec.DecodeOverListener listener) {
        this.extractor = extractor;
        audioTrack = trackIndex;
        mListener = listener;
        mPcmFilePath = savePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        try {
            // 直接从MP3音频文件中得到音轨的MediaFormat
            MediaFormat format = extractor.getTrackFormat(audioTrack);
            // 初始化音频解码器,并配置解码器属性
            MediaCodec audioCodec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
            audioCodec.configure(format,null,null,0);

            // 启动MediaCodec，等待传入数据
            audioCodec.start();

            ByteBuffer[] inputBuffers = audioCodec.getInputBuffers();// 获取需要编码数据的输入流队列，返回的是一个ByteBuffer数组
            ByteBuffer[] outputBuffers = audioCodec.getOutputBuffers();// 获取编解码之后的数据输出流队列，返回的是一个ByteBuffer数组
            MediaCodec.BufferInfo decodeBufferInfo = new MediaCodec.BufferInfo();// 用于描述解码得到的byte[]数据的相关信息
            MediaCodec.BufferInfo inputInfo = new MediaCodec.BufferInfo();// 用于描述输入数据的byte[]数据的相关信息
            boolean codeOver = false;
            boolean inputDone = false;// 整体输入结束标记

            FileOutputStream fos = new FileOutputStream(mPcmFilePath);

            while (!codeOver) {
                if (!inputDone) {
                    for (int i = 0; i < inputBuffers.length; i++) {
                        // 从输入流队列中取数据进行操作
                        // 返回用于填充有效数据的输入buffer的索引，如果当前没有可用的buffer，则返回-1
                        int inputIndex = audioCodec.dequeueInputBuffer(TIMEOUT_USEC);
                        if (inputIndex >= 0) {
                            // 从分离器拿出输入，写入解码器
                            // 拿到inputBuffer
                            ByteBuffer inputBuffer = inputBuffers[inputIndex];
                            // 将position置为0，并不清除buffer内容
                            inputBuffer.clear();
                            int sampleSize = extractor.readSampleData(inputBuffer,0);// 将MediaExtractor读取数据到inputBuffer
                            if (sampleSize < 0){// 表示所有数据已经读取完毕
                                audioCodec.queueInputBuffer(inputIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            } else {
                                inputInfo.offset = 0;
                                inputInfo.size = sampleSize;
                                inputInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                                inputInfo.presentationTimeUs = extractor.getSampleTime();

                                Log.e(TAG,"往解码器写入数据，当前时间戳：" + inputInfo.presentationTimeUs);
                                // 通知MediaCodec解码刚刚传入的数据
                                audioCodec.queueInputBuffer(inputIndex, inputInfo.offset, sampleSize, inputInfo.presentationTimeUs, 0);
                                // 读取下一帧数据
                                extractor.advance();
                            }
                        }
                    }
                }


                // dequeueInputBuffer dequeueOutputBuffer 返回值解释
                // INFO_TRY_AGAIN_LATER=-1 等待超时
                // INFO_OUTPUT_FORMAT_CHANGED=-2 媒体格式更改
                // INFO_OUTPUT_BUFFERS_CHANGED=-3 缓冲区已更改（过时）
                // 大于等于0的为缓冲区数据下标

                boolean decodeOutputDone = false;// 整体解码结束标记
                byte[] chunkPCM;
                while (!decodeOutputDone) {
                    int outputIndex = audioCodec.dequeueOutputBuffer(decodeBufferInfo, TIMEOUT_USEC);
                    if (outputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        // 没有可用的解码器
                        decodeOutputDone = true;
                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        outputBuffers = audioCodec.getOutputBuffers();
                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        MediaFormat newFormat = audioCodec.getOutputFormat();
                    } else if (outputIndex < 0) {

                    } else {
                        ByteBuffer outputBuffer;
                        if (Build.VERSION.SDK_INT >= 21) {
                            outputBuffer = audioCodec.getOutputBuffer(outputIndex);
                        } else {
                            outputBuffer = outputBuffers[outputIndex];
                        }

                        chunkPCM = new byte[decodeBufferInfo.size];
                        outputBuffer.get(chunkPCM);
                        outputBuffer.clear();

                        fos.write(chunkPCM);//数据写入文件中
                        fos.flush();
                        Log.e(TAG,"释放输出流缓冲区：" + outputIndex);
                        audioCodec.releaseOutputBuffer(outputIndex,false);

                        if ((decodeBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {// 编解码结束
                            extractor.release();
                            audioCodec.stop();
                            audioCodec.release();
                            codeOver = true;
                            decodeOutputDone = true;
                        }
                    }
                }
            }

            fos.close();
            mListener.decodeIsOver();
            if (mListener != null) {
                mListener.decodeIsOver();
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.decodeFail();
            }
        }

    }
}
