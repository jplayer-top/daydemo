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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);
		iv_home_heima = (ImageView) findViewById(R.id.iv_home_heima);
		iv_logo_Animator();
		gv_home_items = (GridView) findViewById(R.id.gv_home_items);
		marray = getResources().getStringArray(R.array.toolsArray);
		mimage = new int[] { R.drawable.sjfd, R.drawable.rjgj, R.drawable.lltj,
				R.drawable.xtjs, R.drawable.srlj, R.drawable.jcgl,
				R.drawable.sjsd, R.drawable.cygj };
		mtooldescop = getResources().getStringArray(R.array.toolsDescip);
		gv_home_items.setAdapter(new myBaseAdapter());
		gv_home_items.setOnItemClickListener(new myOnItemClickListener());
	}

	private class myOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.i(TAG, position + "");
			switch (position) {
			case 0:
				sp = getSharedPreferences("config", MODE_PRIVATE);
				boolean dialogSetting = sp.getBoolean("dialogSetting", true);
				if (dialogSetting)
					setPassword(view);
				else
					enterPassword(view);
				break;
			case 1:
				Intent appButler = new Intent(homeActivity.this,
						appbutler.class);
				startActivity(appButler);
				break;
			case 4:
				Intent intent = new Intent();
				intent.setClass(homeActivity.this, blackPhone.class);
				startActivity(intent);
				break;
			case 7:
				Intent locationintent = new Intent();
				locationintent.setClass(homeActivity.this, locationcall.class);
				startActivity(locationintent);
				break;
			}
		}

	}

	private void enterPassword(View view) {
		// TODO Auto-generated method stub
		view = View.inflate(homeActivity.this, R.layout.enterpassword, null);
		AlertDialog.Builder builder = new Builder(homeActivity.this);
		builder.setView(view);
		Button ok = (Button) view.findViewById(R.id.bt_ok);
		final EditText et_input = (EditText) view
				.findViewById(R.id.et_password_input);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Button cancle = (Button) view.findViewById(R.id.bt_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builder.show();
	}

	private void setPassword(View view) {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				String input = et_input.getText().toString().trim();
				String enter = et_enter.getText().toString().trim();
				Log.i(TAG, input + ":::" + enter);

				MessageDigest md;
				try {
					md = MessageDigest.getInstance("md5");
					byte[] mdinput = md.digest(input.getBytes());
					StringBuilder sb = new StringBuilder();
					for (byte byt : mdinput) {
						int num = byt & 0Xff;
						String numString = Integer.toHexString(num);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Button cancle = (Button) view.findViewById(R.id.bt_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builder.show();
	}

	public void setting(View v) {
		Intent intent = new Intent(homeActivity.this, settingActivity.class);
		startActivity(intent);
	}

	public class myBaseAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			return marray.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private void iv_logo_Animator() {
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_heima, "rotationY",
				new float[] { 0, 360 });
		oa.setDuration(3000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.RESTART);
		oa.start();
	}
}
