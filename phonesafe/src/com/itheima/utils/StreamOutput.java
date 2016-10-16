package com.itheima.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ��ȡԴ�벢װ�����ַ���
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
			 * ������޸ı����ʽ
			 * String temp = baos.toString();
			 * if(temp.contains("charset=utf-8")){ return temp; } else
			 * if(temp.contains("gb2312")){ return baos.toString("gb2312");
			 * //new String(buffer, "gb2312"); }return null;
			 */
			String result = new String(buffer);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			// �����ȡ���󡣡�һ��Ҫ����һ���մ�
			return "";
		}
	}
}
