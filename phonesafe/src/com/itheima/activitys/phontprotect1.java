package com.itheima.activitys;

import android.os.Bundle;
import android.view.View;

import com.example.phonesafe.R;
import com.itheima.ui.onBackPress;

public class phontprotect1 extends basePhoneProtect {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect1);

	}

	/**
	 * ���󷽷���д �����һ��ʵ����ת
	 */
	@Override
	public void next() {

		openNewActivity(phontprotect2.class);
		// Activity����ת����,������finish()֮���������startActivity(Intent)֮��
		// Call immediately after one of the flavors of startActivity(Intent) or
		// finish to specify an explicit transition animation to perform next.
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	/**
	 * ���󷽷���д
	 */
	@Override
	public void pre() {
	}

	public void setting_to_02(View v) {
		// ��д�̳и���ķ���
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void onBackPressed() {
		// ��д���ؼ��������Ի���ȷ���Ƿ��˳������򵼽���
		onBackPress.setOnbackpress(phontprotect1.this);
	}

}
