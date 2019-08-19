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

import com.sun.imageio.plugins.common.ImageUtil;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class ImgToVideoTest {


    public static void main(String[] args) throws Exception {
        String imgPath = "d:\\5.jpg";

        String fileSavePath = imgPath + "_converted.mp4";

        create(new FileInputStream(imgPath), fileSavePath);

        //getVideoInfo();

    }

    /**
     * 根据图片本地路径生成5s视频
     */
    public static void create(InputStream inputStream, String videoPath) throws Exception {
        try {
            BufferedImage image = getImage(inputStream, BufferedImage.TYPE_3BYTE_BGR);
            IMediaWriter writer = ToolFactory.makeWriter(videoPath);
            writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, image.getWidth(), image.getHeight());
            for (int i = 0; i <= 5000; i += 40) {
                writer.encodeVideo(0, image, i, TimeUnit.MILLISECONDS);
            }
            writer.close();
        } catch (Exception e) {
            throw new Exception("fail to convert the picture to 5s video", e);
        }
    }

    /**
     * 获取指定格式BufferedImage，不转换格式的话部分图片无法转视频
     */
    private static BufferedImage getImage(InputStream inputStream, int targetType) throws Exception {
        BufferedImage image;
        BufferedImage origImage = ImageIO.read(inputStream);
        if (origImage.getType() == targetType) {
            image = origImage;
        } else {
            image = new BufferedImage(origImage.getWidth(), origImage.getHeight(), targetType);
            image.getGraphics().drawImage(origImage, 0, 0, null);
        }
        return image;
    }


    public static void getVideoInfo() {

        String filename = "D:\\3.png_converted.mp4";

        // first we create a Xuggler container object
        IContainer container = IContainer.make();

        // we attempt to open up the container
        int result = container.open(filename, IContainer.Type.READ, null);

        // check if the operation was successful
        if (result < 0)
            throw new RuntimeException("Failed to open media file");

        // query how many streams the call to open found
        int numStreams = container.getNumStreams();

        // query for the total duration
        long duration = container.getDuration();

        // query for the file size
        long fileSize = container.getFileSize();

        // query for the bit rate
        long bitRate = container.getBitRate();

        System.out.println("Number of streams: " + numStreams);
        System.out.println("Duration (ms): " + duration / 1000);
        System.out.println("File Size (bytes): " + fileSize);
        System.out.println("Bit Rate: " + bitRate);
        System.out.println();

        // iterate through the streams to print their meta data
        for (int i = 0; i < numStreams; i++) {

            // find the stream object
            IStream stream = container.getStream(i);

            // get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();

            System.out.println("*** Start of Stream Info ***");

            System.out.printf("stream %d: ", i);
            System.out.printf("type: %s; ", coder.getCodecType());
            System.out.printf("codec: %s; ", coder.getCodecID());
            System.out.printf("duration: %s; ", stream.getDuration());
            System.out.printf("start time: %s; ", container.getStartTime());
            System.out.printf("timebase: %d/%d; ",
                    stream.getTimeBase().getNumerator(),
                    stream.getTimeBase().getDenominator());
            System.out.printf("coder tb: %d/%d; ",
                    coder.getTimeBase().getNumerator(),
                    coder.getTimeBase().getDenominator());
            System.out.println();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                System.out.printf("sample rate: %d; ", coder.getSampleRate());
                System.out.printf("channels: %d; ", coder.getChannels());
                System.out.printf("format: %s", coder.getSampleFormat());
            } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                System.out.printf("width: %d; ", coder.getWidth());
                System.out.printf("height: %d; ", coder.getHeight());
                System.out.printf("format: %s; ", coder.getPixelType());
                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());
            }

            System.out.println();
            System.out.println("*** End of Stream Info ***");

        }

    }
}
