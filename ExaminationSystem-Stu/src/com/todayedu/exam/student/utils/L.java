package com.todayedu.exam.student.utils;

import com.todayedu.exam.student.Const;

import android.util.Log;

/**
 * ��־�������
 * 
 * @author Craig Lee
 * 
 */
public class L {
	public static final String LOG_TAG = "com.todayedu.examinationsystem.student";

	/**
	 * �����Ϣ
	 * 
	 * @param o
	 */
	public static void i(Object o) {
		if (Const.DEV_MODE)
			Log.i(LOG_TAG, o == null ? "null" : o.toString());
	}

	/**
	 * ������Ϣ
	 * 
	 * @param o
	 */
	public static void e(Object o) {
		if (Const.DEV_MODE)
			Log.e(LOG_TAG, o == null ? "null" : o.toString());
	}

	/**
	 * ������Ϣ
	 * 
	 * @param o
	 */
	public static void w(Object o) {
		if (Const.DEV_MODE)
			Log.w(LOG_TAG, o == null ? "null" : o.toString());
	}

}