package com.sinohealth.eszservice.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtil {

	// 复制文件及文件夹
	public void copyField(String source, String dest) {
		File sourceFile = new File(source);
		// 文件夹复制
		if (sourceFile.isDirectory()) {
			String[] names = sourceFile.list();
			File file = new File(dest + "/" + sourceFile.getName());
			if (!file.exists()) {
				file.mkdir();
			}
			for (String name : names) {
				copyField(source + "/" + name, file.getPath());
			}
		}
		// 文件复制
		else {
			this.copyFile(source, dest);
		}
	}

	// 删除文件及文件夹
	public void deleteField(String source) {
		File sourceFile = new File(source);
		// 文件夹
		if (sourceFile.isDirectory()) {
			String[] names = sourceFile.list();
			// 删除当前文件夹下面的子文件和子文件夹
			for (String name : names) {
				deleteField(source + "/" + name);
			}
			// 删除当前文件夹
			sourceFile.delete();
		}
		// 文件
		else {
			sourceFile.delete();
		}
	}

	// 剪切文件及文件夹
	public void cutField(String source, String dest) {
		// 复制文件夹
		// 删除文件夹
	}

	// 从网络上下载资源
	public void download(URL url) {
		try {
			InputStream is = url.openStream();
			FileOutputStream fos = new FileOutputStream("e:/123.gif");
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			byte[] b = new byte[1024];
			int len = 0;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}

			bos.close();
			bis.close();
			fos.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readFileByBytes(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读一个字节：");
			// 一次读一个字节
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {
				System.out.write(tempbyte);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			// ReadFromFile.showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("以字符为单位读取文件内容，一次读多个字节：");
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static String readFileByLines(String fileName) {
		String str = "";
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return str;
	}

	public static String readFileByLines(File file) {
		String str = "";

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "utf-8"));
			// reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				str += tempString + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return str;
	}

	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stringToFile(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			// FileWriter writer = new FileWriter(fileName, true);
//			FileWriter writer = new FileWriter(fileName);
//			writer.write(content);
//			writer.close();
			
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName, false),"UTF-8");
			osw.write(content);
			
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ==========================================================================
	 * ================================
	 */
	private static String message;
	private static String PATH_SEPARATOR = "\\";

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readTxt(String filePathAndName, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + " ");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            目录
	 * @return 返回目录创建后的路径
	 */
	public static String createFolder(String folderPath) {
		String txt = folderPath;
		try {
			java.io.File myFilePath = new java.io.File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			message = "创建目录操作出错";
		}
		return txt;
	}

	public static void createFolder(File folderPath) {
		try {

			if (!folderPath.exists()) {
				folderPath.mkdir();
			}
		} catch (Exception e) {
			message = "创建目录操作出错";
		}
	}

	/**
	 * 多级目录创建
	 * 
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfac
	 */
	public static String createFolders(String folderPath, String paths) {
		String txts = folderPath;
		try {
			String txt;
			txts = folderPath;
			StringTokenizer st = new StringTokenizer(paths, "|");
			for (int i = 0; st.hasMoreTokens(); i++) {
				txt = st.nextToken().trim();
				if (txts.lastIndexOf("/") != -1) {
					txts = createFolder(txts + txt);
				} else {
					txts = createFolder(txts + txt + "/");
				}
			}
		} catch (Exception e) {
			message = "创建目录操作出错！";
		}
		return txts;
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public static void createFile(String filePathAndName) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			OutputStreamWriter resultFile = new OutputStreamWriter(
					new FileOutputStream(filePathAndName), "UTF-8");
			@SuppressWarnings("unused")
			PrintWriter myFile = new PrintWriter(resultFile);
			resultFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
			System.out.println(message + e.getMessage());
		}
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public static void createFile(String filePathAndName, String fileContent) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			OutputStreamWriter resultFile = new OutputStreamWriter(
					new FileOutputStream(filePathAndName), "UTF-8");
			PrintWriter myFile = new PrintWriter(resultFile);

			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
			resultFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
			System.out.println(message + e.getMessage());
		}
	}

	/**
	 * 有编码方式的文件创建
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @param encoding
	 *            编码方式 例如 GBK 或者 UTF-8
	 * @return
	 */
	public static void createFile(String filePathAndName, String fileContent,
			String encoding) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			PrintWriter myFile = new PrintWriter(myFilePath, encoding);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
		}
	}

	/**
	 * 新建文件及文件所在的目录
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return
	 */
	public static void createFolderAandFile(String filePathAndName) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			// 获取传入文件的目录路径
			String folders = myFilePath.getParent();
			String[] catalogs = folders.replace('\\', '/').split("/");
			// 从盘符的根目录开始检查，没有则创建
			if (catalogs.length >= 3) {
				String newCatalog = catalogs[0] + "\\" + catalogs[1];
				FileUtil.createFolder(newCatalog);
				for (int i = 2; i < catalogs.length; i++) {
					newCatalog = newCatalog + "\\" + catalogs[i];
					FileUtil.createFolder(newCatalog);
				}
			}

			// 如果去除文件名的父目录级均存在则创建文件对应文件
			System.out.println(myFilePath.getParentFile());
			System.out.println(myFilePath.getParentFile().exists());
			if (myFilePath.getParentFile().exists()) {
				OutputStreamWriter resultFile = new OutputStreamWriter(
						new FileOutputStream(filePathAndName), "UTF-8");
				@SuppressWarnings("unused")
				PrintWriter myFile = new PrintWriter(resultFile);
				resultFile.close();
			}

		} catch (Exception e) {
			message = "创建文件操作出错";
			System.out.println(message + e.getMessage());
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true遭遇异常返回false
	 */
	public static boolean delFile(String filePathAndName) {
		boolean bea = false;
		try {
			String filePath = filePathAndName;
			File myDelFile = new File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
				bea = true;
			} else {
				bea = false;
				message = (filePathAndName + "删除文件操作出错");
			}
		} catch (Exception e) {
			message = e.toString();
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			message = ("删除文件夹操作出错");
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				bea = true;
			}
		}
		return bea;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名
	 * @return
	 */
	public static void copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			message = ("复制单个文件操作出错");
		}
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			message = "复制整个文件夹内容操作出错";
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	@SuppressWarnings("static-access")
	public String getMessage() {
		return this.message;
	}

	/**
	 * 将路径格式化成标准形式 1. 去除../ 2. 统一路径分隔符号 3. 结果和实际文件的大小写一致
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 消除路径中可能存在../等
	 */
	public static String formatFilePath(String filePath) {
		filePath = filePath.replace("/", "\\").trim();
		// w00114592 20090819 对于D:这样的路径,JDK无法正确识别,必须写成D:\形式才能正确处理
		if (filePath.endsWith(":")) {
			filePath = filePath + FileUtil.PATH_SEPARATOR;
		}

		// 如果包含"...",就进行代换处理.
		// l00150302 2010-10-27 DTS2010102800679
		if (filePath.contains("...")) {
			if (filePath.startsWith("...")) {
				filePath = "\\" + filePath;
			}

			if (filePath.endsWith("...")) {
				filePath = filePath + "\\";
			}

			// 对连续的超过3个的点符号,替换成2个,这样出来的结果和C#的处理一致
			filePath = filePath.replace("\\", "\\\\")
					.replaceAll("\\\\(\\.){3,}\\\\", "\\\\..\\\\").trim()
					.replace("\\\\", "\\");
		}

		File f = new File(filePath);

		String rtnValue = filePath;

		try {
			rtnValue = f.getCanonicalPath();
		} catch (IOException e) {

		}

		// w00114592 20090819 如果格式化的是一个路径,那么去掉路径后面的分隔符,保证处理的一致性
		if (f.isDirectory() && rtnValue.endsWith(FileUtil.PATH_SEPARATOR)) {
			rtnValue = rtnValue.substring(0, rtnValue.length() - 1);
		}

		return rtnValue;
	}

	/**
	 * 收集指定目录下的XML文件路径
	 * 
	 * @param filePath
	 *            指定目录路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAbsolutePath(String filePath,
			List<String> list) {

		File f = new File(filePath);
		File[] fileDes = f.listFiles();
		for (File file : fileDes) {
			String strFile = file.getAbsolutePath();
			if (strFile.endsWith(".xml")) {
				list.add(strFile);
			} else {
				if (!strFile.endsWith(".bak")) {
					getAbsolutePath(strFile, list);
				}
			}
		}
		return list;
	}

	/**
	 * 返回文件名
	 * 
	 * @param str
	 * @return
	 */
	public static String getFileName(String str) {
		String strReturn = "";
		strReturn = str.substring(str.lastIndexOf("\\"), str.length());
		return strReturn;
	}

	public static void main(String[] args) {
		String filePath = "F:\\Favorite\\longx\\duoduo/long\\gege88\\876/kkk/logtest.xml";
		FileUtil.createFolderAandFile(filePath);
	}
	

//	public HttpServletResponse download(String path,HttpServletResponse response) {
//		try {
//			// path是指欲下载的文件的路径。
//			File file = new File(path);
//			// 取得文件名。
//			String filename = file.getName();
//			// 取得文件的后缀名。
//			String ext = filename.substring(filename.lastIndexOf(".") + 1)
//					.toUpperCase();
//
//			// 以流的形式下载文件。
//			InputStream fis = new BufferedInputStream(new FileInputStream(path));
//			byte[] buffer = new byte[fis.available()];
//			fis.read(buffer);
//			fis.close();
//			// 清空response
//			response.reset();
//			// 设置response的Header
//			response.addHeader("Content-Disposition", "attachment;filename="
//					+ new String(filename.getBytes()));
//			response.addHeader("Content-Length", "" + file.length());
//			OutputStream toClient = new BufferedOutputStream(
//					response.getOutputStream());
//			response.setContentType("application/octet-stream");
//			toClient.write(buffer);
//			toClient.flush();
//			toClient.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		return response;
//	}
//
//	public void downloadLocal(HttpServletResponse response)
//			throws FileNotFoundException {
//		// 下载本地文件
//		String fileName = "Operator.doc".toString(); // 文件的默认保存名
//		// 读到流中
//		InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
//		// 设置输出的格式
//		response.reset();
//		response.setContentType("bin");
//		response.addHeader("Content-Disposition", "attachment; filename=\""
//				+ fileName + "\"");
//		// 循环取出流中的数据
//		byte[] b = new byte[100];
//		int len;
//		try {
//			while ((len = inStream.read(b)) > 0)
//				response.getOutputStream().write(b, 0, len);
//			inStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void downloadNet(HttpServletResponse response)
//			throws MalformedURLException {
//		// 下载网络文件
//		int bytesum = 0;
//		int byteread = 0;
//
//		URL url = new URL("windine.blogdriver.com/logo.gif");
//
//		try {
//			URLConnection conn = url.openConnection();
//			InputStream inStream = conn.getInputStream();
//			FileOutputStream fs = new FileOutputStream("c:/abc.gif");
//
//			byte[] buffer = new byte[1204];
//			int length;
//			while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
//				System.out.println(bytesum);
//				fs.write(buffer, 0, byteread);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 支持在线打开文件的一种方式
//	public void downLoad(String filePath, HttpServletResponse response,
//			boolean isOnLine) throws Exception {
//		File f = new File(filePath);
//		if (!f.exists()) {
//			response.sendError(404, "File not found!");
//			return;
//		}
//		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
//		byte[] buf = new byte[1024];
//		int len = 0;
//
//		response.reset(); // 非常重要
//		if (isOnLine) { // 在线打开方式
//			URL u = new URL("file:///" + filePath);
//			response.setContentType(u.openConnection().getContentType());
//			response.setHeader("Content-Disposition",
//					"inline; filename=" + f.getName());
//			// 文件名应该编码成UTF-8
//		} else { // 纯下载方式
//			response.setContentType("application/x-msdownload");
//			response.setHeader("Content-Disposition", "attachment; filename="
//					+ f.getName());
//		}
//		OutputStream out = response.getOutputStream();
//		while ((len = br.read(buf)) > 0)
//			out.write(buf, 0, len);
//		br.close();
//		out.close();
//	}

}
