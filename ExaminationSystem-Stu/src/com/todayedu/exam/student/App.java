package com.todayedu.exam.student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;

import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.model.Problem;

/**
 * Activity间传递数据的缓冲区
 * 
 * @author Craig Lee
 * 
 */
public class App extends Application {
	// 固定线程来执行任务
	public static ExecutorService executorService = Executors
			.newFixedThreadPool(3);
	/**
	 * 考试状态：已经完成
	 */
	public static final int EXAM_MODE_FINISHED = 1;
	/**
	 * 考试状态：未考试
	 */
	public static final int EXAM_MODE_UNFINISHED = 0;

	/**
	 * 用户id
	 */
	public static int uid;
	/**
	 * 考试剩余时间
	 */
	public static long examLeftTime = 0;

	/**
	 * 查看考试的状态
	 */
	public static int examMode;

	/**
	 * 进入考试界面后所查看的考试
	 */
	public static Exam exam;

	/**
	 * 当前题目
	 */
	public static Problem problem;

	/**
	 * 看到的考试题目在List中的位置
	 */
	public static int problemPosition;

}
