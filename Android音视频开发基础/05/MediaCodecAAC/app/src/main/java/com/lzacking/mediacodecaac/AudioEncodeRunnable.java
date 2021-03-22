package com.lzacking.mediacodecaac;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 音频编码过程
 */
public class AudioEncodeRunnable implements Runnable {

    private static final String TAG = "AudioEncodeRunnable";
    private String pcmPath;
    private String audioPath;
    private AudioCodec.AudioDecodeListener mListener;

    public AudioEncodeRunnable(String pcmPath, String audioPath, final AudioCodec.AudioDecodeListener listener) {
        this.pcmPath = pcmPath;
        this.audioPath = audioPath;
        mListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        try {
            if (!new File(pcmPath).exists()) {// pcm文件目录不存在
                if (mListener != null) {
                    mListener.decodeFail();
                }
                return;
            }

            FileInputStream fis = new FileInputStream(pcmPath);
            byte[] buffer = new byte[8 * 1024];
            byte[] allAudioBytes;

            int inputIndex;
            ByteBuffer inputBuffer;
            int outputIndex;
            ByteBuffer outputBuffer;

            byte[] chunkAudio;
            int outBitSize;
            int outPacketSize;

            // 初始化编码格式   mimetype  采样率  声道数
            MediaFormat encodeFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC,44100,2);
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE,96000);
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE,500 * 1024);

            // 初始化编码器
            MediaCodec mediaEncode = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mediaEncode.configure(encodeFormat,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaEncode.start();

            ByteBuffer[] encodeInputBuffers = mediaEncode.getInputBuffers();
            ByteBuffer[] encodeOutputBuffers = mediaEncode.getOutputBuffers();
            MediaCodec.BufferInfo encodeBufferInfo = new MediaCodec.BufferInfo();

            // 初始化文件写入流
            FileOutputStream fos = new FileOutputStream(new File(audioPath));
            BufferedOutputStream bos = new BufferedOutputStream(fos,500 * 1024);
            boolean isReadEnd = false;
            while (!isReadEnd) {
                for (int i = 0; i < encodeInputBuffers.length - 1; i++) {// 减掉1很重要，不要忘记
                    if (fis.read(buffer) != -1) {
                        allAudioBytes = Arrays.copyOf(buffer, buffer.length);
                    } else {
                        Log.e(TAG,"文件读取完成");
                        isReadEnd = true;
                        break;
                    }

                    Log.e(TAG,"读取文件并写入编码器" + allAudioBytes.length);
                    // 从输入流队列中取数据进行编码操作
                    inputIndex = mediaEncode.dequeueInputBuffer(-1);

                    inputBuffer = encodeInputBuffers[inputIndex];
                    inputBuffer.clear();
                    inputBuffer.limit(allAudioBytes.length);
                    inputBuffer.put(allAudioBytes);// 将pcm数据填充给inputBuffer
                    // 在指定索引处填充输入buffer后，使用queueInputBuffer将buffer提交给组件。
                    mediaEncode.queueInputBuffer(inputIndex, 0, allAudioBytes.length, 0, 0);// 开始编码
                }

                // 从输入流队列中取数据进行编码操作
                outputIndex = mediaEncode.dequeueOutputBuffer(encodeBufferInfo,10000);
                while (outputIndex >= 0) {
                    // 从解码器中取出数据
                    outBitSize = encodeBufferInfo.size;
                    outPacketSize = outBitSize + 7;// 7为adts头部大小
                    outputBuffer = encodeOutputBuffers[outputIndex];// 拿到输出的buffer
                    outputBuffer.position(encodeBufferInfo.offset);
                    outputBuffer.limit(encodeBufferInfo.offset + outBitSize);

                    chunkAudio = new byte[outPacketSize];
                    AudioCodec.addADTStoPacket(chunkAudio, outPacketSize);// 添加ADTS
                    outputBuffer.get(chunkAudio, 7, outBitSize);// 将编码得到的AAC数据取出到byte[]中，偏移量为7
                    outputBuffer.position(encodeBufferInfo.offset);
                    Log.e(TAG, "编码成功并写入文件" + chunkAudio.length);
                    bos.write(chunkAudio,0,chunkAudio.length);// 将文件保存在sdcard中
                    bos.flush();

                    mediaEncode.releaseOutputBuffer(outputIndex,false);
                    outputIndex = mediaEncode.dequeueOutputBuffer(encodeBufferInfo,10000);
                }
            }

            mediaEncode.stop();
            mediaEncode.release();
            fos.close();

            if (mListener != null){
                mListener.decodeOver();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null){
                mListener.decodeFail();
            }
        }
    }

}
