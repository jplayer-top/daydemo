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
			System.out.println("�����쳣");
			//��ȡ�쳣����ʱ��
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
						//�����������쳣catch ��Ϊֻ�������Ż���˵�����static string ���ֶΣ�
						//Ҳ����ǿת(String)����
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
				System.out.println("�ļ��Ҳ���");
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
