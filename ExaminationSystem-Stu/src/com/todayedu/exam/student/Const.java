package com.todayedu.exam.student;

import java.io.File;

/**
 * 软件常量
 * 
 * @author Craig Lee
 * 
 */
public interface Const {

	/**
	 * 开发模式，输出日志
	 */
	public static final boolean DEV_MODE = true;

	/**
	 * 网络参数
	 * 
	 * @author Craig Lee
	 * 
	 */
	public static final class Network {
		/**
		 * WEB服务器地址
		 */
		// public static final String WEB_SERVER_ADDRESS =
		// "http://211.87.227.10";
		/**
		 * 应用服务器地址
		 */
		// public static final String APP_SERVER_ADDRESS = "211.87.227.10";

		/**
		 * 应用服务器端口号
		 */
		public static final int APP_SERVER_PORT = 731;

		/**
		 * 连接超时时间
		 */
		public static final int CONNECT_TIME_OUT_MILLIS = 8000;

	}

	public static final class SDCard {
		/**
		 * SD卡存放图片根路径
		 */
		public static final String SDCARD_DIR_PATH = File.separator
				+ "Examination_stu";
		/**
		 * 考试简答题的存储路径基地址
		 */
		public static final String JD_ANSWER_IMAGE_PATH = SDCARD_DIR_PATH
				+ File.separator + "exam";
		/**
		 * 演算纸的存储路径基地址
		 */
		public static final String YANSUAN_IMAGE_PATH = SDCARD_DIR_PATH
				+ File.separator + "yansuan";

	}

	public static final class Broadcast {
		/**
		 * 登录返回信息
		 */
		public static final String LOGIN_RESPONSE = "com.todayedu.Welcome.login";

		/**
		 * 后台获取到新的考试后通知更新新的考试选择界面
		 */
		public static final String EXAM_CHOOSE_NEW_EXAM = "com.todayedu.ExamChoose.newExam";

		/**
		 * 考试时间
		 */
		public static final String PROBLEM_CHOOSE_TIME = "com.todayedu.ProblemChoose.time";
		/**
		 * 获取到成绩
		 */
		public static final String PROBLEM_CHOOSE_SCORE = "com.todayedu.ProblemChoose.score";

	}

	/**
	 * 设置
	 * 
	 * @author Craig Lee
	 * 
	 */
	public static final class Settings {
		/**
		 * 服务器地址是否设置
		 */
		public static final String IS_SERVER_ADDRESS_SET = "is_server_address_set";
		/**
		 * 服务器地址
		 */
		public static final String SERVER_ADDRESS = "server_address";
	}


}
