package com.sinohealth.eszservice.common.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class QRCode {

	public static void main(String[] args) {
//		createQRCode("http://www.baidu.com", "f:\\qrcode\\qrcode.jpg", "f:\\qrcode\\heihei.png");
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            二维码图片的内容
	 * @param qrcodePath
	 *            生成二维码图片完整的路径
	 * @param logoPath
	 *            二维码图片中间的logo路径
	 */
	public static int createQRCode(String content, String qrcodePath,InputStream logoIs) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(21);

			// System.out.println(content);
			byte[] contentBytes = content.getBytes("utf-8");
			BufferedImage bufImg = new BufferedImage(306, 306, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();

			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 306, 306);
			
			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);

			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 120) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				System.err.println("QRCode content bytes length = "
					+ contentBytes.length + " not in [ 0,120 ]. ");
				return -1;
			}
			//读取logo图片。
			ByteArrayInputStream bais = new ByteArrayInputStream(input2byte(logoIs));
			BufferedImage bi1 =ImageIO.read(bais);
			
//			Image img = ImageIO.read(new File(ccbPath));//实例化一个Image对象。
			//读取图片，设置图片在二维码中起始位置。
			gs.drawImage(bi1, 113, 113, null);
			gs.dispose();
			bufImg.flush();

			// 生成二维码QRCode图片
			File imgFile = new File(qrcodePath);
			ImageIO.write(bufImg, "jpg", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
			return -100;
		}
		return 0;
	}
	
    public static final byte[] input2byte(InputStream inStream)   
            throws IOException {   
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();   
        byte[] buff = new byte[100];   
        int rc = 0;   
        while ((rc = inStream.read(buff, 0, 100)) > 0) {   
            swapStream.write(buff, 0, rc);   
        }   
        byte[] in2b = swapStream.toByteArray();   
        return in2b;   
    } 

}
