package com.tony.admin.web.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密类
 */
public class Md5Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);

    protected static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Md5Util(){}

    /**
     * 将二进制数据进行md5加密
     *
     * @param data 文件二进制数据
     * @return md5加密码
     */
    public static String getMd5ByByte(byte[] data) {
        try {
            char[] str;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(data);
            byte[] md = mdTemp.digest();
            int j = md.length;
            str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            LOGGER.error("Error occurred when making MD5 for data file", e);
            return null;
        }
    }

    /**
     * 获取文件MD5值
     *
     * @param file 文件对象
     * @return
     */
    public static String getMD5ByFile(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
//            System.out.println("开始算");
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
//            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage(), ex);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info(e.getMessage(), e);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                LOGGER.info(ex.getMessage(), ex);
            }
        }
    }

    /**
     * bytesToString
     *
     * @param data
     * @return
     */
    public static String bytesToString(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }
}