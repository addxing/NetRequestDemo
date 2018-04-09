package com.example.baselibrary.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：文件管理相关方法
 */
public class FileUtils {


	/**
	 * Java文件操作 获取文件扩展名
	 *
	 * @param filename 文件名称
	 * @return 文件的拓展名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}

		return filename;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名（去除拓展名）
	 * @param filename 文件名称
	 * @return 返回不带文件拓展名的文件名称
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}


	/**
	 * 获取到路径的文件名称
	 *
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		String filename = "";
		boolean isok = false;
		// 从UrlConnection中获取文件名称
		try {
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			if (conn == null) {
				return null;
			}
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return null;
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return null;
			}
			// Log.i("test", "getContentType:" + conn.getContentType() + ",Url:"
			// + conn.getURL().toString());
			for (String skey : key) {
				List<String> values = hf.get(skey);
				for (String value : values) {
					String result;
					try {
						result = new String(value.getBytes("ISO-8859-1"), "GBK");
						int location = result.indexOf("filename");
						if (location >= 0) {
							result = result.substring(location
									+ "filename".length());
							filename = result
									.substring(result.indexOf("=") + 1);
							isok = true;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}// ISO-8859-1 UTF-8 gb2312
				}
				if (isok) {
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 从路径中获取
		if (filename == null || "".equals(filename)) {
			filename = url.substring(url.lastIndexOf("/") + 1);
		}
		return filename;
	}
	
	/**
	 * 截取文件的后缀，获取到MIME的类型
	 * 
	 * @param file 需要获取后缀的路径
	 * 
	 * */
	public static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	
	/**
	 * 
	 * MIME的表
	 * */
	private static final String[][] MIME_MapTable={
		    //{后缀名，    MIME类型}
		    {".3gp",    "video/3gpp"},
		    {".apk",    "application/vnd.android.package-archive"},
		    {".asf",    "video/x-ms-asf"},
		    {".avi",    "video/x-msvideo"},
		    {".bin",    "application/octet-stream"},
		    {".bmp",      "image/bmp"},
		    {".c",        "text/plain"},
		    {".class",    "application/octet-stream"},
		    {".conf",    "text/plain"},
		    {".cpp",    "text/plain"},
		    {".doc",    "application/msword"},
			{".docx",    "application/msword"},
		    {".exe",    "application/octet-stream"},
		    {".gif",    "image/gif"},
		    {".gtar",    "application/x-gtar"},
		    {".gz",        "application/x-gzip"},
		    {".h",        "text/plain"},
		    {".htm",    "text/html"},
		    {".html",    "text/html"},
		    {".jar",    "application/java-archive"},
		    {".java",    "text/plain"},
		    {".jpeg",    "image/jpeg"},
		    {".jpg",    "image/jpeg"},
		    {".js",        "application/x-javascript"},
		    {".log",    "text/plain"},
		    {".m3u",    "audio/x-mpegurl"},
		    {".m4a",    "audio/mp4a-latm"},
		    {".m4b",    "audio/mp4a-latm"},
		    {".m4p",    "audio/mp4a-latm"},
		    {".m4u",    "video/vnd.mpegurl"},
		    {".m4v",    "video/x-m4v"},    
		    {".mov",    "video/quicktime"},
		    {".mp2",    "audio/x-mpeg"},
		    {".mp3",    "audio/x-mpeg"},
		    {".mp4",    "video/mp4"},
		    {".mpc",    "application/vnd.mpohun.certificate"},        
		    {".mpe",    "video/mpeg"},    
		    {".mpeg",    "video/mpeg"},    
		    {".mpg",    "video/mpeg"},    
		    {".mpg4",    "video/mp4"},    
		    {".mpga",    "audio/mpeg"},
		    {".msg",    "application/vnd.ms-outlook"},
		    {".ogg",    "audio/ogg"},
		    {".pdf",    "application/pdf"},
		    {".png",    "image/png"},
		    {".pps",    "application/vnd.ms-powerpoint"},
		    {".ppt",    "application/vnd.ms-powerpoint"},
		    {".pptx",    "application/vnd.ms-powerpoint"},
		    {".prop",    "text/plain"},
		    {".rar",    "application/x-rar-compressed"},
		    {".rc",        "text/plain"},
		    {".rmvb",    "audio/x-pn-realaudio"},
		    {".rtf",    "application/rtf"},
		    {".sh",        "text/plain"},
		    {".tar",    "application/x-tar"},    
		    {".tgz",    "application/x-compressed"}, 
		    {".txt",    "text/plain"},
		    {".wav",    "audio/x-wav"},
		    {".wma",    "audio/x-ms-wma"},
		    {".wmv",    "video/x-ms-wmv"},
		    {".wps",    "application/vnd.ms-works"},
		    {".xml",    "text/xml"},
		    {".xml",    "text/plain"},
		    {".z",        "application/x-compress"},
		    {".zip",    "application/zip"},
		    {"",        "*/*"},
			{".xls",    "application/vnd.ms-excel"}
		};

}
