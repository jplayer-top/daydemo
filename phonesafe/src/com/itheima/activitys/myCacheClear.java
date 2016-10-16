package com.itheima.activitys;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.fragment.cachefragment;
import com.itheima.fragment.sdfragment;

public class myCacheClear extends Activity implements OnClickListener {
	private LinearLayout ll_clean_cache;
	private ImageView iv_clean_cache;
	private TextView tv_clean_cache;
	private LinearLayout ll_clean_sd;
	private ImageView iv_clean_sd;
	private TextView tv_clean_sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycacheclear);
		ll_clean_cache = (LinearLayout) findViewById(R.id.ll_clean_cache);
		iv_clean_cache = (ImageView) findViewById(R.id.iv_clean_cache);
		tv_clean_cache = (TextView) findViewById(R.id.tv_clean_cache);
		ll_clean_sd = (LinearLayout) findViewById(R.id.ll_clean_sd);
		iv_clean_sd = (ImageView) findViewById(R.id.iv_clean_sd);
		tv_clean_sd = (TextView) findViewById(R.id.tv_clean_sd);
		cachefragment cf = new cachefragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.ll_fragment, cf);
		ft.commit();
		ll_clean_cache.setOnClickListener(this);
		ll_clean_sd.setOnClickListener(this);
	}

	// 14ff00
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_clean_sd:
			Log.i("ddd", "ddfads");
			iv_clean_cache.setImageResource(R.drawable.clean_cache_icon);
			tv_clean_cache.setTextColor(0X99000000);
			ll_clean_cache.setBackgroundResource(R.drawable.gv_home_press);
			ll_clean_sd.setBackgroundDrawable(null);
			iv_clean_sd.setImageResource(R.drawable.clean_sdcard_icon_pressed);
			tv_clean_sd.setTextColor(0Xff14ff00);

			sdfragment sf = new sdfragment();
			FragmentTransaction sdft = getFragmentManager().beginTransaction();
			sdft.replace(R.id.ll_fragment, sf);
			sdft.commit();
			break;
		case R.id.ll_clean_cache:
			iv_clean_sd.setImageResource(R.drawable.clean_sdcard_icon);
			tv_clean_sd.setTextColor(0X99000000);
			ll_clean_sd.setBackgroundResource(R.drawable.gv_home_press);
			ll_clean_cache.setBackgroundDrawable(null);
			iv_clean_cache
					.setImageResource(R.drawable.clean_cache_icon_pressed);
			tv_clean_cache.setTextColor(0Xff14ff00);
			cachefragment cf = new cachefragment();
			FragmentTransaction cacheft = getFragmentManager()
					.beginTransaction();
			cacheft.replace(R.id.ll_fragment, cf);
			cacheft.commit();
			break;
		}
	}
}
