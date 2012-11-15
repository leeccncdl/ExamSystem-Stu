package com.todayedu.exam.student.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.utils.L;

/**
 * 考试倒计时服务
 * 
 * @author Craig Lee
 * 
 */
public class TimerService extends Service {
	/**
	 * 计时服务类型
	 */
	public static final String EXTRA_DATA_SERVICE_TYPE = "extra_data_service_type";

	// 消息类型
	/**
	 * 开始计时
	 */
	public static final int SERVICE_CHOICE_START_TIME = 1;
	/**
	 * 停止计时
	 */
	public static final int SERVICE_CHOICE_STOP_TIME = 0;

	// 倒计时器
	private CountDownTimer countDownTimer = null;
	private ExamDAO examDAO = null;
	private Handler handler;

	public TimerService() {
		super();
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 创建线程计时
		HandlerThread handlerThread = new HandlerThread(
				"com.todayedu.examinationsystem.student.timerHandler");
		handlerThread.start();
		this.examDAO = ExamDAOImpl.getInstance(getBaseContext());
		this.handler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = (Intent) msg.obj;
				// 控制计时
				int msgType = intent.getIntExtra(EXTRA_DATA_SERVICE_TYPE,
						SERVICE_CHOICE_STOP_TIME);
				switch (msgType) {
					case SERVICE_CHOICE_START_TIME:
						startTime();
						break;
					case SERVICE_CHOICE_STOP_TIME:
						stopTime();
						break;
					default:
						break;
				}
			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = this.handler.obtainMessage();
		msg.obj = intent;
		msg.sendToTarget();
		return super.onStartCommand(intent, flags, startId);
	}

	// 开始计时
	private void startTime() {
		L.i("start count!");
		// 每隔1s把剩余时间更新到数据库
		this.countDownTimer = new ExamTimer(App.exam.getRemainTime(), 1000);
		this.countDownTimer.start();
		examDAO.updateStatus(App.exam.getId(), Exam.STATUS_UNDER_FINISHED);
	}

	// 停止计时
	private void stopTime() {
		if (this.countDownTimer != null) {
			this.countDownTimer.cancel();
			this.countDownTimer = null;
		}
		examDAO.updateStatus(App.exam.getId(),
				Exam.STATUS_FINISHED_WITH_NO_GRADE);
		this.stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 倒计时内部类
	 * 
	 * @author Craig Lee
	 * 
	 */
	private class ExamTimer extends CountDownTimer {

		public ExamTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			App.examLeftTime = millisUntilFinished;
			// 更新到数据库
			examDAO.updateRemainTime(App.exam.getId(), millisUntilFinished);
		}

		@Override
		public void onFinish() {
			// 更新数据库
			examDAO.updateRemainTime(App.exam.getId(), 0l);
			examDAO.updateStatus(App.exam.getId(),
					Exam.STATUS_FINISHED_WITH_NO_GRADE);
			// 考试时间用完，需要交卷
		}
	}

}
