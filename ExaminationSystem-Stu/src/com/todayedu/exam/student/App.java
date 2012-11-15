package com.todayedu.exam.student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;

import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.model.Problem;

/**
 * Activity�䴫�����ݵĻ�����
 * 
 * @author Craig Lee
 * 
 */
public class App extends Application {
	// �̶��߳���ִ������
	public static ExecutorService executorService = Executors
			.newFixedThreadPool(3);
	/**
	 * ����״̬���Ѿ����
	 */
	public static final int EXAM_MODE_FINISHED = 1;
	/**
	 * ����״̬��δ����
	 */
	public static final int EXAM_MODE_UNFINISHED = 0;

	/**
	 * �û�id
	 */
	public static int uid;
	/**
	 * ����ʣ��ʱ��
	 */
	public static long examLeftTime = 0;

	/**
	 * �鿴���Ե�״̬
	 */
	public static int examMode;

	/**
	 * ���뿼�Խ�������鿴�Ŀ���
	 */
	public static Exam exam;

	/**
	 * ��ǰ��Ŀ
	 */
	public static Problem problem;

	/**
	 * �����Ŀ�����Ŀ��List�е�λ��
	 */
	public static int problemPosition;

}
