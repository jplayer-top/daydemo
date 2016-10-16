package com.itheima.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取源码并装换成字符串
 * 
 * @author oblivion
 * 
 */
public class StreamOutput {
	public static String getJsonString(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		try {
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			/**
			 * 这个是修改编码格式
			 * String temp = baos.toString();
			 * if(temp.contains("charset=utf-8")){ return temp; } else
			 * if(temp.contains("gb2312")){ return baos.toString("gb2312");
			 * //new String(buffer, "gb2312"); }return null;
			 */
			String result = new String(buffer);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			// 如果读取错误。。一定要返回一个空串
			return "";
		}
	}
}
