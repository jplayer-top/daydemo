package com.itheima.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.utils.runProgressInfo;
import com.itheima.utils.totalRunningProgress;

public class myRuningProgress extends Activity {
	private TextView tv_runprogress;
	private TextView tv_avilaMemo;
	private ActivityManager mActivityManager;
	private ListView lv_runningprogress;
	private myAdapter adapter;
	private List<runProgressInfo> Infos;
	private List<runProgressInfo> useInfos;
	private List<runProgressInfo> systemInfos;
	private myOnItemClickListener listener;
	private runProgressInfo InfogetPosition;
	private long avivMemo;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runninprogress);
		tv_runprogress = (TextView) findViewById(R.id.tv_runprogress);
		tv_avilaMemo = (TextView) findViewById(R.id.tv_canuseavimeo);
		avivMemo = getProgressAiviMemory();
		tv_avilaMemo.setText("可用的内存空间:"
				+ Formatter.formatFileSize(getApplicationContext(), avivMemo));
		count = getRunProgressCount();
		tv_runprogress.setText("正在运行的进程数目:" + count);
		lv_runningprogress = (ListView) findViewById(R.id.lv_runningprogress);
		listener = new myOnItemClickListener();
		lv_runningprogress.setOnItemClickListener(listener);
		new Thread() {
			public void run() {

				Infos = totalRunningProgress
						.getRunProgress(getApplicationContext());
				Log.i("counts", Infos.size()+"");
				useInfos = new ArrayList<runProgressInfo>();
				systemInfos = new ArrayList<runProgressInfo>();
				for (runProgressInfo Info : Infos) {
					if (Info.isSystem()) {
						systemInfos.add(Info);
					} else {
						useInfos.add(Info);
					}
				}
				System.out.println(Infos.toString());
				adapter = new myAdapter();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv_runningprogress.setAdapter(adapter);
					}
				});
			};
		}.start();
	}

	/**
	 * 反选
	 * 
	 * @param v
	 */
	public void bt_reverse(View v) {
		for (runProgressInfo Info : Infos) {
			if (Info.getPackageName().equals(getApplicationInfo().packageName)) {
				continue;
			}
			if (Info.isChecked()) {
				Info.setChecked(false);
			} else {
				Info.setChecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 全选
	 * 
	 * @param v
	 */
	public void bt_selectAll(View v) {
		for (runProgressInfo Info : Infos) {
			if (Info.getPackageName().equals(getApplicationInfo().packageName)) {
				continue;
			}
			Info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 清理选中的进程
	 * 
	 * @param v
	 *            清理进程
	 */
	public void bt_killprogress(View v) {
		List<runProgressInfo> killprogress = new ArrayList<runProgressInfo>();
		for (runProgressInfo Info : Infos) {
			if (Info.isChecked()) {
				killprogress.add(Info);
			}
		}
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		long killtotal = 0l;
		for (runProgressInfo Info : killprogress) {

			am.killBackgroundProcesses(Info.getPackageName());
			if (useInfos.contains(Info)) {
				useInfos.remove(Info);
			} else {
				systemInfos.remove(Info);
			}
			// String num = Info.getAppUseSize().substring(0, 4).trim();
			killtotal += Info.getAppUseSize();
		}
		adapter.notifyDataSetChanged();
		count -= killprogress.size();
		tv_runprogress.setText("正在运行的进程数目:" + count);
		avivMemo -= killtotal;
		tv_avilaMemo.setText("可用的内存空间:"
				+ Formatter.formatFileSize(getApplicationContext(), avivMemo));
		Toast.makeText(
				getApplicationContext(),
				"清理了"
						+ killprogress.size()
						+ "个进程,清理了"
						+ Formatter.formatFileSize(getApplicationContext(),
								killtotal) + "内存", 0).show();
	}

	/**
	 * 添加ListView的条目点击事件
	 */
	private class myOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return;
			} else if (position == useInfos.size() + 1) {
				return;
			} else {
				if (position <= useInfos.size()) {
					InfogetPosition = useInfos.get(position - 1);
				} else {
					InfogetPosition = systemInfos.get(position - 1 - 1
							- useInfos.size());
				}
				if (InfogetPosition.getPackageName().equals(
						getApplicationInfo().packageName)) {
					return;
				} else {
					InfogetPosition.setChecked(!InfogetPosition.isChecked());
					// 使用cb发现控件更优化。
					CheckBox cb = (CheckBox) view
							.findViewById(R.id.cb_run_check);
					cb.setChecked(InfogetPosition.isChecked());
				}
				// adapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 创建ViewHolder用来发现子控件，提高运行速率
	 * 
	 * @author oblivion
	 * 
	 */
	static class ViewHolder {
		ImageView iv_appIcon;
		TextView tv_appName;
		TextView tv_appSize;
		CheckBox cb_isCb;
	}

	/**
	 * 实现适配器类
	 * 
	 * @author oblivion
	 * 
	 */
	private class myAdapter extends BaseAdapter {
		private static final int ITEM_TITLE = 0;
		private static final int ITTEM_CONTENT = 1;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			ViewHolder holder = null;
			TextView tv = null;
			int type = getItemViewType(position);
			switch (type) {
			case ITEM_TITLE:
				if (convertView == null) {
					tv = new TextView(getApplicationContext());
					tv.setBackgroundColor(0x88000000);
					tv.setTextColor(0xaaffffff);
				} else {
					tv = (TextView) convertView;
				}
				break;
			case ITTEM_CONTENT:
				if (convertView == null) {
					view = View.inflate(getApplicationContext(),
							R.layout.item_runprogress, null);
					holder = new ViewHolder();
					holder.iv_appIcon = (ImageView) view
							.findViewById(R.id.iv_run_appicon);
					holder.tv_appName = (TextView) view
							.findViewById(R.id.tv_run_appname);
					holder.tv_appSize = (TextView) view
							.findViewById(R.id.tv_run_appsize);
					holder.cb_isCb = (CheckBox) view
							.findViewById(R.id.cb_run_check);
					view.setTag(holder);
				} else {
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}
				break;
			}
			if (position == 0) {
				tv.setText("用户安装的进程" + useInfos.size());
				return tv;
			} else if (position == useInfos.size() + 1) {
				tv.setText("系统自带的进程" + systemInfos.size());
				return tv;
			} else if (position <= useInfos.size()) {
				InfogetPosition = useInfos.get(position - 1);
			} else {
				InfogetPosition = systemInfos.get(position - 1
						- useInfos.size() - 1);
			}
			holder.iv_appIcon.setImageDrawable(InfogetPosition.getAppIcon());
			holder.tv_appName.setText(InfogetPosition.getAppName());
			holder.tv_appSize.setText(Formatter.formatFileSize(
					getApplicationContext(), InfogetPosition.getAppUseSize()));
			if (InfogetPosition.getPackageName().equals(
					getApplicationInfo().packageName)) {
				view.findViewById(R.id.cb_run_check).setVisibility(
						View.INVISIBLE);
			} else {
				holder.cb_isCb.setChecked(InfogetPosition.isChecked());
			}
			return view;
		}

		/**
		 * 记录添加的条目。目的是为了有标题
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return useInfos.size() + 1 + 1 + systemInfos.size();
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
				return ITEM_TITLE;
			} else if (position == useInfos.size() + 1) {
				return ITEM_TITLE;
			} else if (position <= useInfos.size()) {
				return ITTEM_CONTENT;
			} else {
				return ITTEM_CONTENT;
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

	}

	private int getRunProgressCount() {
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runAppInfo = mActivityManager
				.getRunningAppProcesses();
		int counts = runAppInfo.size();
		Log.i("test", counts+"");
		return runAppInfo.size()-2;
	}

	private long getProgressAiviMemory() {
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		mActivityManager.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
}
