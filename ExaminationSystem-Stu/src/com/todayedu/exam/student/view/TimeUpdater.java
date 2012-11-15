package com.todayedu.exam.student.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.TextView;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.utils.L;
import com.todayedu.exam.student.utils.Util;

/**
 * ����ʱ���߳�
 * 
 * @author Craig Lee
 * 
 */
public class TimeUpdater implements Runnable {
	private final Handler handler = new Handler();
	protected BaseActivity mContext;
	protected TextView mTextView;
	protected boolean mRunning = true;

	public TimeUpdater(BaseActivity mContext, TextView mTextView) {
		super();
		this.mContext = mContext;
		this.mTextView = mTextView;
	}

	@Override
	public void run() {
		while (mRunning) {
			if (App.examLeftTime >= 0) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						mTextView.setText(Util.parseExamTime(App.examLeftTime));
					}
				});
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					L.e(e);
				}
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						// ����ʱ�䵽
						mTextView.setText(R.string.ProblemShow_timeIsUp);
						onTimeUp();
					}
				});
				mRunning = false;
				break;
			}

		}
	}

	/**
	 * ʱ�䵽���¼�����
	 */
	public void onTimeUp() {
		new AlertDialog.Builder(mContext)
				.setMessage(R.string.ProblemShow_timeIsUp)
				.setPositiveButton(mContext.getString(R.string.common_yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ����
								BaseActivity.clearActivityManager();
							}
						}).setCancelable(false).show();
	}

	/**
	 * ��ֹ��ʱ���½���
	 */
	public void stopTimeUpdate() {
		this.mRunning = false;
	}

}
