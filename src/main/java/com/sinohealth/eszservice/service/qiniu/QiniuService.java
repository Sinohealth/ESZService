package com.sinohealth.eszservice.service.qiniu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.TimeZone;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.DigestAuth;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.net.EncodeUtils;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.url.URLEscape;
import com.sinohealth.eszservice.common.config.Global;

/**
 * 七牛
 * 
 * @author 黄洪根 2015-03-23
 */
public class QiniuService {
	/**
	 * 根据存储空间的名字返回token值,用于上传文件
	 * 
	 * @param space
	 * @return
	 */
	public static String getUploadToken(String space) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);

		PutPolicy putPolicy = new PutPolicy(space);

		String token = "{\"token\":\"\"}";

		try {
			token = "{\"token\":\""+putPolicy.token(mac)+"\"}";
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return token;
	}

    /**
     * 根据存储空间的名字,以及文件原始路径返回加密空间的文件访问路径
     * @param space
     * @param url
     * @return
     */
	public static String getDownloadUrl(Space space,String url) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space.toString().toLowerCase() + ".domain");
		String httpsDomain = Global.getConfig("qiniu." + space.toString().toLowerCase() + ".https");

		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space.toString().toLowerCase() + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(httpsDomain).append(URLEscape.escape(filename));

			urlToSign.append("?e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}
	
	public static String getDownloadUrl(String space,String url) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space + ".domain");
		String httpsDomain = Global.getConfig("qiniu." + space + ".https");

		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "{\"url\":\"\"}";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(httpsDomain).append(URLEscape.escape(filename));

			urlToSign.append("?e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();
			downloadUrl = "{\"url\":\""+downloadUrl+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}
	
	public static String getDownloadUrlStr(String space,String url) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space + ".domain");
		String httpsDomain = Global.getConfig("qiniu." + space + ".https");

		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(httpsDomain).append(URLEscape.escape(filename));

			urlToSign.append("?e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}

	public static String getDownloadUrlStr(Space space,String url) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space.toString().toLowerCase() + ".domain");
		String httpsDomain = Global.getConfig("qiniu." + space.toString().toLowerCase() + ".https");
		
		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space.toString().toLowerCase() + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(httpsDomain).append(URLEscape.escape(filename));

			urlToSign.append("?e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}
	
	public static String getHeadshotUrl(String space,String url) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space + ".domain");
		String httpsDomain = Global.getConfig("qiniu." + space + ".https");
		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(httpsDomain).append(URLEscape.escape(filename));

			urlToSign.append("?e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}
	
	/*生成私有空间的缩略图*/
	public static String zoomOut(String space,String bigPic,Integer smallWidth,Integer smallHeight){
		String json = "{\"url\":\"\"}";
		//获取相关配置信息
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space + ".domain");
		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);

		String bucketName=space;
		//获取原图信息
		ImageInfo imageInfo = getImageInfo(space,bigPic);
		
		String smallPic = "";
		if(imageInfo != null){
			Integer bigWidth = imageInfo.getWidth();
			Integer bigHeight = imageInfo.getHeight();
			String extName = bigPic.substring(bigPic.lastIndexOf("."));
			String bigPicName = bigPic.replaceAll(domain, "");
			String smallPicName = bigPicName.replaceAll(extName, "")+"_s"+extName;
			String entryURI = bucketName+":"+smallPicName;
			String encodedEntryURI = EncodeUtils.urlsafeEncode(entryURI);
			String fromURI = "";
			
			if((float)bigWidth/bigHeight >= (float)smallWidth/smallHeight){
				fromURI = bigPic + "?imageMogr2/thumbnail/x"+smallHeight+"/gravity/Center/crop/"+smallWidth+"x"+smallHeight;
			}else{
				fromURI = bigPic + "?imageMogr2/thumbnail/"+smallWidth+"x/gravity/Center/crop/"+smallWidth+"x"+smallHeight;
			}
//			System.out.println(fromURI);
			String signingStr = fromURI.replace("http://", "")+"|saveas/"+encodedEntryURI;
//			System.out.println(signingStr);
			try{
				String sign = mac.sign(signingStr.getBytes());
				String NewURL = "http://"+signingStr+"/sign/"+sign;
				
				NewURL = QiniuService.getTokenStr(space,bigPic,NewURL.replace(bigPic+"?", ""));				
//				System.out.println(NewURL);

				URL url = new URL(NewURL);
				URLConnection conn = url.openConnection();//获得UrlConnection 连接对象
				InputStream is = conn.getInputStream();//获得输入流
				smallPic = domain + smallPicName;
				json = "{\"url\":\""+smallPic+"\"}";
			}catch(Exception e){
				System.out.println("生成缩略图失败！");
			}						
		}		
		return json;
	}
	/*生成私有空间的访问链接*/
	public static String getTokenStr(String space,String url,String ext) {
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String domain = Global.getConfig("qiniu." + space + ".domain");

		Integer expired = Integer.parseInt(Global.getConfig("qiniu." + space + ".expired"));

		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		String filename = url.replace(domain, "");
		String downloadUrl = "";
		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
			now.add(Calendar.SECOND, expired);
			long expire = now.getTimeInMillis() / 1000;
			StringBuilder urlToSign = new StringBuilder();
//			urlToSign.append(domain).append(URLEscape.escape(filename));
			urlToSign.append(domain).append(URLEscape.escape(filename));

			urlToSign.append("?"+ext+"&e=").append(expire);

			String downToken = DigestAuth.sign(mac,urlToSign.toString().getBytes("utf-8"));
			downloadUrl = urlToSign.append("&token=").append(downToken).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadUrl;
	}
	
    /*获取公开空间的缩略图地址*/
	public static String zoomOut(String bigPic,Integer smallWidth,Integer smallHeight){
		//获取原图信息
		ImageInfo imageInfo = getImageInfo(bigPic);
		if(imageInfo != null){
			Integer bigWidth = imageInfo.getWidth();
			Integer bigHeight = imageInfo.getHeight();
			
			if((float)bigWidth/bigHeight >= (float)smallWidth/smallHeight){
				bigPic = bigPic + "?imageMogr2/thumbnail/x"+smallHeight+"/gravity/Center/crop/"+smallWidth+"x"+smallHeight;
			}else{
				bigPic = bigPic + "?imageMogr2/thumbnail/"+smallWidth+"x/gravity/Center/crop/"+smallWidth+"x"+smallHeight;
			}
			
		}	
	
		return bigPic;
	}
	
	/*获取公开空间的图片信息*/
	public static ImageInfo getImageInfo(String bigPic){
		ImageInfo imageInfo = null;
		try{		
			URL url = new URL(bigPic+"?imageInfo");
			URLConnection conn = url.openConnection();//获得UrlConnection 连接对象
			InputStream is = conn.getInputStream();//获得输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(is));//buffered表示缓冲类
			String str;
			String json = "";			
			
			while(null!=(str = br.readLine())){
				json += str;
			}			
			br.close();		
			ObjectMapper m = new ObjectMapper();
			imageInfo = m.readValue(json, ImageInfo.class);
		}catch(Exception e){
			imageInfo = null;
			System.out.println("获取图片信息失败！"+e.getMessage());
			System.out.println("获取图片信息失败！");
		}
		return imageInfo;
	}
	
	/*获取指定空间，指定图片的信息*/
	public static ImageInfo getImageInfo(String space,String bigPic){
		ImageInfo imageInfo = null;
		try{		
			bigPic = QiniuService.getTokenStr(space,bigPic,"imageInfo");
			URL url = new URL(bigPic);
			URLConnection conn = url.openConnection();//获得UrlConnection 连接对象
			InputStream is = conn.getInputStream();//获得输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(is));//buffered表示缓冲类
			String str;
			String json = "";			
			while(null!=(str = br.readLine())){
				json += str;
			}
			br.close();		
			ObjectMapper m = new ObjectMapper();
			imageInfo = m.readValue(json, ImageInfo.class);
		}catch(Exception e){
			System.out.println("获取图片信息失败！");
		}
		return imageInfo;
	}
	
    /** 
     * 上传文件 
     * @param key 上传到七牛时的文件名
     * @param file 本地文件的完整路径
     */
    public static void uploadFile(String key, String file){ 
		String ACCESS_KEY = Global.getConfig("qiniu.ACCESS_KEY");
		String SECRET_KEY = Global.getConfig("qiniu.SECRET_KEY");
		String bucket = "public";
		PutPolicy putPolicy = new PutPolicy(bucket);
        putPolicy.scope=bucket+":"+key;
		
		try {	        
			String  uptoken = putPolicy.token(new Mac(ACCESS_KEY, SECRET_KEY));
			IoApi.putFile(uptoken, key, file, new PutExtra()); 			
		} catch (Exception e) {
			e.printStackTrace();
		}		
        
    }
	
	public static void main(String[] args) {
//		String url = QiniuService.getHeadshotUrl("personal", "http://7xi7xd.com1.z0.glb.clouddn.com/xini03.jpg");
//		
//		System.out.println(url);
//		System.out.println(Space.PERSONAL.toString().toLowerCase());
		
//		System.out.println(QiniuService.getDownloadUrlStr("record","http://7xi7xg.com1.z0.glb.clouddn.com/9eed5cf903964a3b84169392234bd365.jpeg.jpeg"));
//		System.out.println(QiniuService.getDownloadUrl(Space.PRESCRIPTION,"http://7xi7xi.com1.z0.glb.clouddn.com/10000004143204081807EC1FB9-FF8D-46A2-A6FC-42C99D892721.jpg"));
//		System.out.println(QiniuService.getDownloadUrlStr(Space.PERSONAL,"http://7xi7xd.com1.z0.glb.clouddn.com/0b24y0zHhh2OR37ZQ0gFt6jv71p6pi.jpg"));
//		ImageInfo info = QiniuService.getImageInfo("personal","http://7xi7xd.com1.z0.glb.clouddn.com/0b24y0zHhh2OR37ZQ0gFt6jv71p6pi.jpg");

		System.out.println(QiniuService.zoomOut("record","http://7xi7xg.com1.z0.glb.clouddn.com/9eed5cf903964a3b84169392234bd365.jpeg.jpeg", 100, 100));
		
//		System.out.println(zoomOut("http://7xi7xa.com1.z0.glb.clouddn.com/toast02.jpg",100,100));
		
		System.out.println(getDownloadUrl(Space.PERSONAL, "http://7xi7xd.com1.z0.glb.clouddn.com/10000026e0e0c596-346f-46c7-b1a2-d1429466e7f04.jpeg"));
		
	}
	

}
