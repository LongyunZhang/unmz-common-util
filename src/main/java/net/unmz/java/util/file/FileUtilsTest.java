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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * https://eksliang.iteye.com/blog/2217374
 */
public class FileUtilsTest {

    public static void read() throws Exception {
        //读取文本文件的所有行到一个集合
        List<String> lines = FileUtils.readLines(new File("D:/fileUtis/aa.txt"), "utf-8");
        for (String s : lines) {
            System.out.println(s);
        }
        //读取文件内容到一个字符串
        String str = FileUtils.readFileToString(new File("D:/fileUtis/aa.txt"), "utf-8");
        System.out.println(str);
        //读取文件到一个byte数组
        FileUtils.readFileToByteArray(new File("D:/fileUtis/aa.txt"));
    }

    public static void write() throws Exception {
        //将字符写入到一个文件，文件不存在会创建；第三个参数：true：追加，false：覆盖
        FileUtils.write(new File("D:/fileUtis/targetFile/aa.txt"), "aa", false);
        //根据指定编码将字符写入到一个文件，文件不存在会创建；会覆盖
        FileUtils.write(new File("D:/fileUtis/targetFile/bb.txt"), "ickes", "utf-8");
        //根据指定编码将字符写入到一个文件，文件不存在会创建；会覆盖
        FileUtils.write(new File("D:/fileUtis/targetFile/cc.txt"), "ickes", "utf-8", true);
        //将一个字符串集合根据指定的分隔符写入到文件中，第四个参数是分隔符
        List<String> ss = new ArrayList<>();
        ss.add("aa");
        ss.add("bb");
        FileUtils.writeLines(new File("D:/fileUtis/targetFile/dd.txt"), "utf-8", ss, ",");
        //将一个字符串集合，一行一行写入到文件中，
        FileUtils.writeLines(new File("D:/fileUtis/targetFile/ee.txt"), "utf-8", ss, true);
    }

    public static void del() throws Exception {
        //删除一个目录和他的所有子目录，如果文件或者目录不存在会抛出异常
        FileUtils.deleteDirectory(new File("D:/fileUtis/targetFile/"));
        //删除一个目录或者一个文件，如果这个目录或者目录不存在不会抛出异常
        FileUtils.deleteQuietly(new File("D:/fileUtis/targetFile/"));
        //清除一个目录下面的所有文件跟目录。
        FileUtils.cleanDirectory(new File("D:/fileUtis/targetFile/"));
        //删除一个文件，如果是目录则递归删除forceDelete(File file),跟deleteDirectory基本一样
        FileUtils.forceDelete(new File("D:/fileUtis/targetFile/"));
    }

    public static void create() throws Exception {
        //创建一个目录，可以递归创建，只要不为null
        FileUtils.forceMkdir(new File("D:/fileUtis/targetFile/aa"));
        //创建一个空文件，若文件应经存在则只更改文件的最近修改时间
        FileUtils.touch(new File("D:/fileUtis/targetFile/Liftoff.java"));
    }

    public static void copy() throws Exception {
        File srcFile = null;
        File destFile = null;

        //复制目录
        File dataFile = new File("D:/fileUtis/dataFile");
        File targetFile = new File("D:/fileUtis/targetFile");
        if (targetFile.isDirectory()) {//判断是否是一个目录
            FileUtils.copyDirectory(dataFile, targetFile);
        }
        //复制文件
        dataFile = new File("D:/fileUtis/dataFile/joiner.java");
        targetFile = new File("D:/fileUtis/targetFile/aa.txt");
        FileUtils.copyFile(dataFile, targetFile);
        //复制文件到一个目录
        dataFile = new File("D:/fileUtis/dataFile/joiner.java");
        targetFile = new File("D:/fileUtis/");
        FileUtils.copyFileToDirectory(dataFile, targetFile);

        //移动目录到新的目录并且删除老的目录,新的目录不存在会创建，如果存在会报错
        dataFile = new File("D:/fileUtis/dataFile");
        targetFile = new File("D:/fileUtis/aa");
        FileUtils.moveDirectory(dataFile, targetFile);

        dataFile = new File("D:/fileUtis/dataFile");
        targetFile = new File("D:/fileUtis/aa");
        //把目录移动到一个新的文件下面，是新文件下面,ture，当目标文件不存在是否创建
        FileUtils.moveDirectoryToDirectory(dataFile, targetFile, true);
        //复制文件
        FileUtils.moveFile(srcFile, destFile);
    }

    /**
     * 通配符过滤目录下的文件
     */
    static void wildcardTest(){
        File dir = new File("D:/fileUtis");
        FileFilter fileFilter = new WildcardFileFilter("*.java");
        File[] files = dir.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
    }
    /**
     * 过滤文件大小，等于或大于某一尺寸 ，单位为字节
     */
    static void sizeFile(){
        File dir = new File("D:/fileUtis");
        String[] files = dir.list( new SizeFileFilter(1024*2) );
        for ( int i = 0; i < files.length; i++ ) {
            System.out.println(files[i]);
        }
    }

    /**
     * 过滤文件后缀名
     */
    static void suffix(){
        File dir = new File("D:/fileUtis");
        String[] files = dir.list(new SuffixFileFilter(".java"));
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
    }
    /**
     * 使用正则表达式过滤
     */
    static void regex(){
        File dir = new File("D:/fileUtis");
        FileFilter fileFilter = new RegexFileFilter("^j.*.java");
        File[] files = dir.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
    }

    /**
     * 文件前缀过滤
     */
    static void aa(){
        File dir = new File("D:/fileUtis");
        String[] files = dir.list( new PrefixFileFilter("aa"));
        for ( int i = 0; i < files.length; i++ ) {
            System.out.println(files[i]);
        }
    }
    /**
     * 打印这个目录下所有.java结尾的文件名,会递归去他子目录中去找
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File dir = new File("D:/fileUtis");
        Collection<File> files = FileUtils.listFiles(dir,
                FileFilterUtils.suffixFileFilter(".java"),
                DirectoryFileFilter.DIRECTORY);
        for (File f : files) {
            System.out.println(f.getName());
        }
    }

    /**
     * #获取一个目录的大小
     * FileUtils.sizeOfDirectory(file);
     *
     * #获取文件或者目录的大小
     * FileUtils.sizeOf(file)；
     *
     * #得到系统临时目录的路径，例如C:\Users\ADMINI~1\AppData\Local\Temp\
     * FileUtils.getTempDirectoryPath()
     *
     * #比较两个文件内容是否相等，左右两边有空格返回false
     * FileUtils.contentEquals(file1, file2)；
     *
     * #获取用户的主目录路径,返回的是字符串
     * FileUtils.getUserDirectoryPath();
     *
     * #获取代表用户主目录的文件，返回的是file
     * FileUtils.getUserDirectory();
     *
     * #根据指定的文件获取一个新的文件输出流
     * FileUtils.openOutputStream(file);
     *
     * #字节转换成直观带单位的值（包括单位GB，MB，KB或字节），如下返回95 M
     * FileUtils.byteCountToDisplaySize(100000000);
     *
     *
     */
}
