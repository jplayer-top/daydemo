package com.itheima.services;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.itheima.utils.ModifyOffset;

public class getRelLocation<PointDouble> extends Service {
	private myLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("sss", "·þÎñ¿ªÆô");
		LocationManager loactionmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new myLocationListener();
		loactionmanager.requestLocationUpdates("gps", 0, 0, listener);
		return super.onStartCommand(intent, flags, startId);
	}

	private class myLocationListener implements LocationListener {
		private SharedPreferences sp;

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences("config", 0);
			String phonenum = sp.getString("phonenum", "");
			double la = location.getLatitude();
			double lo = location.getLongitude();
			Log.i("sss", la + "----" + lo);
			try {
				InputStream is = getAssets().open("axisoffset.dat");//axisoffset.dat
				ModifyOffset modifyoffset = ModifyOffset.getInstance(is);
				String relLocation = modifyoffset.s2c(
						new com.itheima.utils.PointDouble(la, lo)).toString();
				Log.i("sss", relLocation+"----");
				SmsManager.getDefault().sendTextMessage(phonenum, null,
						relLocation, null, null);
				stopSelf();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}
}
