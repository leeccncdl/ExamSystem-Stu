package com.todayedu.exam.student.utils;

import com.todayedu.exam.student.Const;

import android.util.Log;

/**
 * 日志输出工具
 * 
 * @author Craig Lee
 * 
 */
public class L {
	public static final String LOG_TAG = "com.todayedu.examinationsystem.student";

	/**
	 * 输出信息
	 * 
	 * @param o
	 */
	public static void i(Object o) {
		if (Const.DEV_MODE)
			Log.i(LOG_TAG, o == null ? "null" : o.toString());
	}

	/**
	 * 错误信息
	 * 
	 * @param o
	 */
	public static void e(Object o) {
		if (Const.DEV_MODE)
			Log.e(LOG_TAG, o == null ? "null" : o.toString());
	}

	/**
	 * 警告信息
	 * 
	 * @param o
	 */
	public static void w(Object o) {
		if (Const.DEV_MODE)
			Log.w(LOG_TAG, o == null ? "null" : o.toString());
	}

}
