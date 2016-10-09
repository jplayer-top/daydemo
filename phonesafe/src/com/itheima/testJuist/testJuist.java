package com.itheima.testJuist;

import java.util.List;

import android.test.AndroidTestCase;

import com.itheima.db.dao.mySQL;
import com.itheima.utils.phonemode;

public class testJuist extends AndroidTestCase {
	public void testadd() throws Exception {
		mySQL ms = new mySQL(getContext());
		ms.insert("123", "0");
	}

	public void testdelete() throws Exception {
		mySQL ms = new mySQL(getContext());
		ms.delete("123");
	}

	public void testupdate() throws Exception {
		mySQL ms = new mySQL(getContext());
		ms.update("123", "1");
	}
	public void testfind() throws Exception{
		mySQL ms = new mySQL(getContext());
		List<phonemode> list = ms.find();
		assertEquals(1, list.size());
		System.out.println(list.size());
	}

	public void testselect() throws Exception {
		mySQL ms = new mySQL(getContext());
		String mode = ms.select("123");
		if (mode != null) {
			if (mode.equals("0")) {
				System.out.println("选择全部拦截");
			} else if (mode.equals("1")) {

				System.out.println("选择拦截短信");

			}
		} else {
			System.out.println("不是黑名单");

		}
	}
}
