package com.tony.admin.web.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import cn.hutool.core.codec.Base64Encoder;

/**
 * 验证码生成工具类
 * @author Guoqing.Lee
 * @date 2019年4月2日 上午11:05:52
 *
 */
public class VerifyCodeUtils {
	
	public static final String VERIFY_CODE = "VERIFY_CODE_";
	
	public static void main(String[] args) throws IOException {
		String code = getRandKey(4);
		System.out.println(verifyCode(90, 25, code));
	}
	
	/**
	 * 生成指定长度的随机验证码图片
	 * @param w		图片宽度
	 * @param h		图片高度
	 * @param code	验证码
	 * @return		base64的验证码图片
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static String verifyCode(int w, int h, String code) throws IOException {
        Base64Encoder encoder = new Base64Encoder();
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        outputImage(w, h, data, code);
        String img = encoder.encode(data.toByteArray());
        return img;
    }
	
	/**
	 * 生成随机验证码
	 * @param length
	 * @return
	 */
	public static String getRandKey(int length) {
		Random random = new Random();
		StringBuffer sRand = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand.append(rand);
		}
		return sRand.toString();
	}

	/**
	 * 生成指定验证码图片流
	 * @param width		图片宽度
	 * @param height	图片高度
	 * @param randKey	验证码的key
	 * @param os		图片流
	 * @throws IOException
	 */
	public static void outputImage(int width, int height, OutputStream os, String randKey) throws IOException {
		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		//生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		//设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		//画边框
		//g.setColor(new Color());
		//g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		int verifySize = randKey.length();
		for (int i = 0; i < verifySize; i++) {
			String sRand = randKey.substring(i, i+1);
			String rand = String.valueOf(sRand);
			// 将认证码显示到图象中
			//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			//13   16   调整图片数字的位置
			g.drawString(rand, 21 * i + 6, 20);
		} 

		// 图象生效
		g.dispose();
		// 输出图象到页面
		ImageIO.write(image, "JPEG", os);
	}
	
	/**
	 * 给定范围获取随机颜色
	 * @param fc   前景色
	 * @param bc   背景色
	 * @return
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
