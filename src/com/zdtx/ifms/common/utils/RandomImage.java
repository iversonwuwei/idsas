package com.zdtx.ifms.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * @title 随机验证码生成
 * @author LiuGuilong
 * @since 2012-3-21
 */
public class RandomImage {

	private static int width = 80;
	private static int height = 25;
	private static int count = 25;

	private static Random random = new Random();
	private static char[] randomCode = "ABCDEFGHJKLMNPQRSTUVWXY2345678".toCharArray();
	private static Font font = new Font(Font.DIALOG, Font.LAYOUT_NO_LIMIT_CONTEXT, 20);

	private ByteArrayInputStream image;// 验证码图片
	private String code;// 验证码

	private RandomImage() {
		initialize();
	}

	/**
	 * @param 创建该类
	 * @return
	 */
	public static RandomImage Instance() {
		return new RandomImage();
	}

	/**
	 * @param 返回图片
	 * @return
	 */
	public ByteArrayInputStream getImage() {
		return this.image;
	}

	/**
	 * @param 返回验证码
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	public void initialize() {
		try {

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graph = image.getGraphics();// 图形上下文
			graph.setColor(Color.WHITE);
					//getRandomColor(200, 250));// 设定背景色
			graph.fillRect(0, 0, width, height);
			graph.setFont(font); // 设定字体
			graph.setColor(getRandomColor(160, 200));
			// 画干扰线
			for (int i = 0; i < count; i++) {
				graph.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width)
						+ random.nextInt(5), random.nextInt(height) + random.nextInt(5));
			}
			String $ = "";
			for (int i = 0; i < 4; i++) {
				int r = random.nextInt(randomCode.length);
				$ += randomCode[r];
				graph.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				graph.drawString("" + randomCode[r], font.getSize() * i, 20);
			}
			this.code = $;// 赋值验证码
			graph.dispose();// 图象生效

			ByteArrayInputStream in = null;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
				ImageIO.write(image, "JPEG", imageOut);
				imageOut.close();
				in = new ByteArrayInputStream(out.toByteArray());
			} catch (Exception e) {
				System.err.println("验证码生成错误：" + e.toString());
			}
			this.image = in;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param 随机生成颜色
	 * @param color_b
	 * @param color_e
	 * @return
	 */
	private Color getRandomColor(int color_b, int color_e) {
		color_b = color_b > 255 ? 255 : color_b;
		color_e = color_e > 255 ? 255 : color_e;
		return new Color(color_b + random.nextInt(color_e - color_b), color_b + random.nextInt(color_e - color_b),
				color_b + random.nextInt(color_e - color_b));
	}
}