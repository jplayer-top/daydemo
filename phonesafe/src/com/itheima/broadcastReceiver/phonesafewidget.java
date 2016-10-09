package com.itheima.broadcastReceiver;

import com.itheima.services.widgetservice;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class phonesafewidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Intent intent = new Intent(context, widgetservice.class);
		context.startService(intent);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context, widgetservice.class);
		context.stopService(intent);
	}
}
