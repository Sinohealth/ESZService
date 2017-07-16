package com.sinohealth.eszservice.common.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 */
public class StringUtil {

	/**
	 * bytes数组转为hex，比如 new byte[] {0x01,0x02,0x03,0x04,0x05}将转换为 "0102030405"
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer("");
		ByteBuffer bf = ByteBuffer.wrap(b);
		byte sb;
		while (bf.hasRemaining()) {
			sb = bf.get();
			String hex = Integer.toHexString(sb & 0xFF);
			ret.append((hex.length() == 1) ? "0" + hex : hex);
		}
		return ret.toString().toUpperCase().trim();
	}

	/**
	 * hex字符串转换为Byte值，比如 "0102030405"将转换为 new byte[] {0x01,0x02,0x03,0x04,0x05}
	 * 
	 * @param str
	 *            Byte字符串，每个Byte之间没有分隔符
	 * @return byte[] 返回的16进制数组
	 */
	public static byte[] hexString2Bytes(String str) {
		int l = str.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			ret[i] = Integer.valueOf(str.substring(i * 2, i * 2 + 2), 16)
					.byteValue();
		}
		return ret;
	}

	/**
	 * byte[]转换为十进制字符串，比如 "{0x01,0x02}"将转换为 "12"
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String bytes2String(byte[] b) {
		ByteBuffer bf = ByteBuffer.wrap(b);
		byte bt;
		StringBuffer sb = new StringBuffer();
		while (bf.hasRemaining()) {
			bt = bf.get();
			sb.append(Integer.toString(bt & 0xff));
		}
		bf.clear();
		return sb.toString();
	}

	/**
	 * @param s
	 *            "[{\"EPC\":\"0010020\",\"METHOD\":\"SDS\"}]"
	 * @return 
	 *         5b7b22455043223a2230303130303230222c224d4554484f44223a22534453227d5d
	 */
	public static String toHexString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			sb.append(Integer.toHexString(ch));
		}
		return sb.toString();
	}

	/**
	 * @param s
	 *            5
	 *            b7b22455043223a2230303130303230222c224d4554484f44223a22534453227d5d
	 * @return "[{\"EPC\":\"0010020\",\"METHOD\":\"SDS\"}]"
	 */
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		try {
			for (int i = 0; i < baKeyword.length; i++) {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			}
			s = new String(baKeyword, "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 十进制转化为16，同时错字节位。用于在报文中，计算用户数据长度字段
	 * 
	 * @param a
	 *            十进制
	 * @return 16进制
	 */
	public static String To16(int a) {
		StringBuffer sb = new StringBuffer();
		String s;
		String as = Integer.toHexString(a);
		if (a <= 15) {
			sb.append("0" + as).append("00");
		} else if (a <= 255) {
			sb.append(as).append("00");
		} else if ((s = a + "").trim().length() == 3) {
			sb.append("0" + as.substring(2)).append(as.substring(0, 2));
		} else {
			sb.append(as.substring(2)).append(as.substring(0, 2));
		}
		return sb.toString();
	}

	/**
	 * 二进制字符串转化为16进制字符串
	 * 
	 * @param bString
	 *            二进制字符串
	 * @return 16进制字符串
	 */
	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	/**
	 * 16进制字符串转化为2进制字符串
	 * 
	 * @param hexString
	 *            16进制字符串
	 * @return 2进制字符串
	 */
	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 去掉字符串回车换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceEnter(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 随机生成7位数字的随机码
	 * 
	 * @return
	 */
	public static String getRandom() {
		int result = (int) ((Math.random() * 9 + 1) * 1000000);
		return String.valueOf(result);
	}

	/**
	 * 验证是否数字格式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(s);
		return isNum.matches();
	}
}