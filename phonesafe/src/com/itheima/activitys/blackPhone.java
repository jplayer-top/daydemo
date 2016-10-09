package com.itheima.activitys;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.db.dao.mySQL;
import com.itheima.utils.phonemode;

public class blackPhone extends Activity {
	private ImageView iv_addphone;
	private ListView lv_phone_black;
	private myBaseAdapter adapter;
	private mySQL ms;
	private List<phonemode> list;
	private RelativeLayout rl_visible;
	private ImageView iv_empty;
	private String [] items;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blackphoneactivity);
		iv_empty = (ImageView) findViewById(R.id.iv_empty);
		iv_addphone = (ImageView) findViewById(R.id.iv_addphone);
		iv_addphone.setOnClickListener(new myOnClickListener());
		rl_visible = (RelativeLayout) findViewById(R.id.rl_visible);
		lv_phone_black = (ListView) findViewById(R.id.lv_phone_black);
		items  = new String []{"全部","电话","短信"};
		lv_phone_black.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				Log.i("blackPhone", "长按点击");
				AlertDialog.Builder builder = new Builder(blackPhone.this);
				builder.setTitle("选择要更换的类型");
				builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ms.update(list.get(position).getPhone(), items[which]);
						list.get(position).setMode( items[which]);
						list.set(position,list.get(position) );
						adapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("cancel", null);
				builder.show();
				return true;
			}
		});
		new Thread() {
			public void run() {
				ms = new mySQL(blackPhone.this);
				list = ms.find();
				adapter = new myBaseAdapter();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						rl_visible.setVisibility(View.INVISIBLE);
						getChange();
						lv_phone_black.setAdapter(adapter);
					}
				});
			};
		}.start();
	}

	private void getChange() {
		if (list.size() <= 0) {
			iv_empty.setVisibility(View.VISIBLE);
		} else {
			// TODO Auto-generated method stub
			iv_empty.setVisibility(View.INVISIBLE);
		}
	}

	class ViewHolder {
		private TextView tv_phone;
		private TextView tv_mode;
		private ImageView iv_delete;
	}

	private class myBaseAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			final ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(blackPhone.this,
						R.layout.items_findblacknummber, null);
				holder = new ViewHolder();
				holder.tv_phone = (TextView) view
						.findViewById(R.id.tv_phonenumber);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_phonemode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_phone.setText(list.get(position).getPhone());
			holder.tv_mode.setText(list.get(position).getMode());
			holder.iv_delete.setBackgroundResource(R.drawable.ic_delete_btn);
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.iv_delete
							.setBackgroundResource(R.drawable.ic_delete);
					AlertDialog.Builder builder = new Builder(blackPhone.this);
					builder.setTitle("温馨提示");
					builder.setMessage("是否移除此项黑名单");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									ms.delete(list.get(position).getPhone());
									list.remove(position);
									getChange();
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(blackPhone.this, addphoneNumber.class);
			startActivityForResult(intent, 0);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String insertphone = data.getStringExtra("phone");
		String insertmode = data.getStringExtra("mode");
		boolean flag = data.getBooleanExtra("flag", false);
		if (flag) {
			Log.i("flag", "插入成功");
			phonemode pm = new phonemode();
			pm.setPhone(insertphone);
			pm.setMode(insertmode);
			list.add(pm);
			getChange();
			adapter.notifyDataSetChanged();
		} else
			Log.i("flag", "插入失败");
	}
}
