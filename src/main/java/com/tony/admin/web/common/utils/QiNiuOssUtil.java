package com.tony.admin.web.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.tony.admin.web.common.config.QiNiuOssConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Guoqing
 * @desc 七牛OSS 存储上传
 * @date 2019/8/8
 */
public class QiNiuOssUtil {

    private static QiNiuOssConfig qiNiuOssConfig = (QiNiuOssConfig)SpringContextUtil.getBean("qiNiuOssConfig");

    /**
     * 获取域名地址
     * @return
     */
    public static String getPrefixUrl(){
        return qiNiuOssConfig.getPrefixUrl();
    }

    /**
     * 流文件上传
     * @param bucket  存储空间名称；不指定，使用默认的存储空间
     * @param key   文件名
     * @param data  文件流
     * @return
     */
    public static DefaultPutRet upload(String bucket, String key, byte[] data){
        /**
         * 构造一个带指定Zone对象的配置类
         * 华东	Zone.zone0()
         * 华北	Zone.zone1()
         * 华南	Zone.zone2()
         * 北美	Zone.zoneNa0()
         * 东南亚	Zone.zoneAs0()
         */
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证，然后准备上传
        String accessKey = qiNiuOssConfig.getAccessKey();
        String secretKey = qiNiuOssConfig.getSecretKey();
        if(StringUtils.isEmpty(bucket)){
            bucket = qiNiuOssConfig.getBucket();
        }
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(data, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet;
        }catch (QiniuException e){
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
//        //...其他参数参考类注释
//
//        UploadManager uploadManager = new UploadManager(cfg);
//        //...生成上传凭证，然后准备上传
//        String accessKey = "z28CT52ky38V2lxQLzm6cY67k18swi06fTV22vIe";
//        String secretKey = "QBD80YSO78CgbFkPA3bSWPeN85svJkzUFyVIWs4K";
//        String bucket = "guoqing";
//        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "C://Users//liguo//Downloads//fun.ico";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = "fun.ico";
//
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//
//        try {
//            Response response = uploadManager.put(localFilePath, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//
//    }
}
