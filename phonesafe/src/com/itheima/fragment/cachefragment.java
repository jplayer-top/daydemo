package com.itheima.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.services.scanCacheService;

public class cachefragment extends Fragment {
	private LinearLayout ll_load_cache;
	private ListView lv_addviewcache;
	private TextView tv_scan_cache;
	private ProgressBar pb_cache;
	private PackageManager mpm;
	private final int SCANING = 1;
	private final int SCANOVER = 2;
	private List<appInfos> allappInfos;
	private myBaseAdapter adapter;
	private myOnItemClickListener listener;
	private Button bt_cleanAll;
	private SharedPreferences sp;
	private updateListReceiver receiver;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				String appname = (String) msg.obj;
				tv_scan_cache.setText("正在扫描" + appname);
				break;
			case SCANOVER:
				tv_scan_cache.setText("扫描完毕");
				ll_load_cache.setVisibility(View.INVISIBLE);
				bt_cleanAll.setClickable(true);
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = View.inflate(getActivity(), R.layout.cache_clean, null);
		lv_addviewcache = (ListView) view.findViewById(R.id.lv_addcacheItem);
		tv_scan_cache = (TextView) view.findViewById(R.id.tv_scan_appcache);
		pb_cache = (ProgressBar) view.findViewById(R.id.pb_cache);
		ll_load_cache = (LinearLayout) view.findViewById(R.id.ll_load_cache);
		bt_cleanAll = (Button) view.findViewById(R.id.bt_cleanAll);
		bt_cleanAll.setClickable(false);
		sp = getActivity().getSharedPreferences("config", 0);
		return view;
	}

	/**
	 * 用于存放有缓存的条目信息
	 * 
	 * @author oblivion
	 * 
	 */
	class appInfos {
		String appname;
		long cacheSize;
		Drawable appIcon;
		String packagename;

		@Override
		public String toString() {
			return "appInfos [appname=" + appname + ", cacheSize=" + cacheSize
					+ ", appIcon=" + appIcon + "]";
		}

	}

	/**
	 * 获取每一个程序的缓存大小
	 * 
	 * @param packagename
	 */
	private void getCacheSize(final String packagename) {
		new Thread() {
			public void run() {
				Method[] methods = PackageManager.class.getDeclaredMethods();
				for (Method method : methods) {
					if (method.getName().equals("getPackageSizeInfo")) {
						try {
							method.invoke(mpm, packagename,
									new IPackageStatsObserver.Stub() {

										@Override
										public void onGetStatsCompleted(
												PackageStats pStats,
												boolean succeeded)
												throws RemoteException {
											long cacheSize = pStats.cacheSize;
											if (cacheSize > 0) {
												try {
													ApplicationInfo Info = mpm
															.getApplicationInfo(
																	packagename,
																	0);
													Drawable appIcon = Info
															.loadIcon(mpm);
													String appname = Info
															.loadLabel(mpm)
															.toString();
													appInfos appInfo = new appInfos();
													appInfo.appIcon = appIcon;
													appInfo.appname = appname;
													appInfo.cacheSize = cacheSize;
													appInfo.packagename = packagename;
													allappInfos.add(appInfo);

												} catch (NameNotFoundException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
											System.out.println(cacheSize);
										}

									});
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}
			};

		}.start();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (lv_addviewcache.getChildCount() != 0) {
			return;
		}
		new Thread() {
			public void run() {
				allappInfos = new ArrayList<cachefragment.appInfos>();
				mpm = getActivity().getPackageManager();
				List<PackageInfo> Infos = mpm.getInstalledPackages(0);
				pb_cache.setMax(Infos.size());
				int progress = 0;
				for (PackageInfo Info : Infos) {
					String packagename = Info.packageName;
					getCacheSize(packagename);
					String appname = Info.applicationInfo.loadLabel(mpm)
							.toString();
					Message msg = Message.obtain();
					msg.obj = appname;
					msg.what = SCANING;
					handler.sendMessage(msg);
					progress++;
					pb_cache.setProgress(progress);
					SystemClock.sleep(50);
				}
				Message msg = Message.obtain();
				msg.what = SCANOVER;
				handler.sendMessage(msg);
				System.out.println(allappInfos.toString());
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter = new myBaseAdapter();
						lv_addviewcache.setAdapter(adapter);
						listener = new myOnItemClickListener();
						lv_addviewcache.setOnItemClickListener(listener);
						bt_cleanAll.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Method[] methods = PackageManager.class
										.getDeclaredMethods();
								for (Method method : methods) {
									if ("freeStorageAndNotify".equals(method
											.getName())) {
										for (final appInfos appInfo : allappInfos) {
											try {
												method.invoke(
														mpm,
														Long.MAX_VALUE,
														new IPackageDataObserver.Stub() {

															@Override
															public void onRemoveCompleted(
																	String packageName,
																	boolean succeeded)
																	throws RemoteException {
																// TODO
																// Auto-generated
																// method stub
																if (succeeded) {
																	System.out
																			.println(succeeded);
																}
																System.out
																		.println(succeeded);
															}
														});
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (IllegalAccessException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											allappInfos.remove(appInfo);
										}
										break;
									}
								}
								
								adapter.notifyDataSetChanged();
								if(allappInfos.size()==0){
									//这里可以加上判断是否为空，然后土司一个无缓存的logo
								}
							}
						});
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
			Intent intent = new Intent();
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.parse("package:"
					+ allappInfos.get(position).packagename));
			startActivity(intent);
			/*appchange ac = new appchange();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
			filter.addDataScheme("package");
			getActivity().registerReceiver(ac, filter);*/
			Editor editor = sp.edit();
			editor.putString("packagename",  allappInfos.get(position).packagename);
			editor.putInt("position", position);
			editor.commit();
			//创建一个服务，携带一个包名过去，并通过这个服务查询这个包下的缓存数据
			Intent serviceIntent = new Intent(getActivity(), scanCacheService.class);
			getActivity().startService(serviceIntent);
			//创建一个广播接受者，用来动态接受回收到的清除数据后的广播
			 receiver = new updateListReceiver();
			 IntentFilter filter = new IntentFilter();
			 filter.addAction("com.android.findcachesizeiszero");
			 getActivity().registerReceiver(receiver, filter);
		}

	}
	class updateListReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			allappInfos.remove(intent.getIntExtra("position", 0));
			adapter.notifyDataSetChanged();
			context.unregisterReceiver(receiver);
			receiver = null;
		}
		
	}

	/**
	 * 测试数据修改的广播，发现并无卵用
	 * 
	 * @author oblivion
	 * 
	 */
	/*class appchange extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("change", "cahnge");
		}

	}
*/
	/**
	 * 创建适配器
	 * 
	 * @author oblivion
	 * 
	 */
	private class myBaseAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(getActivity(), R.layout.item_cache, null);
			TextView tv_appname = (TextView) view
					.findViewById(R.id.tv_cache_appname);
			ImageView iv_appIcon = (ImageView) view
					.findViewById(R.id.iv_cache_appicon);
			TextView tv_cachesize = (TextView) view
					.findViewById(R.id.tv_cachesize);
			tv_appname.setText(allappInfos.get(position).appname);
			tv_cachesize.setText(Formatter.formatFileSize(getActivity(),
					allappInfos.get(position).cacheSize));
			iv_appIcon.setImageDrawable(allappInfos.get(position).appIcon);
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allappInfos.size();
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

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
