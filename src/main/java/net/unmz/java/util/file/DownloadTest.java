/*******************************************************
 * Copyright (C) 2019 iQIYI.COM - All Rights Reserved
 *
 * This file is part of {cupid_3}.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * Date: 2019/7/24
 * Author(s): zhanglongyun<zhanglongyun@qiyi.com>
 *
 *******************************************************/
package net.unmz.java.util.file;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;


// https://codedestine.com/download-file-from-internet-in-java/s
public class DownloadTest {

    public static void main(String[] args) {
        String url = "http://fcvideo.cdn.bcebos.com/75eaaf44d4fc80b7e448aed25f45e966_1280_720_mp4.mp4";
        String file = "D://file.mp4";

        try {
            //connectionTimeout, readTimeout = 10 seconds
            FileUtils.copyURLToFile(new URL(url), new File(file), 10000, 10000);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
