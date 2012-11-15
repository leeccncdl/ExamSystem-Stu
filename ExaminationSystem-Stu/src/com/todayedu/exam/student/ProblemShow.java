package com.todayedu.exam.student;

import android.os.Bundle;

import com.todayedu.exam.student.utils.L;
import com.todayedu.exam.student.view.ProblemShowFinishedViewCreator;
import com.todayedu.exam.student.view.ProblemShowUnfinishedViewCreator;
import com.todayedu.exam.student.view.TimeUpdater;

/**
 * �������
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
			//��ʼ�������ԵĽ���
			initFinished();
		} else if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			//��ʼ��δ���ԵĽ���
			initUnfinished();
		} else {
			// ����
			showLongToast("�����ڲ����ݴ��ݳ���");
			this.finish();
		}
	}
	//��ʼ��δ���ԵĽ���
	private void initUnfinished() {
		activityManager.add(this);
		this.problemShowUnfinishedViewCreator = new ProblemShowUnfinishedViewCreator(
				this, null);
		setContentView(problemShowUnfinishedViewCreator.getView());
	}
	//��ʼ�������ԵĽ���
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
