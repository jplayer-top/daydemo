package com.itheima.loadBigParcture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView iv_parcture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv_parcture = (ImageView) findViewById(R.id.iv_parcture);
	}

	public void load(View v) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
				+ "/123.jpg", opts);
		opts.inSampleSize = getSize(opts);
		System.out.println(getSize(opts));
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(
				Environment.getExternalStorageDirectory() + "/123.jpg", opts);
		iv_parcture.setImageBitmap(bitmap);
	}

	public int getSize(Options opts) {
		int reswidth = opts.outWidth;
		int resHeight = opts.outHeight;
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		int getWidth = reswidth / width;
		int getHidth = resHeight / height;
		int size = getWidth < getHidth ? getWidth : getHidth;
		return size;
	}
}
