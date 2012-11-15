package com.todayedu.exam.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.todayedu.exam.student.Const.Broadcast;
import com.todayedu.exam.student.service.NetService;
import com.todayedu.exam.student.view.ExamFinishedViewCreator;
import com.todayedu.exam.student.view.ExamUnfinishedViewCreator;

/**
 * 考试选择界面
 * 
 * @author Craig Lee
 * 
 */
public class ExamChoose extends BaseActivity {

	private LinearLayout ll_examlist;
	private Button btn_finishedExam;
	private Button btn_unfinishedExam;
	// 新考试接收器
	private NewExamReceiver myReceiver;
	// 未进行的考试View
	private ExamUnfinishedViewCreator examUnfinishedViewCreator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myReceiver = new NewExamReceiver();
		examUnfinishedViewCreator = new ExamUnfinishedViewCreator(this, null);
		initLayout();
	}

	private void initLayout() {
		setContentView(R.layout.examchoose);

		ll_examlist = (LinearLayout) findViewById(R.id.ExamChoose_ll_examlist);
		btn_finishedExam = (Button) findViewById(R.id.ExamChoose_btn_finishedExam);
		btn_unfinishedExam = (Button) findViewById(R.id.ExamChoose_btn_unfinishedExam);

		btn_finishedExam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 载入已经考过的考试列表
				loadFinishedExam();
			}
		});

		btn_unfinishedExam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 载入未考过的考试列表
				loadUnFinishedExam();
			}
		});
	}

	// 载入已经考过的考试列表
	private void loadFinishedExam() {
		btn_unfinishedExam.setSelected(false);
		btn_finishedExam.setSelected(true);
		ll_examlist.removeAllViews();
		ll_examlist.addView(new ExamFinishedViewCreator(this, null).getView());
	}

	// 载入未考过的考试列表
	private void loadUnFinishedExam() {
		btn_unfinishedExam.setSelected(true);
		btn_finishedExam.setSelected(false);
		ll_examlist.removeAllViews();
		examUnfinishedViewCreator.loadNewExam();
		ll_examlist.addView(examUnfinishedViewCreator.getView());
	}

	@Override
	protected void onResume() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Broadcast.EXAM_CHOOSE_NEW_EXAM);
		this.registerReceiver(myReceiver, intentFilter);
		loadFinishedExam();
		super.onResume();
	}

	@Override
	protected void onPause() {
		this.unregisterReceiver(myReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Intent stopIntent = new Intent(this, NetService.class);
		stopIntent.putExtra(NetService.EXTRA_DATA_SERVICE_TYPE,
				NetService.SERVICE_CHOICE_DISCONNECT);
		this.stopService(stopIntent);
		super.onDestroy();
	}

	/**
	 * 广播接收器
	 * 
	 * @author Craig Lee
	 * 
	 */
	private class NewExamReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 通知获取到新的考试
			loadUnFinishedExam();
		}

	}
}
