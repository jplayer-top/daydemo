package com.itheima.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

import com.itheima.activitys.homeActivity;

public class onBackPress {
	public static void setOnbackpress(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("��ܰ��ʾ");
		builder.setMessage("ȷ���˳������򵼣�");
		builder.setNegativeButton("������˼", null);
		builder.setPositiveButton("������ּ", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context, homeActivity.class);
				context.startActivity(intent);
				//������Ϊʲô������----------
				((Activity) context).finish();
			}
		});
		Log.i("ssss", "ִ������");
		builder.show();
	}
}
