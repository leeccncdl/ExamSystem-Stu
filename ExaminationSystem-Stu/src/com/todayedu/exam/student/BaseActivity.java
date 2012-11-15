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
 * Activity����
 * 
 * @author Craig Lee
 * 
 */
public abstract class BaseActivity extends Activity {
	/**
	 * ��ʾ������
	 */
	public static final int PROGRESS_START = 0;
	/**
	 * ȡ��������
	 */
	public static final int PROGRESS_STOP = 1;

	/**
	 * ��ʾ��ʾ��Ϣhandler
	 */
	private Handler msgHandler;

	/**
	 * ����������߳������̲߳���
	 */
	protected static Handler handler;

	/**
	 * �������Ի���
	 */
	private ProgressDialog progressDialog;

	/**
	 * ÿ����һ��Activity�ͼ��뵽��List��
	 */
	public static final List<Activity> activityManager = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ����ProgressDialog�߳�
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
	 * ��ʾ�ȴ���
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
	 * ��ʾ�ȴ���
	 * 
	 * @param str
	 */
	public void showProgressDialog(int id) {
		showProgressDialog(getString(id));
	}

	/**
	 * ȡ���ȴ���
	 */
	public void stopProgressDialog() {
		Message msg = msgHandler.obtainMessage();
		msg.what = PROGRESS_STOP;
		msg.sendToTarget();
	}

	/**
	 * ��ʾ��Toast
	 * 
	 * @param message
	 */
	public void showLongToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * ��ʾ��Toast
	 * 
	 * @param id
	 */
	public void showLongToast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
	}

	/**
	 * ��ʾ��Toast
	 * 
	 * @param message
	 */
	public void showShortToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ʾ��Toast
	 * 
	 * @param id
	 */
	public void showShortToast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ���Activity��List
	 */
	public static void clearActivityManager() {
		for (Activity a : activityManager) {
			if (a != null)
				a.finish();
		}
	}

}
