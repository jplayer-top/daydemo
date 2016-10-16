package com.itheima.activitys;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phonesafe.R;

public class myTrafficStats extends Activity {
	private TextView tv_alltraffic;
	private TextView tv_gtraffic;
	private ListView lv_singletraffic;
	private List<ApplicationInfo> appInfos;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytraffic);
		tv_alltraffic = (TextView) findViewById(R.id.tv_alltraffic);
		tv_gtraffic = (TextView) findViewById(R.id.tv_gtraffic);
		lv_singletraffic = (ListView) findViewById(R.id.lv_singletraffic);
		// TrafficStats traffic = new TrafficStats();
		long totalTx = TrafficStats.getTotalTxBytes();
		long totalRx = TrafficStats.getTotalTxBytes();
		long gTx = TrafficStats.getMobileTxBytes();
		long gRx = TrafficStats.getMobileRxBytes();
		tv_alltraffic.setText(Formatter.formatFileSize(getApplicationContext(),
				totalRx + totalTx));
		tv_gtraffic.setText(Formatter.formatFileSize(getApplicationContext(),
				gRx + gTx));
		pm = getPackageManager();
		appInfos = pm.getInstalledApplications(0);
		lv_singletraffic.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView tv = new TextView(getApplicationContext());
				long uidRx = TrafficStats.getUidRxBytes(appInfos.get(position).uid);
				long uidTx = TrafficStats.getUidTxBytes(appInfos.get(position).uid);
				String uidAll = Formatter.formatFileSize(
						getApplicationContext(), uidTx + uidRx);
				tv.setText(appInfos.get(position).loadLabel(pm).toString()
						+ uidAll);
				return tv;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return appInfos.size();
			}
		});
	}
}
