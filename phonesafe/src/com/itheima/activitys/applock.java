package com.itheima.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.db.dao.savepackagename;

public class applock extends Activity implements OnClickListener {
	private TextView tv_applock;
	private TextView tv_appunlock;
	private LinearLayout ll_applock;
	private LinearLayout ll_appunlock;
	private ListView lv_applock;
	private ListView lv_appunlock;
	private TextView tv_lockcounts;
	private TextView tv_unlockcounts;
	private lockAdapter lockadapter;
	private unlockAdapter unlockadapter;
	private PackageManager mPackageManager;
	private List<ApplicationInfo> applicationInfos;
	private unlockListener unlocklistener;
	private lockListener locklistener;
	private List<ApplicationInfo> unlockapplicationInfos;
	private List<ApplicationInfo> lockapplicationInfos;
	private savepackagename packagenameDao;

	/**
	 * 初始化数据，找到控件
	 */
	private void initView() {
		tv_applock = (TextView) findViewById(R.id.tv_applock);
		tv_appunlock = (TextView) findViewById(R.id.tv_appunlock);
		ll_applock = (LinearLayout) findViewById(R.id.ll_lock);
		ll_appunlock = (LinearLayout) findViewById(R.id.ll_unlock);
		lv_applock = (ListView) findViewById(R.id.lv_lockapp);
		lv_appunlock = (ListView) findViewById(R.id.lv_unlockapp);
		tv_lockcounts = (TextView) findViewById(R.id.tv_lockcounts);
		tv_unlockcounts = (TextView) findViewById(R.id.tv_unlockcounts);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applock);
		initView();
		tv_applock.setOnClickListener(this);
		tv_appunlock.setOnClickListener(this);
		unlockadapter = new unlockAdapter();
		new Thread() {
			public void run() {
				mPackageManager = getPackageManager();
				applicationInfos = mPackageManager.getInstalledApplications(0);
				unlockapplicationInfos = new ArrayList<ApplicationInfo>();
				lockapplicationInfos = new ArrayList<ApplicationInfo>();
				packagenameDao = new savepackagename(getApplicationContext());
				for (ApplicationInfo applicationInfo : applicationInfos) {
					if (packagenameDao.find(applicationInfo.packageName)) {
						lockapplicationInfos.add(applicationInfo);
					} else {
						unlockapplicationInfos.add(applicationInfo);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv_appunlock.setAdapter(unlockadapter);
					}
				});
			};
		}.start();
		unlocklistener = new unlockListener();
		lv_appunlock.setOnItemClickListener(unlocklistener);
	}

	/**
	 * 未加锁程序的条目点击事件 创建动画监听事件实现划出效果
	 * 
	 * @author oblivion
	 * 
	 */
	private class unlockListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			packagenameDao = new savepackagename(getApplicationContext());
			// 要不要判断是否已经存在于数据库中
			/*
			 * if(!packagenameDao.find(unlockapplicationInfos.get(position).
			 * packageName)){ return; }
			 */
			packagenameDao
					.add(unlockapplicationInfos.get(position).packageName);
			lockapplicationInfos.add(unlockapplicationInfos.get(position));
			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0);
			ta.setDuration(500);
			view.startAnimation(ta);
			ta.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					unlockapplicationInfos.remove(position);
					unlockadapter.notifyDataSetChanged();
				}
			});
		}
	}

	/**
	 * c创建ViewHolder 提高效率
	 * 
	 * @author oblivion
	 * 
	 */
	static class ViewHolder {
		ImageView appIcon;
		TextView appname;
	}

	/**
	 * 未加锁程序的适配器
	 * 
	 * @author oblivion
	 * 
	 */
	private class unlockAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.item_unlock, null);
				holder = new ViewHolder();
				holder.appIcon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.appname = (TextView) view.findViewById(R.id.tv_appname);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.appIcon.setImageDrawable(unlockapplicationInfos
					.get(position).loadIcon(mPackageManager));
			holder.appname.setText(unlockapplicationInfos.get(position)
					.loadLabel(mPackageManager));
			return view;
		}

		@Override
		public int getCount() {
			tv_unlockcounts.setText("未加锁的程序数目" + unlockapplicationInfos.size());
			return unlockapplicationInfos.size();
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

	private class lockAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.item_lock, null);
				holder = new ViewHolder();
				holder.appIcon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.appname = (TextView) view.findViewById(R.id.tv_appname);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.appIcon.setImageDrawable(lockapplicationInfos.get(position)
					.loadIcon(mPackageManager));
			holder.appname.setText(lockapplicationInfos.get(position)
					.loadLabel(mPackageManager));
			return view;
		}

		@Override
		public int getCount() {
			tv_lockcounts.setText("已加锁的应用程序" + lockapplicationInfos.size());
			return lockapplicationInfos.size();
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

	/**
	 * 枷锁和未枷锁的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_applock:
			tv_applock.setBackgroundResource(R.drawable.dg_btn_confirm_normal);
			tv_appunlock
					.setBackgroundResource(R.drawable.dg_btn_confirm_select);
			ll_applock.setVisibility(View.VISIBLE);
			ll_appunlock.setVisibility(View.INVISIBLE);
			lockadapter = new lockAdapter();
			lockadapter.notifyDataSetChanged();
			System.out.println(lockapplicationInfos.size() + "----");
			lv_applock.setAdapter(lockadapter);
			locklistener = new lockListener();
			lv_applock.setOnItemClickListener(locklistener);
			break;
		case R.id.tv_appunlock:
			tv_appunlock
					.setBackgroundResource(R.drawable.dg_btn_confirm_normal);
			tv_applock.setBackgroundResource(R.drawable.dg_btn_confirm_select);
			ll_appunlock.setVisibility(View.VISIBLE);
			ll_applock.setVisibility(View.INVISIBLE);
			unlockadapter.notifyDataSetChanged();
			break;
		}
	}

	private class lockListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			// TODO Auto-generated method stub
			packagenameDao = new savepackagename(getApplicationContext());
			// 要不要判断是否已经存在于数据库中
			/*
			 * if(!packagenameDao.find(unlockapplicationInfos.get(position).
			 * packageName)){ return; }
			 */
			packagenameDao
					.delete(lockapplicationInfos.get(position).packageName);
			unlockapplicationInfos.add(lockapplicationInfos.get(position));
			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0);
			ta.setDuration(500);
			view.startAnimation(ta);
			ta.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					lockapplicationInfos.remove(position);
					lockadapter.notifyDataSetChanged();
				}
			});
		}

	}
}
