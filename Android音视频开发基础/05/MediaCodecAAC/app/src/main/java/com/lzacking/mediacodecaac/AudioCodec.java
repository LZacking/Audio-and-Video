package com.lzacking.mediacodecaac;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;

/**
 * 音频相关的操作类
 */
public class AudioCodec {

    private static final String TAG = "AudioCodec";
    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 将音频文件解码成原始的PCM数据
     * @param audioPath         音频文件目录
     * @param audioSavePath     pcm文件保存位置
     * @param listener
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void getPCMFromAudio(String audioPath, String audioSavePath, final AudioDecodeListener listener) {
        MediaExtractor extractor = new MediaExtractor();// 此类可分离视频文件的音轨和视频轨道
        int audioTrack = -1;// 音频MP3文件其实只有一个音轨
        boolean hasAudio = false;// 判断音频文件是否有音频音轨

        try {
            extractor.setDataSource(audioPath);
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("audio/")) {
                    audioTrack = i;
                    hasAudio = true;
                    break;
                }
            }

            if (hasAudio) {
                extractor.selectTrack(audioTrack);
                // 原始音频解码
                new Thread(new AudioDecodeRunnable(extractor, audioTrack, audioSavePath, new DecodeOverListener() {
                    @Override
                    public void decodeIsOver() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.decodeOver();
                                }
                            }
                        });
                    }

                    @Override
                    public void decodeFail() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.decodeFail();
                                }
                            }
                        });
                    }
                })).start();

            } else {// 如果音频文件没有音频音轨
                Log.e(TAG,"音频文件没有音频音轨");
                if (listener != null) {
                    listener.decodeFail();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"解码失败");
            if (listener != null) {
                listener.decodeFail();
            }
        }

    }


    /**
     * pcm文件转音频
     * @param pcmPath       pcm文件目录
     * @param audioPath     音频文件目录
     * @param listener
     */
    public static void PcmToAudio(String pcmPath,String audioPath,final AudioDecodeListener listener) {

        new Thread(new AudioEncodeRunnable(pcmPath, audioPath, new AudioDecodeListener() {
            @Override
            public void decodeOver() {
                if (listener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.decodeOver();
                        }
                    });
                }
            }

            @Override
            public void decodeFail() {
                if (listener != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.decodeFail();
                        }
                    });
                }
            }
        })).start();
    }


    /**
     * 写入ADTS头部数据
     * @param packet
     * @param packetLen
     */
    public static void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = 4; // 44.1KHz
        int chanCfg = 2; // CPE

        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }

    public interface DecodeOverListener {
        void decodeIsOver();
        void decodeFail();
    }

    /**
     * 音频解码监听器：监听是否解码成功
     */
    public interface AudioDecodeListener {
        void decodeOver();
        void decodeFail();
    }

}
