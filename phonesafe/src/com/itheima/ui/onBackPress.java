package com.itheima.ui;

import com.itheima.activitys.homeActivity;
import com.itheima.activitys.phontprotect2;
import com.itheima.activitys.phontprotect4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class onBackPress {
	public static void setOnbackpress(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("��ܰ��ʾ");
		builder.setMessage("ȷ���˳������򵼣�");
		builder.setNegativeButton("������˼", null);
		builder.setPositiveButton("������ּ", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						homeActivity.class);
				context.startActivity(intent);
				((Activity) context).finish();
			}
		});
		Log.i("ssss", "ִ������");
		builder.show();
	}
}
