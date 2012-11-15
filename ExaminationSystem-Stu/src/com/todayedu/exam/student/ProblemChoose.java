package com.todayedu.exam.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ebag.net.request.AnswerRequest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.todayedu.exam.student.Const.Broadcast;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.service.NetService;
import com.todayedu.exam.student.service.TimerService;
import com.todayedu.exam.student.utils.L;
import com.todayedu.exam.student.utils.Util;
import com.todayedu.exam.student.view.TimeUpdater;

/**
 * 考试题目选择界面
 * 
 * @author Craig Lee
 * 
 */
public class ProblemChoose extends BaseActivity implements OnItemClickListener {
	private GridView gv_problemList = null;
	private TextView tv_examName = null;
	private TextView tv_score = null;
	private Button btn_getScore = null;
	private TextView tv_timeRemain = null;

	private ExamDAO examDAO = null;
	private AnswerRecevier answerRecevier = null;

	private TimeUpdater timeUpdateThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.problemchoose);
		examDAO = ExamDAOImpl.getInstance(this);
		gv_problemList = (GridView) findViewById(R.id.ProblemChoose_gv_problemList);
		tv_examName = (TextView) findViewById(R.id.ProblemChoose_tv_examName);
		if (App.examMode == App.EXAM_MODE_FINISHED) {
			// 进入查看已经完成的考试界面
			initFinished();
		} else if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			// 进入查看未完成的考试界面
			initUnfinished();
		} else {
			// 错误
			showLongToast("程序内部数据传递出错！");
			this.finish();
		}
		tv_examName.setText(App.exam.getName());
		initProblemList();
	}

	// 初始化题目选择列表
	private void initProblemList() {
		List<HashMap<String, Integer>> data = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> mapItem = null;
		for (int i = 1; i <= App.exam.getProblemList().size(); i++) {
			mapItem = new HashMap<String, Integer>();
			mapItem.put("problemnum", i);
			data.add(mapItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
				R.layout.problemitem, new String[] { "problemnum" },
				new int[] { R.id.ProblemChooseItem_tv_problemnum });
		gv_problemList.setAdapter(simpleAdapter);
		gv_problemList.setOnItemClickListener(this);

	}

	// 进入查看已经完成的考试界面
	private void initFinished() {
		findViewById(R.id.ProblemChoose_rl_unfinished).setVisibility(View.GONE);
		Button btn_back = (Button) findViewById(R.id.ProblemChoose_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回到考试选择界面
				ProblemChoose.this.finish();
			}
		});
		tv_score = (TextView) findViewById(R.id.TitleBar_tv_score);
		btn_getScore = (Button) findViewById(R.id.ProblemChoose_btn_getScore);
		btn_getScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 向netService发送广播请求获取成绩,并在此注册广播接收器，接受重新获取到的exam，对于更新数据库的操作在clienthandler中进行
				IntentFilter getScoreFilter = new IntentFilter();
				getScoreFilter.addAction(Broadcast.PROBLEM_CHOOSE_SCORE);
				answerRecevier = new AnswerRecevier();
				ProblemChoose.this.registerReceiver(answerRecevier,
						getScoreFilter);
				// 考完没有获取过成绩
				Intent scoreIntent = new Intent(ProblemChoose.this,
						NetService.class);
				AnswerRequest ar = new AnswerRequest();
				ar.uid = App.uid;
				ar.examId = App.exam.getId();
				scoreIntent.putExtra(NetService.EXTRA_DATA_SERVICE_TYPE,
						NetService.SERVICE_CHOICE_SEND_OBJECT);
				scoreIntent.putExtra(NetService.EXTRA_DATA_OBJECT_TO_BE_SENT,
						ar);
				startService(scoreIntent);
				showShortToast("开始获取考试成绩！^^");
			}
		});
		judgeExamStatus();
	}

	// 判断考试状态
	private void judgeExamStatus() {
		int status = App.exam.getStatus();
		if (status == Exam.STATUS_FINISHED_WITH_NO_GRADE) {
			tv_score.setText("总成绩：" + "暂无");
		} else if (status == Exam.STATUS_FINISHED_WITH_GRADE) {
			btn_getScore.setVisibility(View.GONE);
			tv_score.setText("总成绩：" + App.exam.getScore() + "分");
		}

	}

	// 进入查看未完成的考试界面
	private void initUnfinished() {
		findViewById(R.id.ProblemChoose_rl_finished).setVisibility(View.GONE);
		tv_timeRemain = (TextView) findViewById(R.id.ProblemChooseTitleBar_tv_timeRemain);
		final Button btn_handIn = (Button) findViewById(R.id.ProblemChoose_btn_handIn);

		// 预置时间
		App.examLeftTime = App.exam.getRemainTime();
		// 启动计时器
		Intent service = new Intent(this, TimerService.class);
		service.putExtra(TimerService.EXTRA_DATA_SERVICE_TYPE,
				TimerService.SERVICE_CHOICE_START_TIME);
		startService(service);

		btn_handIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createSubmitAlertDialog();
			}

		});
	}

	// 提交试卷提醒对话框
	private void createSubmitAlertDialog() {
		new AlertDialog.Builder(ProblemChoose.this)
				.setTitle("交卷")
				.setMessage("请确认完成题目，现在交卷完成考试？")
				.setPositiveButton(getString(R.string.common_confirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								handler.post(new Runnable() {
									@Override
									public void run() {
										handInExam();
									}
								});
							}
						})
				.setNegativeButton(getString(R.string.common_cancle),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						}).show();
	}

	// 交卷
	private void handInExam() {
		showProgressDialog("试卷提交中,请耐心等待...");
		// 交卷，并停止计时
		Intent intent = new Intent(ProblemChoose.this, TimerService.class);
		intent.putExtra(TimerService.EXTRA_DATA_SERVICE_TYPE,
				TimerService.SERVICE_CHOICE_STOP_TIME);
		startService(intent);
		boolean result = true;
		try {
			Util.submitExam(this, App.exam.getId());
		} catch (Throwable e) {
			L.e(e);
			result = false;
		}
		stopProgressDialog();
		if (result) {
			this.showShortToast("试卷已经成功提交！^.^");
			this.finish();
		} else {
			this.showLongToast("提交时出错，请稍后重试！>.<");
		}

	}

	@Override
	protected void onResume() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			timeUpdateThread = new TimeUpdater(this, tv_timeRemain) {
				@Override
				public void onTimeUp() {
					super.onTimeUp();
					gv_problemList.setEnabled(false);
				}
			};
			App.executorService.submit(timeUpdateThread);
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED) {
			this.timeUpdateThread.stopTimeUpdate();
		} else {
			// 成绩接收器
			if (this.answerRecevier != null) {
				this.unregisterReceiver(answerRecevier);
				answerRecevier = null;
			}
		}
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		App.problemPosition = position;
		Intent intent = new Intent(this, ProblemShow.class);
		// 选择一个题目，跳转到题目详情界面
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (App.examMode == App.EXAM_MODE_UNFINISHED)
			createSubmitAlertDialog();
		else
			super.onBackPressed();
	}

	/**
	 * 对于考完的考试获取到答案和成绩
	 * 
	 * @author Craig Lee
	 * 
	 */
	private class AnswerRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 通知更新界面
			showLongToast("考试数据已经更新！^.^");
			App.exam = examDAO.get(App.exam.getId());
			judgeExamStatus();
		}

	}

}
