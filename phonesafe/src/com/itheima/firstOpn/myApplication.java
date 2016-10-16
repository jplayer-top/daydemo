package com.itheima.firstOpn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

public class myApplication extends Application {
	private myUncaughtExceptionHandler handler;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new myUncaughtExceptionHandler();
		Thread.currentThread().setUncaughtExceptionHandler(handler);
	}

	private class myUncaughtExceptionHandler implements
			UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			System.out.println("发现异常");
			//获取异常发生时间
			long cuttentTime = System.currentTimeMillis();
			StringBuffer sb = new StringBuffer();
			sb.append("timer = " + cuttentTime + "\n");
			Field[] fields = Build.class.getDeclaredFields();
			try {
				for (Field field : fields) {
					try {
						String value = field.get(null).toString();
						String name = field.getName();
						sb.append(name + "=" +value + "\n");
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (Exception e){
						//必须添加这个异常catch 因为只有这样才会过滤掉不是static string 的字段；
						//也就是强转(String)错误
						System.out.println(e.toString());
					}
				}
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				sb.append(sw.toString());
				File file = new File(Environment.getExternalStorageDirectory(),
						"error.txt");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
				System.out.println(sb.toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("arg");
			} catch (FileNotFoundException e) {
				System.out.println("文件找不到");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(".....");
			} finally {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}

	}
}
