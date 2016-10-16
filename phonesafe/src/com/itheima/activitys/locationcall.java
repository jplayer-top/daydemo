package com.itheima.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.example.phonesafe.R;
import com.itheima.services.rockettosat;
import com.itheima.ui.onOffImageButton;
import com.itheima.utils.ServicesNum;
import com.itheima.utils.smsbackuptool;
import com.itheima.utils.smsbackuptool.callBackUp;

public class locationcall extends Activity {
	private RelativeLayout rl_locationcall;
	private RelativeLayout rl_sms_backup;
	private RelativeLayout rl_sms_resore;
	private onOffImageButton ib_rocket;
	private ProgressDialog pd;
	private RelativeLayout rl_app_lock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationcall);
		rl_app_lock = (RelativeLayout) findViewById(R.id.rl_app_lock);
		rl_app_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent applockIntent = new Intent(getApplicationContext(), applock.class);
				startActivity(applockIntent);
			}
		});
		ib_rocket = (onOffImageButton) findViewById(R.id.ib_rocket);
		ib_rocket.setFlag(ServicesNum.getServicesNum(this,
				"com.itheima.services.rockettosat"));
		ib_rocket.setOnClickListener(new rocketOnClickListener());
		rl_sms_resore = (RelativeLayout) findViewById(R.id.rl_sms_restor);
		rl_sms_resore.setOnClickListener(new restoreOnClickListener());
		rl_sms_backup = (RelativeLayout) findViewById(R.id.rl_sms_backup);
		rl_sms_backup.setOnClickListener(new backupOnCilckListener());
		rl_locationcall = (RelativeLayout) findViewById(R.id.rl_locationcall);
		rl_locationcall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(locationcall.this, wherecall.class);
				startActivity(intent);
			}
		});
	}

	private class rocketOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ib_rocket.changeFlag();
			Intent intent = new Intent(locationcall.this, rockettosat.class);
			boolean flag = ib_rocket.getFlag();
			if (flag) {
				startService(intent);
			} else {
				stopService(intent);
			}
		}

	}

	private class restoreOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(locationcall.this);
			pd.setTitle("Loadding");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.show();
			new Thread() {
				public void run() {
					smsbackuptool.setSmsInfo(locationcall.this,
							new callBackUp() {

								@Override
								public void setMaxProgress(int max) {
									// TODO Auto-generated method stub
									pd.setMax(max);
								}

								@Override
								public void setCurrentProgress(int progress) {
									// TODO Auto-generated method stub
									pd.setProgress(progress);
								}
							});
					pd.dismiss();
				};
			}.start();

		}

	}

	private class backupOnCilckListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(locationcall.this);
			pd.setTitle("Loadding");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.show();
			new Thread() {
				public void run() {
					smsbackuptool.getSmsInfos(locationcall.this,
							new callBackUp() {

								@Override
								public void setMaxProgress(int max) {
									// TODO Auto-generated method stub
									pd.setMax(max);
								}

								@Override
								public void setCurrentProgress(int progress) {
									// TODO Auto-generated method stub
									pd.setProgress(progress);
								}
							});
					pd.dismiss();
				};
			}.start();
		}

	}
}
