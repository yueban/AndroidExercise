package com.juphoon.cloud.sample.Toos;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfStatistic {
    private static String TAG = ConfStatistic.class.getSimpleName();

    public class LocalStatistic {
        //音视频发送总发包
        public String sendAllPackets;
        //音视频发送总丢包
        public String sendLost;
        //瞬时音视频发送丢包率（瞬时）
        public String sendLostRate;
        //发送抖动
        public String sendJitter;
        //接收抖动
        public String recvJitter;
        //发送延时
        public String sendRTT;
        //接收延时
        public String recvRTT;
        //发送带宽
        public String sendBitRate;
        //接收带宽
        public String recvBitRate;
        //音视频接收总收包
        public String recvAllPackets;
        //音视频接收总丢包
        public String recvLost;
        //音视频接收丢包率（瞬时）
        public String recvLostRate;
        //视频编码
        public String videoCodec;
        //音频编码
        public String audioCodec;
    }

    public static class PartpStatistic {
        //用户名
        public String uid;
        //视频数据包个数
        public String videoPackets;
        //当前镜头采集帧速
        public String videoCaptureFr;
        //当前视频接收帧速/已发送的视频关键帧请求个数
        public String videoFPSFIR;
        //视频分辨率
        public String videoResolution;
        //当前音频数据码率(视频带宽）
        public String videoBitrate;
        //视频编码量化系数
        public String videoQP;
        //冗余保护占的百分比
        public String videoFecPrecent;
        //音频数据包个数
        public String audioPackets;
        //当前音频数据码率(音频带宽)
        public String audioBitRate;
        //音频冗余保护占的百分比
        public String audioFecPrecent;
        //开启音频
        public String audio;
        //开启视频
        public String video;
        //订阅屏幕共享的信息
        public String screen;
        //当前视频渲染帧速
        public String videoRenderFr;
        // mos分
        public String videoPvMos;
    }

    /**
     * 本地参数
     */
    public LocalStatistic localStatistic;
    /**
     * 成员参数集合
     */
    public ArrayList<PartpStatistic> partpStatisticList;

    /**
     * 解析会议统计信息字符串生成统计信息对象
     *
     * @param statisticData 参数数据 通过JCManager.getInstance().mediaChannel.getStatistics()获得
     * @return 统计信息对象
     */
    public static ConfStatistic parseStatisic(String statisticData) {
        ConfStatistic statistic = new ConfStatistic();
        try {
            JSONObject object = new JSONObject(statisticData);
            statistic.parseConfig(object.optString("Config"));
            statistic.parseNetWork(object.optString("Network"));
            JSONArray array = object.optJSONArray("Participants");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String key = obj.keys().next();
                    String value = obj.getString(key);
                    Map partStatistic = statistic.splitPartpStatistic(value);
                    statistic.partpStatisticList.add(statistic.parsePartpStatistic(key, partStatistic));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return statistic;
    }

    private ConfStatistic() {
        localStatistic = new LocalStatistic();
        partpStatisticList = new ArrayList<>();
    }

    private void parseConfig(String config) {
        Pattern audioPattern = Pattern.compile("Audio Config:\r\n([\\s\\S]*)Video Config:");
        Matcher audioConfigMatcher = audioPattern.matcher(config);
        if (audioConfigMatcher.find()) {
            String audioConfigData = audioConfigMatcher.group(1);
            Pattern audioConfigPattern = Pattern.compile("Codec:(.*)\r\n");
            Matcher matcher = audioConfigPattern.matcher(audioConfigData);
            if (matcher.find()) {
                localStatistic.audioCodec = matcher.group(1);
            } else {
                localStatistic.audioCodec = "";
            }
        } else {
            localStatistic.audioCodec = "";
        }
        Pattern configPattern = Pattern.compile("Video Config:\r\n([\\s\\S]*)");
        Matcher videoConfigMatcher = configPattern.matcher(config);
        if (videoConfigMatcher.find()) {
            String videoConfigData = videoConfigMatcher.group(1);
            Pattern videoConfigPattern = Pattern.compile("Codec:(.*)\r\n");
            Matcher matcher = videoConfigPattern.matcher(videoConfigData);
            if (matcher.find()) {
                localStatistic.videoCodec = matcher.group(1);
            } else {
                localStatistic.videoCodec = "";
            }
        } else {
            localStatistic.videoCodec = "";
        }
    }

    private void parseNetWork(String netWorkString) {
        Pattern pattern = Pattern.compile("^Send Statistic:\r\n ([\\s\\S]*)Recv Statistic:\r\n([\\s\\S]*)\r\nServer");
        Matcher matcher = pattern.matcher(netWorkString);
        if (matcher.find()) {
            Map sendMap = split2Map(matcher.group(1));
            Map receiverMap = split2Map(matcher.group(2));
            localStatistic.sendAllPackets = (String) sendMap.get("Packets");
            localStatistic.sendLost = (String) sendMap.get("Lost");
            localStatistic.sendLostRate = (String) sendMap.get("LostRate/Relay");
            localStatistic.sendJitter = (String) sendMap.get("Jitter");
            localStatistic.sendRTT = (String) sendMap.get("RTT");
            localStatistic.sendBitRate = (String) sendMap.get("BitRate/BWE");
            localStatistic.recvJitter = (String) receiverMap.get("Jitter");
            localStatistic.recvAllPackets = (String) receiverMap.get("Packets");
            localStatistic.recvLost = (String) receiverMap.get("Lost");
            localStatistic.recvLostRate = (String) receiverMap.get("Lost Ratio");
            localStatistic.recvBitRate = (String) receiverMap.get("BitRate/BWE");
        } else {
            Log.d(TAG, "netWorkString no match");
        }
    }


    private Map splitPartpStatistic(String partpString) {
        HashMap map = new HashMap();
        Pattern videoPattern = Pattern.compile("Video (Sending|Receiving) Stats:\r\n([\\s\\S]*)");
        Matcher videoMatcher = videoPattern.matcher(partpString);
        if (videoMatcher.find()) {
            videoMatcher.group(1).trim();
            HashMap videoMap = (HashMap) split2Map(videoMatcher.group(2).trim());
            if (TextUtils.equals(videoMatcher.group(1).trim(), "Sending")) {
                map.put("videoCaptureFr", videoMap.get("Capture Fr"));
                map.put("videoFPS/IDR", videoMap.get("FPS/IDR"));
                map.put("videoBitrate/Setrate", videoMap.get("Bitrate/Setrate"));
                map.put("videoQP", videoMap.get("QP"));
                map.put("videoFecPrecent", videoMap.get("FecPrecent"));
            } else {
                map.put("videoFPS/IDR", videoMap.get("FPS/FIR"));
                map.put("videoRenderFR", videoMap.get("Render FR"));
                map.put("videoPvMos", videoMap.get("PvMos"));
                map.put("videoBitrate/Setrate", videoMap.get("Bitrate"));
            }
            map.put("videoPackets", videoMap.get("Packets"));
            map.put("videoResolution", videoMap.get("Resolution"));
        } else {
            Log.d(TAG, "Video no match");
        }
        Pattern audioPattern = Pattern.compile("Audio (Sending|Receiving) Stats:\r\n([\\s\\S]*)(Video Sending Stats|Video Receiving Stats)");
        Matcher audioMatcher = audioPattern.matcher(partpString);
        if (audioMatcher.find()) {
            HashMap audioMap = (HashMap) split2Map(audioMatcher.group(2).trim());
            map.put("audioPackets", audioMap.get("Packets"));
            map.put("audioBitRate", audioMap.get("BitRate"));
            map.put("audioFecPrecent", audioMap.get("FecPrecent"));
        } else {
            Log.d(TAG, "Audio no match");
        }
        Pattern subscribedPattern = Pattern.compile("(Subscribed|Subscribe) Stats:\r\n([\\s\\S]*)");
        Matcher subscribedMatcher = subscribedPattern.matcher(partpString);
        if (subscribedMatcher.find()) {
            HashMap subscribledMap = (HashMap) split2Map(subscribedMatcher.group(2).trim());
            map.put("Audio", subscribledMap.get("Audio"));
            map.put("Video", subscribledMap.get("Video"));
            map.put("Screen", subscribledMap.get("Screen"));
        } else {
            Log.d(TAG, "Subscribed no match");
        }
        return map;
    }

    private ConfStatistic.PartpStatistic parsePartpStatistic(String uid, Map partStatistic) {
        ConfStatistic.PartpStatistic partPStatistic = new ConfStatistic.PartpStatistic();
        partPStatistic.uid = uid;
        partPStatistic.videoPackets = (String) partStatistic.get("videoPackets");
        partPStatistic.videoCaptureFr = (String) partStatistic.get("videoCaptureFr");
        partPStatistic.videoFPSFIR = (String) partStatistic.get("videoFPS/IDR");
        partPStatistic.videoResolution = (String) partStatistic.get("videoResolution");
        partPStatistic.videoBitrate = (String) partStatistic.get("videoBitrate/Setrate");
        partPStatistic.videoQP = (String) partStatistic.get("videoQP");
        partPStatistic.videoFecPrecent = (String) partStatistic.get("videoFecPrecent");
        partPStatistic.videoRenderFr = (String) partStatistic.get("videoRenderFr");
        partPStatistic.audioBitRate = (String) partStatistic.get("audioBitRate");
        partPStatistic.audioFecPrecent = (String) partStatistic.get("audioFecPrecent");
        partPStatistic.audioPackets = (String) partStatistic.get("audioPackets");
        partPStatistic.audio = (String) partStatistic.get("Audio");
        partPStatistic.video = (String) partStatistic.get("Video");
        partPStatistic.screen = (String) partStatistic.get("Screen");
        return partPStatistic;
    }

    private Map split2Map(String statistic) {
        HashMap<String, String> map = new HashMap();
        String[] separated = statistic.split("\r\n");
        for (int i = 0; i < separated.length; i++) {
            String[] s = separated[i].split(":");
            if (s.length == 1) {
                map.put(s[0].trim(), "");
            } else if (s.length >= 2) {
                map.put(s[0].trim(), s[1].trim());
            }
        }
        return map;
    }
}
