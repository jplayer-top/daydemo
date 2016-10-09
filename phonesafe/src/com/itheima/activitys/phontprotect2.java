package com.itheima.activitys;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.ui.onBackPress;

public class phontprotect2 extends basePhoneProtect {
	private com.itheima.ui.lock_unlockImageView iv_phoneprotect02_tolock;
	private RelativeLayout rl_phoneprotect2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect2);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		iv_phoneprotect02_tolock = (com.itheima.ui.lock_unlockImageView) findViewById(R.id.iv_phoneprotect02_tolock);
		iv_phoneprotect02_tolock.setLockphone(sp.getBoolean("lockSIM", false));
		rl_phoneprotect2 = (RelativeLayout) findViewById(R.id.rl_phoneprotect2);
		rl_phoneprotect2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean lockSIM = iv_phoneprotect02_tolock.islock();
				Toast.makeText(phontprotect2.this, lockSIM + "", 0).show();
				Editor editor = sp.edit();
				editor.putBoolean("lockSIM", lockSIM);
				if (lockSIM) {
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String phoneSIM = tm.getSimSerialNumber();
					Toast.makeText(phontprotect2.this, phoneSIM, 0).show();
					editor.putString("phoneSIM", phoneSIM);
				} else {
					editor.putString("phoneSIM", "");
					Toast.makeText(phontprotect2.this, "SIMÎª¿Õ", 0).show();

				}
				editor.commit();
			}
		});
	}

	public void setting_to_01(View v) {
		openNewActivity(phontprotect1.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}

	public void setting_to_03(View v) {
		openNewActivity(phontprotect3.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		onBackPress.setOnbackpress(phontprotect2.this);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		openNewActivity(phontprotect3.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		openNewActivity(phontprotect1.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}
}
