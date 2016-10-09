package com.itheima.ui;

import com.example.phonesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class onOffImageButton extends ImageButton {

	public onOffImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public onOffImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public onOffImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private boolean flag = true;

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
		if (flag) {
			setBackgroundResource(R.drawable.on);

		} else {
			setBackgroundResource(R.drawable.off);
		}
	}

	public boolean changeFlag() {
		flag = !flag;
		setFlag(flag);
		return flag;
	}
}
