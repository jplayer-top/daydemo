package com.itheima.activitys;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;

public class homeActivity extends Activity {
	public static final String TAG = "homeActivity";
	private ImageView iv_home_heima;
	private GridView gv_home_items;
	private String[] marray;
	private int[] mimage;
	private String[] mtooldescop;
	private AlertDialog dialog;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		iv_home_heima = (ImageView) findViewById(R.id.iv_home_heima);
		iv_logo_Animator();
		gv_home_items = (GridView) findViewById(R.id.gv_home_items);
		marray = getResources().getStringArray(R.array.toolsArray);
		// 定义图片的 int 数组
		mimage = new int[] { R.drawable.sjfd, R.drawable.rjgj, R.drawable.lltj,
				R.drawable.xtjs, R.drawable.srlj, R.drawable.jcgl,
				R.drawable.sjsd, R.drawable.cygj };
		// 国际化StringArray
		mtooldescop = getResources().getStringArray(R.array.toolsDescip);
		gv_home_items.setAdapter(new myBaseAdapter());
		gv_home_items.setOnItemClickListener(new myOnItemClickListener());
	}

	/**
	 * 条目点击事件，GridView ,进入功能界面
	 * 
	 * @author oblivion
	 * 
	 */
	private class myOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i(TAG, position + "");
			switch (position) {
			// 手机防盗
			case 0:
				sp = getSharedPreferences("config", MODE_PRIVATE);
				boolean dialogSetting = sp.getBoolean("dialogSetting", true);
				if (dialogSetting)
					// 未设置密码，需要设定，传入一个View 目的为了设定对话框
					setPassword(view);
				else
					enterPassword(view);
				break;
			// 程序管家
			case 1:
				Intent appButler = new Intent(homeActivity.this,
						appbutler.class);
				startActivity(appButler);
				break;
			// 流量统计
			case 2:
				Intent tramIntent = new Intent(homeActivity.this,
						myTrafficStats.class);
				startActivity(tramIntent);
				break;
			// 缓存清理
			case 3:
				Intent cacheIntent = new Intent(homeActivity.this,
						myCacheClear.class);
				startActivity(cacheIntent);
				break;
			// 骚扰拦截
			case 4:
				Intent intent = new Intent();
				intent.setClass(homeActivity.this, blackPhone.class);
				startActivity(intent);
				break;
			// 进程管理
			case 5:
				Intent progressIntent = new Intent(homeActivity.this,
						myRuningProgress.class);
				startActivity(progressIntent);
				break;
			// 病毒防护
			case 6:
				Intent antivirusIntent = new Intent();
				antivirusIntent.setClass(homeActivity.this, antivirus.class);
				startActivity(antivirusIntent);
				break;
			// 常用工具
			case 7:
				Intent locationintent = new Intent();
				locationintent.setClass(homeActivity.this, locationcall.class);
				startActivity(locationintent);
				break;
			}
		}

	}

	/**
	 * 确认密码
	 * 
	 * @param view
	 */
	private void enterPassword(View view) {
		view = View.inflate(homeActivity.this, R.layout.enterpassword, null);
		AlertDialog.Builder builder = new Builder(homeActivity.this);
		builder.setView(view);
		Button ok = (Button) view.findViewById(R.id.bt_ok);
		final EditText et_input = (EditText) view
				.findViewById(R.id.et_password_input);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = et_input.getText().toString().trim();
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("md5");
					byte[] bty = md.digest(input.getBytes());
					StringBuilder sb = new StringBuilder();
					for (byte b : bty) {
						int num = b & 0xff;
						String numString = Integer.toHexString(num);
						if (numString.length() == 1) {
							sb.append("0" + numString);
						} else {
							sb.append(numString);
						}
					}
					String md5Number = sb.toString().trim();
					String enter = sp.getString("password", "");
					Log.i(TAG, md5Number + "---" + enter);
					if (md5Number.equals("")) {
						Toast.makeText(homeActivity.this, "密码不能空", 0).show();
					} else if (md5Number.equals(enter)) {
						Intent intent = new Intent(homeActivity.this,
								phontprotect.class);
						startActivity(intent);
						dialog.dismiss();
					} else {
						Toast.makeText(homeActivity.this, "与设定密码不一致", 0).show();
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		});
		Button cancle = (Button) view.findViewById(R.id.bt_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = builder.show();
	}

	/**
	 * 设置密码
	 * 
	 * @param view
	 */
	private void setPassword(View view) {
		view = View.inflate(homeActivity.this, R.layout.passworddialog, null);
		AlertDialog.Builder builder = new Builder(homeActivity.this);
		builder.setView(view);
		Button ok = (Button) view.findViewById(R.id.bt_ok);
		final EditText et_input = (EditText) view
				.findViewById(R.id.et_password_input);
		final EditText et_enter = (EditText) view
				.findViewById(R.id.et_password_enter);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = et_input.getText().toString().trim();
				String enter = et_enter.getText().toString().trim();
				Log.i(TAG, input + ":::" + enter);
				// 重点-----将明文密码转化为MD5 格式文本进行存取----------
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("md5");
					byte[] mdinput = md.digest(input.getBytes());
					StringBuilder sb = new StringBuilder();
					for (byte byt : mdinput) {
						// 将byte转化为int，添加位数
						int num = byt & 0Xff;
						// 将int 数转化为16进制的String 文本
						String numString = Integer.toHexString(num);
						// 遍历，发现长度小于1的都添加一个0
						if (numString.length() == 1) {
							sb.append("0" + numString);
						} else {
							sb.append(numString);
						}
					}
					String md5Number = sb.toString();
					Log.i("md5Number", md5Number);
					if (input.equals("") || enter.equals("")) {
						Toast.makeText(homeActivity.this, "密码不能空", 0).show();
					} else if (input.equals(enter)) {
						Editor editor = sp.edit();
						editor.putString("password", md5Number);
						editor.commit();
						Intent intent = new Intent(homeActivity.this,
								phontprotect1.class);
						startActivity(intent);
						dialog.dismiss();
					} else {
						Toast.makeText(homeActivity.this, "两次密码不一致", 0).show();
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		});
		Button cancle = (Button) view.findViewById(R.id.bt_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 需要将对话框关闭，但是只能通过dialog 去关闭，所以在这里得到一个dialog的对象
		dialog = builder.show();
	}

	/**
	 * 设置中心的点击事件
	 */
	public void setting(View v) {
		Intent intent = new Intent(homeActivity.this, settingActivity.class);
		startActivity(intent);
	}

	/**
	 * GirdView 适配器
	 * 
	 * @author oblivion
	 * 
	 */
	public class myBaseAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(homeActivity.this, R.layout.home_gv_item,
					null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			iv_item.setImageResource(mimage[position]);
			TextView tv_item_main = (TextView) view
					.findViewById(R.id.tv_item_main);
			tv_item_main.setText(marray[position]);
			TextView tv_item_desc = (TextView) view
					.findViewById(R.id.tv_item_desc);
			tv_item_desc.setText(mtooldescop[position]);
			return view;
		}

		@Override
		public int getCount() {
			return marray.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	/**
	 * 主界面旋转的logo动画加载
	 */
	private void iv_logo_Animator() {
		// 属性动画
		// 能获取getset 方法，所以属性就设定这个
		// iv_home_heima.setRotation(rotation);
		// iv_home_heima.getRotationY();
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_heima, "rotationY",
				new float[] { 0, 360 });
		oa.setDuration(3000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.RESTART);
		oa.start();
	}
}
