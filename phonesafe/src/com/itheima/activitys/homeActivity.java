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
		// ����ͼƬ�� int ����
		mimage = new int[] { R.drawable.sjfd, R.drawable.rjgj, R.drawable.lltj,
				R.drawable.xtjs, R.drawable.srlj, R.drawable.jcgl,
				R.drawable.sjsd, R.drawable.cygj };
		// ���ʻ�StringArray
		mtooldescop = getResources().getStringArray(R.array.toolsDescip);
		gv_home_items.setAdapter(new myBaseAdapter());
		gv_home_items.setOnItemClickListener(new myOnItemClickListener());
	}

	/**
	 * ��Ŀ����¼���GridView ,���빦�ܽ���
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
			// �ֻ�����
			case 0:
				sp = getSharedPreferences("config", MODE_PRIVATE);
				boolean dialogSetting = sp.getBoolean("dialogSetting", true);
				if (dialogSetting)
					// δ�������룬��Ҫ�趨������һ��View Ŀ��Ϊ���趨�Ի���
					setPassword(view);
				else
					enterPassword(view);
				break;
			// ����ܼ�
			case 1:
				Intent appButler = new Intent(homeActivity.this,
						appbutler.class);
				startActivity(appButler);
				break;
			// ����ͳ��
			case 2:
				Intent tramIntent = new Intent(homeActivity.this,
						myTrafficStats.class);
				startActivity(tramIntent);
				break;
			// ��������
			case 3:
				Intent cacheIntent = new Intent(homeActivity.this,
						myCacheClear.class);
				startActivity(cacheIntent);
				break;
			// ɧ������
			case 4:
				Intent intent = new Intent();
				intent.setClass(homeActivity.this, blackPhone.class);
				startActivity(intent);
				break;
			// ���̹���
			case 5:
				Intent progressIntent = new Intent(homeActivity.this,
						myRuningProgress.class);
				startActivity(progressIntent);
				break;
			// ��������
			case 6:
				Intent antivirusIntent = new Intent();
				antivirusIntent.setClass(homeActivity.this, antivirus.class);
				startActivity(antivirusIntent);
				break;
			// ���ù���
			case 7:
				Intent locationintent = new Intent();
				locationintent.setClass(homeActivity.this, locationcall.class);
				startActivity(locationintent);
				break;
			}
		}

	}

	/**
	 * ȷ������
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
						Toast.makeText(homeActivity.this, "���벻�ܿ�", 0).show();
					} else if (md5Number.equals(enter)) {
						Intent intent = new Intent(homeActivity.this,
								phontprotect.class);
						startActivity(intent);
						dialog.dismiss();
					} else {
						Toast.makeText(homeActivity.this, "���趨���벻һ��", 0).show();
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
	 * ��������
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
				// �ص�-----����������ת��ΪMD5 ��ʽ�ı����д�ȡ----------
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("md5");
					byte[] mdinput = md.digest(input.getBytes());
					StringBuilder sb = new StringBuilder();
					for (byte byt : mdinput) {
						// ��byteת��Ϊint�����λ��
						int num = byt & 0Xff;
						// ��int ��ת��Ϊ16���Ƶ�String �ı�
						String numString = Integer.toHexString(num);
						// ���������ֳ���С��1�Ķ����һ��0
						if (numString.length() == 1) {
							sb.append("0" + numString);
						} else {
							sb.append(numString);
						}
					}
					String md5Number = sb.toString();
					Log.i("md5Number", md5Number);
					if (input.equals("") || enter.equals("")) {
						Toast.makeText(homeActivity.this, "���벻�ܿ�", 0).show();
					} else if (input.equals(enter)) {
						Editor editor = sp.edit();
						editor.putString("password", md5Number);
						editor.commit();
						Intent intent = new Intent(homeActivity.this,
								phontprotect1.class);
						startActivity(intent);
						dialog.dismiss();
					} else {
						Toast.makeText(homeActivity.this, "�������벻һ��", 0).show();
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
		// ��Ҫ���Ի���رգ�����ֻ��ͨ��dialog ȥ�رգ�����������õ�һ��dialog�Ķ���
		dialog = builder.show();
	}

	/**
	 * �������ĵĵ���¼�
	 */
	public void setting(View v) {
		Intent intent = new Intent(homeActivity.this, settingActivity.class);
		startActivity(intent);
	}

	/**
	 * GirdView ������
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
	 * ��������ת��logo��������
	 */
	private void iv_logo_Animator() {
		// ���Զ���
		// �ܻ�ȡgetset �������������Ծ��趨���
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
