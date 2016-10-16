package com.itheima.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.db.dao.mySQL;

public class addphoneNumber extends Activity {
	private EditText et_insertnumber;
	private RadioGroup radiogroup;
	private mySQL ms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addphonenumber);
		et_insertnumber = (EditText) findViewById(R.id.et_insertnumber);
		radiogroup = (RadioGroup) findViewById(R.id.radioGroup);
	}

	public void bt_insert(View v) {
		String phone = et_insertnumber.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			return;
		}
		int id = radiogroup.getCheckedRadioButtonId();
		String mode = "0";
		switch (id) {
		case R.id.rb_all:
			mode = "0";
			break;
		case R.id.rb_phone:
			mode = "1";
			break;
		case R.id.rb_message:
			mode = "2";
			break;
		}
		ms = new mySQL(addphoneNumber.this);
		if (mode.equals("0")) {
			mode = "全选";
		} else if (mode.equals("1")) {

			mode = "电话";
		} else if (mode.equals("2")) {

			mode = "短信";
		}
		boolean flag = ms.insert(phone, mode);
		Intent intent = new Intent(addphoneNumber.this, blackPhone.class);
		intent.putExtra("flag", flag);
		intent.putExtra("phone", phone);
		intent.putExtra("mode", mode);
		Log.i("intent", phone + "----" + mode);
		setResult(0, intent);
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		String phone = et_insertnumber.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(addphoneNumber.this, "不能为空啊，大哥",0).show();
			return;
		}
	}
}
