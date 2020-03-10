package com.example.factory.net;

import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.factory.Factory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.HashUtil;

/**
 * 上传工具类，用于上传文件到阿里OSS平台
 */
public class UploadHelper {
    private static final String TAG = UploadHelper.class.getSimpleName();
    //与存储区域有关
    private static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
    //上传仓库名
    private static final String BUCKET_NAME = "italker-new";

    private static OSS getClient(){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAIYQD07p05pHQW","2txxzT8JXiHKEdEjylumFy6sXcDQ0G");
        return new OSSClient(Factory.app(),ENDPOINT,credentialProvider);
    }

    /**
     * 上传方法，成功则返回一个路径
     * @param objKey 上传后，服务器中独立的KEY
     * @param path 需要上传的文件路径
     * @return 存储地址
     */
    private static String upload(String objKey,String path){
        //构造请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKey, path);

        try{
            //初始化上传的Client
            OSS client = getClient();
            //开始同步上传 result未使用？
            PutObjectResult result = client.putObject(request);
            //得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME,objKey);
            //格式打印输出上传的云端地址
            Log.d(TAG, String.format("PublicObjectURL:%s",url));
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            //如果有异常则返回空
            return null;
        }
    }

    /**
     * 上传图片
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path){
        String key = getImageObjKey(path);
        return  upload(key,path);
    }

    /**
     * 上传头像
     * @param path
     * @return
     */
    public static String uploadPortraits(String path){
        String key = getPortraitObjKey(path);
        return  upload(key,path);
    }

    /**
     * 上传音频
     * @param path
     * @return
     */
    public static String uploadAudio(String path){
        String key = getAudioObjKey(path);
        return  upload(key,path);
    }

    /**
     * 分与存储
     * @return
     */
//    private static String getDataString(){
//        return new SimpleDateFormat("yyyy-MM").format(new Date().toString());
//
//    }

    private static String getImageObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
//        String dataString = getDataString();
        return String.format("images/%s.jpg",fileMd5);
    }

    private static String getPortraitObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
//        String dataString = getDataString();
        return String.format("portrait/%s.jpg",fileMd5);
    }

    private static String getAudioObjKey(String path){
        String fileMd5 = HashUtil.getMD5String(new File(path));
//        String dataString = getDataString();
        return String.format("audio/%s.mp3",fileMd5);
    }

}
