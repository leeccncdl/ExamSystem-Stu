package com.todayedu.exam.student;

import android.os.Bundle;

import com.todayedu.exam.student.utils.L;
import com.todayedu.exam.student.view.ProblemShowFinishedViewCreator;
import com.todayedu.exam.student.view.ProblemShowUnfinishedViewCreator;
import com.todayedu.exam.student.view.TimeUpdater;

/**
 * 答题界面
 * 
 * @author Craig Lee
 * 
 */
public class ProblemShow extends BaseActivity {

	private ProblemShowUnfinishedViewCreator problemShowUnfinishedViewCreator = null;
	private TimeUpdater timeUpdateThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		if (App.examMode == App.EXAM_MODE_FINISHED) {
			//初始化考完试的界面
			initFinished();
		} else if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			//初始化未考试的界面
			initUnfinished();
		} else {
			// 错误
			showLongToast("程序内部数据传递出错！");
			this.finish();
		}
	}
	//初始化未考试的界面
	private void initUnfinished() {
		activityManager.add(this);
		this.problemShowUnfinishedViewCreator = new ProblemShowUnfinishedViewCreator(
				this, null);
		setContentView(problemShowUnfinishedViewCreator.getView());
	}
	//初始化考完试的界面
	private void initFinished() {
		setContentView(new ProblemShowFinishedViewCreator(this, null).getView());
	}

	@Override
	protected void onResume() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			timeUpdateThread = new TimeUpdater(this,
					problemShowUnfinishedViewCreator.getTv_timeRemain());
			L.i("problem show thread" + timeUpdateThread);
			App.executorService.submit(timeUpdateThread);
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			this.timeUpdateThread.stopTimeUpdate();
		}
		super.onStop();
	}
}
