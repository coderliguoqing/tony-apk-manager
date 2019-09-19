package com.tony.admin.web.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.tony.admin.web.common.config.CloudUploadConfig;
import org.apache.http.HttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author huangkaifu
 * @date 2018/5/24.
 */
public class BMCloudStoreUtil {
	
	private static CloudUploadConfig cloudUploadConfig = (CloudUploadConfig) SpringContextUtil.getApplicationContext().getBean("cloudUploadConfig");

    /**
     * 简单文件上传
     */
    static JSONObject simpleUpload(File file, boolean isSecret) throws Exception {
        Assert.isTrue(file.exists(), "文件( " + file.getName() + ")不存在");
        String fileName = file.getName();
        String smallFileMD5 = Md5Util.getMD5ByFile(file);

        Map<String, File> files = new HashMap<>(2);
        files.put("file", file);
        /*添加请求参数*/
        Map<String, Object> paramsInToken = new HashMap<>(2);
        paramsInToken.put("fileMD5", smallFileMD5);
        paramsInToken.put("fileName", fileName);
        paramsInToken.put("isSecret", isSecret);
        /*创建上传token*/
        String token = createNSecondsUploadToken(cloudUploadConfig.getSecretKey(), 1000, paramsInToken);
        Map<String, String> params = new HashMap<>(2);
        params.put("appId", cloudUploadConfig.getAppID());
        params.put("t", token);

        HttpResponse httpResponse = HttpUtils.sendPost(cloudUploadConfig.getSimpleUploadEntry(), params, files, null);
        String charsetOfResponse = "UTF-8";
        String responseStr = HttpUtils.getResponseStr(httpResponse, charsetOfResponse);
        Assert.isTrue(org.apache.commons.lang3.StringUtils.isNotBlank(responseStr),
                "Fail to upload file simplely, because the response is null. The url of file is " + file.getName());
        JSONObject simpleUploadResultJsonObj = JSON.parseObject(responseStr);
        Integer status = simpleUploadResultJsonObj.getInteger("status");
        Boolean isSuccess = simpleUploadResultJsonObj.getBoolean("isSuccess");
        Assert.isTrue((status != null && status == 200) || (isSuccess != null && isSuccess), "请求失败, response = :" + responseStr);
        JSONObject data = simpleUploadResultJsonObj.getJSONObject("data");
        data.put("fileMD5", smallFileMD5);
        String picUrl = cloudUploadConfig.getBmCloud() + data.getString("uri");
        data.put("picUrl", picUrl);
        return simpleUploadResultJsonObj;
    }
    
    /**
     * 生成n秒有效期的token
     *
     * @param secretKey aes密钥
     * @param n token有效期，单位：s(秒second）
     * @param parmas 需要添加到token中的参数
     *
     * @return token字串
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static String createNSecondsUploadToken(String secretKey, int n, Map<String, Object> parmas) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        if (ObjectUtils.isEmpty(parmas)) {
            parmas = new HashMap<String, Object>(2);
        }
        parmas.put("deadline", System.currentTimeMillis()/1000 + n);
        parmas.put("a", System.currentTimeMillis());
        JSONObject paramsJsonObj = new JSONObject(parmas);
        String token = createToken(paramsJsonObj.toJSONString(), secretKey);
        System.out.println("new token : " + token);
        return token;
    }
    
    /**
     * 生成token字串
     * @param paramsJsonStr 需要添加到token中的参数字串，json格式
     * @param secretKey aes密钥
     *
     * @return token字串
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     */
    public static String createToken(String paramsJsonStr, String secretKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        byte[] aesEncryptedBytes = CryptoUtil.aesEncryptAndDecrypt(paramsJsonStr.getBytes(), secretKey.getBytes(), Cipher.ENCRYPT_MODE);
        String safeUrlBase64Str = Base64.getUrlEncoder().encodeToString(aesEncryptedBytes);
        return URLEncoder.encode(safeUrlBase64Str, "utf-8");
    }
    
    

}
