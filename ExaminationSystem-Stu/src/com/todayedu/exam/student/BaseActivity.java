package com.todayedu.exam.student;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

/**
 * Activity基类
 * 
 * @author Craig Lee
 * 
 */
public abstract class BaseActivity extends Activity {
	/**
	 * 显示进度条
	 */
	public static final int PROGRESS_START = 0;
	/**
	 * 取消进度条
	 */
	public static final int PROGRESS_STOP = 1;

	/**
	 * 显示提示信息handler
	 */
	private Handler msgHandler;

	/**
	 * 处理从其他线程向主线程操作
	 */
	protected static Handler handler;

	/**
	 * 进度条对话框
	 */
	private ProgressDialog progressDialog;

	/**
	 * 每启动一个Activity就加入到该List中
	 */
	public static final List<Activity> activityManager = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 创建ProgressDialog线程
		HandlerThread handlerThread = new HandlerThread(
				"com.todayedu.examinationsystem.student.progressHandler");
		handlerThread.start();

		msgHandler = new Handler(handlerThread.getLooper()) {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case PROGRESS_START:
						if (progressDialog != null)
							progressDialog.show();
						break;
					case PROGRESS_STOP:
						if (progressDialog != null)
							progressDialog.dismiss();
						progressDialog = null;
						break;
					default:
						break;

				}

			}
		};
		HandlerThread mainHanderThread = new HandlerThread(
				"com.todayedu.examinationsystem.student.mainHandler");
		mainHanderThread.start();
		handler = new Handler(mainHanderThread.getLooper());
	}

	/**
	 * 显示等待框
	 * 
	 * @param msg
	 */
	public void showProgressDialog(String str) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				stopProgressDialog();
			}

		});
		progressDialog.setMessage(str);

		Message msg = msgHandler.obtainMessage();
		msg.what = PROGRESS_START;
		msg.sendToTarget();
	}

	/**
	 * 显示等待框
	 * 
	 * @param str
	 */
	public void showProgressDialog(int id) {
		showProgressDialog(getString(id));
	}

	/**
	 * 取消等待框
	 */
	public void stopProgressDialog() {
		Message msg = msgHandler.obtainMessage();
		msg.what = PROGRESS_STOP;
		msg.sendToTarget();
	}

	/**
	 * 显示长Toast
	 * 
	 * @param message
	 */
	public void showLongToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示长Toast
	 * 
	 * @param id
	 */
	public void showLongToast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示短Toast
	 * 
	 * @param message
	 */
	public void showShortToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示短Toast
	 * 
	 * @param id
	 */
	public void showShortToast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 清空Activity的List
	 */
	public static void clearActivityManager() {
		for (Activity a : activityManager) {
			if (a != null)
				a.finish();
		}
	}

}
