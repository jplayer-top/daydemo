package com.itheima.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.db.dao.addressDao;

public class wherecall extends Activity {
	private EditText et_number;
	private TextView tv_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wherecall);
		et_number = (EditText) findViewById(R.id.et_phonewhere);
		TextWatcher watcher = new myTextWatcher();
		et_number.addTextChangedListener(watcher);
		tv_location = (TextView) findViewById(R.id.tv_location);
	}

	public void findLocation(View v) {
		String insertNumber = et_number.getText().toString().trim();
		if (TextUtils.isEmpty(insertNumber)) {
			//Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			//et_number.startAnimation(shake);
			TranslateAnimation tf = new TranslateAnimation(0.0f, 10.0f, 0, 0);
			Interpolator inter = new CycleInterpolator(10);
			tf.setInterpolator(inter);
			tf.setDuration(300);
			et_number.startAnimation(tf);
			return;
		}
		String location = addressDao.whereAddress(wherecall.this, insertNumber);
		tv_location.setText(location);
	}

	private class myTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() >= 7) {
				String location = addressDao.whereAddress(wherecall.this,
						s.toString());
				tv_location.setText(location);

			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

	}
}
