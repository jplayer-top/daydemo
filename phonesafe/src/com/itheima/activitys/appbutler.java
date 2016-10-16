package com.itheima.activitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.utils.appInfosdoman;
import com.itheima.utils.appinfos;

public class appbutler extends Activity implements OnClickListener {
	public static final int ItemViewType_TITLE = 0;
	public static final int ItemViewType_CONTENT = 1;
	private TextView tv_ram;
	private TextView tv_rom;
	private ListView lv_infos;
	private List<appInfosdoman> Infos;
	private myAdapter adapter;
	private LinearLayout ll_infos_loadding;
	private List<appInfosdoman> userInfos;
	private List<appInfosdoman> systemInfos;
	private TextView tv_visible;
	private myOnItemClickListener listener;
	private PopupWindow mPupupWindow;
	private ImageView install;
	private ImageView open;
	private ImageView shared;
	private ImageView message;
	private appInfosdoman Infoposition;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mPupupWindow != null) {
			mPupupWindow.dismiss();
			mPupupWindow = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appbutler);
		tv_ram = (TextView) findViewById(R.id.tv_ram);
		tv_rom = (TextView) findViewById(R.id.tv_rom);
		tv_ram.setText(appinfos.getRam(appbutler.this));
		tv_rom.setText(appinfos.getRom(appbutler.this));
		lv_infos = (ListView) findViewById(R.id.lv_infos);
		listener = new myOnItemClickListener();
		lv_infos.setOnItemClickListener(listener);
		tv_visible = (TextView) findViewById(R.id.tv_visible);
		lv_infos.setOnScrollListener(new myOnScrolllistener());
		ll_infos_loadding = (LinearLayout) findViewById(R.id.ll_infos_loadding);
		ll_infos_loadding.setVisibility(View.VISIBLE);
		tv_visible.setVisibility(View.INVISIBLE);
		new Thread() {
			public void run() {
				Infos = appinfos.getAppInfos(getApplicationContext());
				userInfos = new ArrayList<appInfosdoman>();
				systemInfos = new ArrayList<appInfosdoman>();
				for (appInfosdoman Info : Infos) {
					if (Info.getSystemApp()) {
						systemInfos.add(Info);
					} else {
						userInfos.add(Info);
					}
				}
				Log.i("appbutler",
						userInfos.size() + "---" + systemInfos.size());
				adapter = new myAdapter();
				System.out.println(Infos.toString());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ll_infos_loadding.setVisibility(View.INVISIBLE);
						tv_visible.setVisibility(View.VISIBLE);
						lv_infos.setAdapter(adapter);
					}
				});
			};
		}.start();
	}

	private class myOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return;
			} else if (position == userInfos.size() + 1) {
				return;
			} else {

				if (mPupupWindow != null) {
					mPupupWindow.dismiss();
					mPupupWindow = null;
				}
				View contentView = View.inflate(getApplicationContext(),
						R.layout.item_popupwindow, null);
				install = (ImageView) contentView
						.findViewById(R.id.iv_popupwindow_install);
				open = (ImageView) contentView
						.findViewById(R.id.iv_popupwindow_open);
				shared = (ImageView) contentView
						.findViewById(R.id.iv_popupwindow_shared);
				message = (ImageView) contentView
						.findViewById(R.id.iv_popupwindow_message);
				if (position <= userInfos.size()) {// userInfos.size()=4
					// 需要将获取的position-1才能得到正确的指针位置;
					Infoposition = userInfos.get(position - 1);

				} else {
					// 需要将获取的position-1-1-userInfos.size()才能得到正确的指针位置;
					Infoposition = systemInfos.get(position - userInfos.size()
							- 1 - 1);

				}
				install.setOnClickListener(appbutler.this);
				open.setOnClickListener(appbutler.this);
				shared.setOnClickListener(appbutler.this);
				message.setOnClickListener(appbutler.this);
				int[] locations = new int[2];
				view.getLocationInWindow(locations);
				mPupupWindow = new PopupWindow(contentView, -2, -2);
				// 如果添加背景的设置放在了show之后这样会没有结果，所以如果要实现动画效果必须在之前更新背景的设置
				mPupupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				mPupupWindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT,
						65, locations[1]);
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(1000);
				contentView.startAnimation(sa);
			}
		}

	}

	private class myOnScrolllistener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (mPupupWindow != null) {
				mPupupWindow.dismiss();
				mPupupWindow = null;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			Log.i("appbutler", "被滑动了");
			if (userInfos != null && systemInfos != null) {

				if (firstVisibleItem > userInfos.size()) {
					tv_visible.setText("系统安装的应用程序:" + systemInfos.size());
				} else {
					tv_visible.setText("用户安装的应用程序:" + userInfos.size());
				}
			}
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_appName;
		TextView tv_appSize;
		ImageView iv_isexeratry;
	}

	private class myAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			ViewHolder holder = null;
			// 判断的是convertView 的类型，这里需要将他们的复用类型分开;
			int type = getItemViewType(position);
			if (convertView == null) {
				switch (type) {
				case ItemViewType_TITLE:
					break;
				case ItemViewType_CONTENT:
					view = View.inflate(getApplicationContext(),
							R.layout.item_appinfo_listview, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView) view
							.findViewById(R.id.iv_info_icon);
					holder.tv_appName = (TextView) view
							.findViewById(R.id.tv_info_appname);
					holder.tv_appSize = (TextView) view
							.findViewById(R.id.tv_info_appsize);
					holder.iv_isexeratry = (ImageView) view
							.findViewById(R.id.iv_isexeratry);
					view.setTag(holder);
					break;
				}
			} else {
				switch (type) {
				case ItemViewType_TITLE:
					break;
				case ItemViewType_CONTENT:
					view = convertView;
					holder = (ViewHolder) view.getTag();
					break;
				}
			}
			if (position == 0) {
				TextView userAppItems = new TextView(getApplicationContext());
				userAppItems.setText("用户安装的应用程序:" + userInfos.size());
				userAppItems.setBackgroundColor(0x88000000);
				userAppItems.setTextColor(0xaaffffff);
				return userAppItems;
			}
			if (position == userInfos.size() + 1) {
				TextView systemAppItems = new TextView(getApplicationContext());
				systemAppItems.setText("系统安装的应用程序:" + systemInfos.size());
				systemAppItems.setBackgroundColor(0x88000000);
				systemAppItems.setTextColor(0xaaffffff);
				return systemAppItems;
			}
			if (position <= userInfos.size()) {// userInfos.size()=4
				// 需要将获取的position-1才能得到正确的指针位置;
				Infoposition = userInfos.get(position - 1);
				holder.iv_icon.setImageDrawable(Infoposition.getAppicon());
				holder.tv_appName.setText(Infoposition.getAppname());
				holder.tv_appSize.setText(Formatter.formatFileSize(
						getApplicationContext(), Infoposition.getSize()));
				if (!Infoposition.getEXetary()) {
					holder.iv_isexeratry.setImageResource(R.drawable.memory);
				} else {
					holder.iv_isexeratry.setImageResource(R.drawable.sd);
				}
			} else {
				// 需要将获取的position-1-1-userInfos.size()才能得到正确的指针位置;
				Infoposition = systemInfos.get(position - userInfos.size() - 1
						- 1);
				holder.iv_icon.setImageDrawable(Infoposition.getAppicon());
				holder.tv_appName.setText(Infoposition.getAppname());
				holder.tv_appSize.setText(Formatter.formatFileSize(
						getApplicationContext(), Infoposition.getSize()));
				if (!Infoposition.getEXetary()) {
					holder.iv_isexeratry.setImageResource(R.drawable.memory);
				} else {
					holder.iv_isexeratry.setImageResource(R.drawable.sd);
				}
			}
			return view;

			// appInfosdoman InfosPosition = Infos.get(position);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1 + userInfos.size() + 1 + systemInfos.size();
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

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return ItemViewType_TITLE;
			} else if (position == userInfos.size() + 1) {
				return ItemViewType_TITLE;
			} else if (position <= userInfos.size()) {// userInfos.size()=4
				// 需要将获取的position-1才能得到正确的指针位置;
				return ItemViewType_CONTENT;
			} else {
				return ItemViewType_CONTENT;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

	}
/**
 * 创建一个广播接受后用来处理接收到的广播
 */
	private appUninstallReceiver receiver;

	private class appUninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String data = intent.getData().toString();
			Log.i("data", data);
			Log.i("Infoposition", Infoposition.getPackagename());
			Iterator<appInfosdoman> iterator = userInfos.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getPackagename()
						.equals(Infoposition.getPackagename())) {
					iterator.remove();
				}
			}
			adapter.notifyDataSetChanged();
			unregisterReceiver(receiver);
			receiver = null;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mPupupWindow != null) {
			mPupupWindow.dismiss();
			mPupupWindow = null;
		}
		switch (v.getId()) {
		case R.id.iv_popupwindow_install:
			Log.i("appbutler", "卸载");
			//创建一个广播接受者用来接收广播，接收后的哦后续事项交与下一步广播处理
			receiver = new appUninstallReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			filter.addDataScheme("package");
			registerReceiver(receiver, filter);
			Intent installIntent = new Intent();
			installIntent.setAction("android.intent.action.DELETE");
			installIntent.addCategory("android.intent.category.DEFAULT");
			installIntent.setData(Uri.parse("package:"
					+ Infoposition.getPackagename()));
			startActivity(installIntent);

			break;
		case R.id.iv_popupwindow_open:
			Log.i("appbutler", "开启" + Infoposition.getPackagename());
			PackageManager pm = getPackageManager();
			Intent openIntent = pm.getLaunchIntentForPackage(Infoposition
					.getPackagename());
			if (openIntent != null) {
				startActivity(openIntent);
			} else {
				Toast.makeText(getApplicationContext(), "程序无法开启", 0).show();
				return;
			}
			break;
		case R.id.iv_popupwindow_shared:

			Intent sharedIntent = new Intent();
			sharedIntent.setAction("android.intent.action.SENDTO");
			sharedIntent.addCategory("android.intent.category.DEFAULT");
			sharedIntent.addCategory("android.intent.category.BROWSABLE");
			sharedIntent.setData(Uri.parse("smsto:" + ""));
			sharedIntent.putExtra(
					"sms_body",
					"推荐你使用一款软件,下载地址,http://bbs.itheima.com"
							+ Infoposition.getPackagename());
			startActivity(sharedIntent);
			/*
			 * Intent sharedintent = new Intent();
			 * sharedintent.setAction("android.intent.action.SEND");
			 * sharedintent.addCategory("android.intent.category.DEFAULT");
			 * sharedintent.setType("text/plain");
			 * sharedintent.putExtra(Intent.EXTRA_TEXT,
			 * "这款软件"+Infoposition.getPackagename()+"很好用你也要来玩吗");
			 * startActivity(sharedintent);
			 */
			break;
		case R.id.iv_popupwindow_message:
			Log.i("appbutler", "信息" + Infoposition.getPackagename());
			Intent messageintent = new Intent();
			messageintent
					.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			messageintent.addCategory("android.intent.category.DEFAULT");
			messageintent.setData(Uri.parse("package:"
					+ Infoposition.getPackagename()));
			startActivity(messageintent);
			break;
		}
	}
}
