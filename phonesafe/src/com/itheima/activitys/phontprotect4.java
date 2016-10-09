package com.itheima.activitys;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.ui.onBackPress;

public class phontprotect4 extends basePhoneProtect {
	private CheckBox cb_select;
	private ImageView iv_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect4);
		cb_select = (CheckBox) findViewById(R.id.cb_select);
		iv_lock = (ImageView) findViewById(R.id.iv_lock);
		sp = getSharedPreferences("config", 0);
		cb_select.setChecked(sp.getBoolean("isChecked", true));
		if (sp.getBoolean("isChecked", true)) {
			iv_lock.setBackgroundResource(R.drawable.lock);
		} else {
			iv_lock.setBackgroundResource(R.drawable.unlock);
		}
		cb_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					Toast.makeText(phontprotect4.this, isChecked + "", 0)
							.show();
					iv_lock.setBackgroundResource(R.drawable.lock);
				} else {
					iv_lock.setBackgroundResource(R.drawable.unlock);

				}
				Editor editor = sp.edit();
				editor.putBoolean("isChecked", isChecked);
				editor.commit();
			}
		});
	}

	public void setting_to_03(View v) {
		openNewActivity(phontprotect3.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}

	public void setting_to_end(View v) {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("dialogSetting", false);
		editor.commit();
		openNewActivity(phontprotect.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		onBackPress.setOnbackpress(phontprotect4.this);

	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("dialogSetting", false);
		editor.commit();
		openNewActivity(phontprotect.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		openNewActivity(phontprotect3.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}
}
