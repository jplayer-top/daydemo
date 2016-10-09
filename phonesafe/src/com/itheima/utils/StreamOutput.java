package com.itheima.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamOutput {
	public  static String getJsonString(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		try {
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			String result = new String(buffer);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
