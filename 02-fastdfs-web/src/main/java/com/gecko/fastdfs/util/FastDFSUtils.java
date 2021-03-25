package com.gecko.fastdfs.util;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * FastDFS 分布式文件系统 Java 客户端工具类
 * 具体功能：文件上传、下载、替换、删除、查询文件元数据、查看文件详情
 */
public class FastDFSUtils {
    private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;
    private static StorageClient storageClient = null;

    static {
        //1.加载配置文件，默认去classpath下加载
        try {
            ClientGlobal.init("fdfs_client.conf");
        //2.创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //3.创建TrackerServer对象
            trackerServer = trackerClient.getConnection();
        //4.创建StorageServer对象
            storageServer = trackerClient.getStoreStorage(trackerServer);
            //5.创建StorageClient对象，这个对象完成对文件的操作
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        fileDownload("group1", "M00/00/00/wKi2gGBatYiASR5EACoeC5KngBs842.jpg", "D:/888.jpg");
    }

    /**
     * 资源释放
     */
    public static void releaseFastDFS() {
        if (storageServer != null) {
            try {
                storageServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 文件上传
     * @param localFileName 本地文件名[具体位置+文件名]
     * @param fileExtName   上传文件的后缀
     */
    public static void fileUpload(String localFileName, String fileExtName) {
        try {
            //6.上传文件  第一个参数：本地文件路径 第二个参数：上传文件的后缀 第三个参数：文件信息
            String[] uploadArray = storageClient.upload_file(localFileName, fileExtName, null);
            for (String str : uploadArray) {
                System.out.println(str);
                // http:192.168.182.128/group1/M00/00/00/wKi2gGBas4aAEAdlACoeC5KngBs554.jpg
            }
        } catch (IOException | MyException e) {
            e.printStackTrace();
        } finally {
            releaseFastDFS();
        }
    }

    /**
     * 文件上传
     * @param bytes            byte字节数组
     * @param fileExtName      文件拓展名[后缀]
     * @return 组名/远程文件名   group1/M00/00/00/....
     */
    public static String fileUpload(byte[] bytes, String fileExtName) {
        try {
            String[] uploadFile = storageClient.upload_file(bytes, fileExtName, null);
            for (String str : uploadFile) {
                System.out.println(str);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://192.168.182.128/");
            stringBuilder.append(uploadFile[0]);
            stringBuilder.append("/");
            stringBuilder.append(uploadFile[1]);
            return stringBuilder.toString();
        } catch (IOException | MyException e) {
            e.printStackTrace();
        } finally {
            releaseFastDFS();
        }
        return null;
    }

    /**
     * 文件下载
     * @param groupName       组/卷名，默认值：group1
     * @param remoteFileName  文件名，例如："M00/00/00/wKgKZl9tkTCAJAanAADhaCZ_RF0495.jpg"
     * @param localFileName   本地文件位置  D:/222.jpg
     * @return 0为成功 ，非0为失败
     */
    public static int fileDownload(String groupName, String remoteFileName, String localFileName){

        int count = -1;
        try {
            count = storageClient.download_file(groupName == null ? "group1" : groupName, remoteFileName, localFileName);
            System.out.println("downloadFile : " + count);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        } finally {
            releaseFastDFS();
        }
        return count;
    }
    /**
     * 文件下载
     * @param groupName       组/卷名，默认值：group1
     * @param remoteFileName  文件名，例如："M00/00/00/wKgKZl9tkTCAJAanAADhaCZ_RF0495.jpg"
     * @return byte[]       文件字节数组
     */
    public static byte[] fileDownload(String groupName, String remoteFileName) {
        byte[] bytes = null;
        try {
             bytes = storageClient.download_file(groupName, remoteFileName);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        } finally {
            releaseFastDFS();
        }
        return bytes;
    }
    /**
     * 文件删除
     *
     * @param groupName      组/卷名，默认值：group1
     * @param remoteFileName 文件名，例如："M00/00/00/wKgKZl9tkTCAJAanAADhaCZ_RF0495.jpg"
     * @return 0为成功 true，非0为失败 false
     */
    public static boolean fileDelete(String groupName, String remoteFileName) {
        int delete_file = -1;
        try {
            delete_file = storageClient.delete_file(groupName, remoteFileName);
            System.out.println("deleteCount : " + delete_file);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        } finally {
            releaseFastDFS();
        }
        return delete_file == 0;
    }

    /**
     * 修改一个已经存在的文件
     *
     * @param oldGroupName 旧组名
     * @param oldFileName  旧文件名
     * @param bytes         新文件
     * @param fileName     新文件名
     * @return
     */
    public static String modifyFile(String oldGroupName, String oldFileName, byte[] bytes, String fileName) {
        // 先上传
        String fileUpload = fileUpload(bytes, fileName);
        if (fileUpload == null) {
            return null;
        }
        // 再删除
        boolean delResult = fileDelete(oldGroupName, oldFileName);
        if (delResult) {
            return null;
        }
        return fileUpload;
    }
    /**
     * 获取元数据
     *
     * @param groupName      组/卷名，默认值：group1
     * @param remoteFileName 文件名，例如："M00/00/00/wKgKZl9tkTCAJAanAADhaCZ_RF0495.jpg"
     * @return 文件的元数据数组
     */
    public static NameValuePair[] getMetaData(String groupName, String remoteFileName) {
        try {
            // 根据组名和文件名通过 Storage 客户端获取文件的元数据数组
            return storageClient.get_metadata(groupName == null ? "group1" : groupName, remoteFileName);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件详情
     *
     * @param groupName      组/卷名，默认值：group1
     * @param remoteFileName 文件名，例如："M00/00/00/wKgKZl9tkTCAJAanAADhaCZ_RF0495.jpg"
     * @return 文件详情
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {
        try {
            return storageClient.get_file_info(groupName == null ? "group1" : groupName, remoteFileName);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取文件后缀名（不带点）
     *
     * @param fileName 文件名称
     * @return 如："jpg" or ""
     */
    public static String getFileExt(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1); // 不带最后的点
    }

    /**
     * 获取文件后缀名（带点）
     *
     * @param fileName 文件名称
     * @return 如：".jpg  .gif" or ""
     */
    public static String getFileExtWithPoint(String fileName) {
        if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")); // 带最后的点
    }

}