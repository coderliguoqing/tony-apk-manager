package com.tony.admin.web.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CryptoUtil {

	private static KeyGenerator keyGenerator;
	private static Cipher cipher;

	private static class SingletonHolder {
		/**
		 * 单例对象实例
		 */
		static final CryptoUtil INSTANCE = new CryptoUtil();
	}

	public static CryptoUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	// 禁止实例化
	private CryptoUtil() {
	}

	/**
	 * 获取源文件md5
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public String md5(byte[] source) throws NoSuchAlgorithmException {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		md.update(source);
		byte tmp[] = md.digest();
		char str[] = new char[32];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			str[k++] = hexDigits[tmp[i] >>> 4 & 0xf];
			str[k++] = hexDigits[tmp[i] & 0xf];
		}
		return new String(str);
	}

	/**
	 * HmacSHA1 加密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            秘钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public String hmacSHA1(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] keyBytes = key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : rawHmac) {
			sb.append(byteToHexString(b));
		}
		return sb.toString();
	}

	private String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0f];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	public byte[] hmacSHA1Encrypt(String encryptText, String encryptKey)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = encryptKey.getBytes("UTF-8");
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance("HmacSHA1");
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes("UTF-8");
		byte[] rawHmac = mac.doFinal(text);

		// 完成 Mac 操作
		return rawHmac;
	}

	public static byte[] file2Byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static String bytesToHexString(byte[] src){
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}
	
	//对称加密速度对比
/*	public static void main(String[] args) throws Exception{
		//生成秘钥
		String seed = "bluemoon";
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(seed.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

		String str = "1|/cloudDisk/file/12345678901234567890123456789012|1923534|212384534|拉卡机老地方见辣椒粉蜡健康减肥垃圾筐拉法基留房间奥拉夫拉克丝进来发进来发金卡价房贷啦时间浪费静安寺留房间按量急垃圾老地方见卡剪了发简历罚款设计费拉数据量发酵饲料放假啦说法是两间房间阿斯利康放假啊酸辣粉机.xml";
		//AES加密
		long startAES = System.currentTimeMillis();
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        byte[] byteContent = str.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] byteAES = cipher.doFinal(byteContent);
        long endAES = System.currentTimeMillis();
        System.out.println("AES加密时间:" + (endAES - startAES));
        String aes = new String(Base64.getEncoder().encodeToString(byteAES));
        String url_en_aes = URLEncoder.encode(aes, "utf-8");
        System.out.println(URLEncoder.encode(aes, "utf-8"));

		//AES解密
        Cipher cipherDe = Cipher.getInstance("AES");// 创建密码器
        cipherDe.init(Cipher.DECRYPT_MODE, key);// 初始化
        long startAESDe = System.currentTimeMillis();
		String url_de_aes = URLDecoder.decode(url_en_aes, "utf-8");
		byte[] base64_de = Base64.getDecoder().decode(url_de_aes);
        byte[] byteAESDe = cipherDe.doFinal(base64_de);
        long endAESDe = System.currentTimeMillis();
        System.out.println("AES解密时间:" + (endAESDe - startAESDe));
//        String test = new String(byteAESDe);
//        System.out.println(test);
		System.out.println(new String(byteAESDe));

		//Blowfish秘钥生成
		KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(32,new SecureRandom(seed.getBytes()));
        SecretKey BlowfishKye = keyGenerator.generateKey();

		//Blowfish加密
        long startBlowfish = System.currentTimeMillis();
		Cipher cipherBlowfish = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipherBlowfish.init(cipherBlowfish.ENCRYPT_MODE, BlowfishKye);
        byte[] byteBlowfish = cipherBlowfish.doFinal(str.getBytes());
        long endBlowfish = System.currentTimeMillis();
        System.out.println("blowfish加密时间为:" + (endBlowfish - startBlowfish));
        String blowfish = new String(Base64.getEncoder().encodeToString(byteBlowfish));
//        System.out.println(byteBlowfish);
		System.out.println(URLEncoder.encode(blowfish, "utf-8"));

		//Blowfish解密
        long startBlowfishDe = System.currentTimeMillis();
        Cipher cipherBlowfishDe = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipherBlowfishDe.init(cipherBlowfishDe.DECRYPT_MODE, BlowfishKye);
        byte[] byteBlowfishDe = cipherBlowfishDe.doFinal(byteBlowfish);
        long endBlowfishDe = System.currentTimeMillis();
        System.out.println("blowfish解密时间:" + (endBlowfishDe - startBlowfishDe));
        System.out.println(new String(byteBlowfishDe));
	}*/

	public static KeyGenerator getAesKeyGenerator() throws NoSuchAlgorithmException {
		if (CryptoUtil.keyGenerator == null) {
			CryptoUtil.keyGenerator = KeyGenerator.getInstance("AES");
		}
		return CryptoUtil.keyGenerator;
	}

	public static Cipher getAesCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
		if (CryptoUtil.cipher == null) {
			CryptoUtil.cipher = Cipher.getInstance("AES");
		}
		return CryptoUtil.cipher;
	}

	/**
	 * AES encrypt and decrypt
	 *
	 * @param contentBytes wating to be encrypted or decrypted byte array
	 * @param keyBytes secret key's byte array
	 * @param mode AES's mode, default value is Cipher.ENCRYPT_MODE
	 *
	 * @return encrypted/decrypted byte array
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] aesEncryptAndDecrypt(byte[] contentBytes, byte[] keyBytes, Integer mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		/* set default mode */
		if (mode == null) {
			mode = Cipher.ENCRYPT_MODE;
		}
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(keyBytes);
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, random);
		SecretKey secretKey = kgen.generateKey();
		Cipher cipher = CryptoUtil.getAesCipher();
		cipher.init(mode, secretKey);
		return cipher.doFinal(contentBytes);
	}

	/**
	 * AES encrypt
	 *
	 * @param content normal content String wating to encrypt
	 * @param key secret key
	 *
	 * @return encrypted byte array
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] aesEncrypt(String content, String key) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (StringUtils.isBlank(content) || StringUtils.isBlank(key)) {
			new NullPointerException();
		}
		byte[] contentBytes = content.getBytes();
		byte[] keyBytes = key.getBytes();
		return aesEncryptAndDecrypt(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
	}

	/**
	 * AES decrypt
	 *
	 * @param hexContentStr hexadecimal content wating to decrypt
	 * @param key secret key
	 *
	 * @return decrypted byte array
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] aesDecrypt(String hexContentStr, String key) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (StringUtils.isBlank(hexContentStr) || StringUtils.isBlank(key)) {
			new NullPointerException();
		}
		byte[] contentBytes = hexStringToBytes(hexContentStr);
		byte[] keyBytes = key.getBytes();
		return aesEncryptAndDecrypt(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
	}

	/**
	 * AES encrypt, return hexadecimal String
	 *
	 * @param normalContentStr normal content String wating to encrypt
	 * @param key secret key(normal String)
	 *
	 * @return encrypted hexadecimal String
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static String aesEncryptToHexStr(String normalContentStr, String key) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (StringUtils.isBlank(normalContentStr) || StringUtils.isBlank(key)) {
			new NullPointerException();
		}
		byte[] contentBytes = normalContentStr.getBytes();
		byte[] keyBytes = key.getBytes();
		return bytesToHexString(aesEncryptAndDecrypt(contentBytes, keyBytes, Cipher.ENCRYPT_MODE));
	}

	/**
	 * AES decrypt, return normal String
	 *
	 * @param hexContent hexadecimal content wating to decrypt
	 * @param key secret key(normal String)
	 *
	 * @return decrypted normal String
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static String aesDecryptToNormalStr(String hexContent, String key) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		if (StringUtils.isBlank(hexContent) || StringUtils.isBlank(key)) {
			new NullPointerException();
		}
		byte[] contentBytes = hexStringToBytes(hexContent);
		byte[] keyBytes = key.getBytes();
		return new String(aesEncryptAndDecrypt(contentBytes, keyBytes, Cipher.DECRYPT_MODE));
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
