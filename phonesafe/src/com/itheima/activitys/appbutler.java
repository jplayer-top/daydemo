package com.itheima.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.utils.appinfos;

public class appbutler extends Activity {
	private TextView tv_ram;
	private TextView tv_rom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appbutler);
		tv_ram = (TextView) findViewById(R.id.tv_ram);
		tv_rom = (TextView) findViewById(R.id.tv_rom);
		tv_ram.setText(appinfos.getRam(appbutler.this));
		tv_rom.setText(appinfos.getRom(appbutler.this));
		appinfos.getAppInfos(this);
	}
}
