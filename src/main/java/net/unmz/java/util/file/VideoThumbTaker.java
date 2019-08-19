/*******************************************************
 * Copyright (C) 2019 iQIYI.COM - All Rights Reserved
 *
 * This file is part of {cupid_3}.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * Date: 2019/8/19
 * Author(s): zhanglongyun<zhanglongyun@qiyi.com>
 *
 *******************************************************/
package net.unmz.java.util.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 获取视频 ——指定帧，指定大小的图片
 */
public class VideoThumbTaker {

    protected String ffmpegApp;

    public VideoThumbTaker(String ffmpegApp) {
        this.ffmpegApp = ffmpegApp;
    }

    @SuppressWarnings("unused")
    /****
     * 获取指定时间内的图片
     * @param videoFilename:视频路径
     * @param thumbFilename:图片保存路径
     * @param width:图片长
     * @param height:图片宽
     * @param hour:指定时
     * @param min:指定分
     * @param sec:指定秒
     * @throws IOException
     * @throws InterruptedException
     */
    public void getThumb(String videoFilename, String thumbFilename, int width, int height, int hour, int min, float sec) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp, "-y", "-i", videoFilename, "-vframes", "1", "-ss", hour + ":" + min + ":" + sec, "-f", "mjpeg", "-s", width + "*" + height,
                "-an", thumbFilename);

        Process process = processBuilder.start();

        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null)
            ;
        process.waitFor();

        if (br != null)
            br.close();
        if (isr != null)
            isr.close();
        if (stderr != null)
            stderr.close();
    }

    public static void main(String[] args) {
        VideoThumbTaker videoThumbTaker = new VideoThumbTaker("D:\\安装包\\ffmpeg-20190818-cff3090-win64-static\\ffmpeg-20190818-cff3090-win64-static\\bin\\ffmpeg.exe");
        try {
            videoThumbTaker.getThumb("D:\\3.png_converted.mp4", "D:\\3.mp4.png", 800, 600, 0, 0, 5);
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
