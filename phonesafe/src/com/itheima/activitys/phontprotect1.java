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
	 * 抽象方法重写 点击下一步实现跳转
	 */
	@Override
	public void next() {

		openNewActivity(phontprotect2.class);
		// Activity的跳转动画,必须在finish()之后或者是在startActivity(Intent)之后
		// Call immediately after one of the flavors of startActivity(Intent) or
		// finish to specify an explicit transition animation to perform next.
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	/**
	 * 抽象方法重写
	 */
	@Override
	public void pre() {
	}

	public void setting_to_02(View v) {
		// 重写继承父类的方法
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void onBackPressed() {
		// 重写返回键，弹出对话框，确认是否退出设置向导界面
		onBackPress.setOnbackpress(phontprotect1.this);
	}

}
