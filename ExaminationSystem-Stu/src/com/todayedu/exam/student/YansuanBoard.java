package com.todayedu.exam.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todayedu.exam.student.paintpad.PaintPadViewCreator;
import com.todayedu.exam.student.utils.L;
import com.todayedu.exam.student.view.TimeUpdater;

/**
 * 演算板界面
 * 
 * @author Craig Lee
 * 
 */
public class YansuanBoard extends BaseActivity {
	private PaintPadViewCreator paintPadViewCreator;

	private LinearLayout ll_paintPad;
	private Button btn_back;
	private TextView tv_timeRemain;
	private TimeUpdater timeUpdateThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.yansuanboard);
		ll_paintPad = (LinearLayout) findViewById(R.id.YansuanBoard_ll_paintPad);
		btn_back = (Button) findViewById(R.id.YansuanBoard_btn_back);
		tv_timeRemain = (TextView) findViewById(R.id.YansuanBoardTitleBar_tv_timeRemain);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 获取考试号与题号
		paintPadViewCreator = new PaintPadViewCreator(this, null,
				App.exam.getId(), App.problem.getId(), false);
		ll_paintPad.addView(paintPadViewCreator.getView());

		new AlertDialog.Builder(this)
				.setMessage("载入之前的演算草稿？")
				.setPositiveButton(R.string.common_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								paintPadViewCreator.loadExsitedImage();
							}
						})
				.setNegativeButton(R.string.common_no,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).show();
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			activityManager.add(this);
		}
	}

	@Override
	protected void onResume() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			timeUpdateThread = new TimeUpdater(this, tv_timeRemain);
			L.i("yansuan time thread" + timeUpdateThread);
			App.executorService.submit(timeUpdateThread);

		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		paintPadViewCreator.onClickButtonSave();
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			timeUpdateThread.stopTimeUpdate();
		}
		super.onStop();
	}
}
