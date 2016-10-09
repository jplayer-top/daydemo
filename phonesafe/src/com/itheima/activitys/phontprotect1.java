package com.itheima.activitys;

import android.os.Bundle;
import android.view.View;

import com.example.phonesafe.R;
import com.itheima.ui.onBackPress;

public class phontprotect1 extends basePhoneProtect {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect1);

	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
	}

	public void setting_to_02(View v) {
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		onBackPress.setOnbackpress(phontprotect1.this);
	}

}
