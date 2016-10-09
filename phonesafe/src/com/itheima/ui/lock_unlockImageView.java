package com.itheima.ui;

import com.example.phonesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class lock_unlockImageView extends ImageView {

	public lock_unlockImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public lock_unlockImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public lock_unlockImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private boolean lockSIM = true;

	public boolean getLockphone() {
		return lockSIM;
	}

	public void setLockphone(boolean lockSIM) {
		this.lockSIM = lockSIM;
		if (lockSIM) {
			setBackgroundResource(R.drawable.lock);
		} else {
			setBackgroundResource(R.drawable.unlock);

		}
	}

	public boolean islock() {
		lockSIM = !lockSIM;
		setLockphone(lockSIM);
		return lockSIM;
	}
}
