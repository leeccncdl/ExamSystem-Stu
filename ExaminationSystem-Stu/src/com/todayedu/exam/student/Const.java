package com.todayedu.exam.student;

import java.io.File;

/**
 * �������
 * 
 * @author Craig Lee
 * 
 */
public interface Const {

	/**
	 * ����ģʽ�������־
	 */
	public static final boolean DEV_MODE = true;

	/**
	 * �������
	 * 
	 * @author Craig Lee
	 * 
	 */
	public static final class Network {
		/**
		 * WEB��������ַ
		 */
		// public static final String WEB_SERVER_ADDRESS =
		// "http://211.87.227.10";
		/**
		 * Ӧ�÷�������ַ
		 */
		// public static final String APP_SERVER_ADDRESS = "211.87.227.10";

		/**
		 * Ӧ�÷������˿ں�
		 */
		public static final int APP_SERVER_PORT = 731;

		/**
		 * ���ӳ�ʱʱ��
		 */
		public static final int CONNECT_TIME_OUT_MILLIS = 8000;

	}

	public static final class SDCard {
		/**
		 * SD�����ͼƬ��·��
		 */
		public static final String SDCARD_DIR_PATH = File.separator
				+ "Examination_stu";
		/**
		 * ���Լ����Ĵ洢·������ַ
		 */
		public static final String JD_ANSWER_IMAGE_PATH = SDCARD_DIR_PATH
				+ File.separator + "exam";
		/**
		 * ����ֽ�Ĵ洢·������ַ
		 */
		public static final String YANSUAN_IMAGE_PATH = SDCARD_DIR_PATH
				+ File.separator + "yansuan";

	}

	public static final class Broadcast {
		/**
		 * ��¼������Ϣ
		 */
		public static final String LOGIN_RESPONSE = "com.todayedu.Welcome.login";

		/**
		 * ��̨��ȡ���µĿ��Ժ�֪ͨ�����µĿ���ѡ�����
		 */
		public static final String EXAM_CHOOSE_NEW_EXAM = "com.todayedu.ExamChoose.newExam";

		/**
		 * ����ʱ��
		 */
		public static final String PROBLEM_CHOOSE_TIME = "com.todayedu.ProblemChoose.time";
		/**
		 * ��ȡ���ɼ�
		 */
		public static final String PROBLEM_CHOOSE_SCORE = "com.todayedu.ProblemChoose.score";

	}

	/**
	 * ����
	 * 
	 * @author Craig Lee
	 * 
	 */
	public static final class Settings {
		/**
		 * ��������ַ�Ƿ�����
		 */
		public static final String IS_SERVER_ADDRESS_SET = "is_server_address_set";
		/**
		 * ��������ַ
		 */
		public static final String SERVER_ADDRESS = "server_address";
	}


}
