package com.itheima.activitys;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.utils.getPhoneNum;
import com.itheima.utils.phones;

public class phoneContacts extends Activity {
	private List<phones> list;
	private ListView lv;
	private LinearLayout ll_loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_contacts);
		lv = (ListView) findViewById(R.id.lv_phonenums);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		new Thread() {
			public void run() {
				ll_loading.setVisibility(View.VISIBLE);
				list = getPhoneNum.phoneContacts(phoneContacts.this);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv.setAdapter(new myBaseAdapter());
						ll_loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String phonenum = list.get(position).getPhone();
				Intent intent = new Intent(phoneContacts.this,
						phontprotect3.class);
				intent.putExtra("phonenum", phonenum);
				setResult(0, intent);
				finish();
			}
		});
	}

	private class myBaseAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(phoneContacts.this,
					R.layout.items_phonenum, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_phonename);
			tv_name.setText(list.get(position).getName());
			TextView tv_num = (TextView) view.findViewById(R.id.tv_phonenum);
			tv_num.setText(list.get(position).getPhone());
			return view;
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

	};
}
