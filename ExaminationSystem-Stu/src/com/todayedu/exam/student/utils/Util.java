package com.todayedu.exam.student.utils;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ebag.net.obj.I;
import org.ebag.net.obj.answer.AnswerObj;
import org.ebag.net.request.AnswerUpload;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.dao.impl.ProblemDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.model.Problem;
import com.todayedu.exam.student.service.NetService;

/**
 * 工具类
 * 
 * @author Craig Lee
 * 
 */
public class Util {

	/**
	 * 日期格式化
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy/MM/dd");

	/**
	 * 将考试时间转换成分钟数
	 * 
	 * @param time
	 * @return
	 */
	public static String parseExamTime(long time) {
		int minute = (int) (time / 60000);
		if (minute == 0)
			return "不足1分钟,请立即保存！";
		return minute + "分钟";
	}

	/**
	 * 获取题目相关内容的URL
	 * 
	 * @param pid
	 * @param type
	 * @return
	 */
	public static String getRequestUrl(int pid, String type) {
		return "http://" + I.tupload.mina_server_site
				+ ":8080/ServerOfEbag/index.jsp?pid=" + pid + "&type=" + type;
	}

	/**
	 * 获取老师阅卷的相关图片
	 * 
	 * @param picUrl
	 * @return
	 */
	public static String getMarkedAnswerUrl(String picUrl) {
		return "http://" + I.tupload.mina_server_site
				+ ":8080/ServerOfEbag/getpic.jsp?url=" + picUrl;
	}

	/**
	 * 提交试卷
	 * 
	 * @param context
	 * @param examId
	 */
	public static void submitExam(BaseActivity context, int examId)
			throws Throwable {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(I.tupload.ftp_site, I.tupload.ftp_port);
		ftpClient.login("picup", I.tupload.ftp_pwd);

		ProblemDAO problemDAO = ProblemDAOImpl.getInstance(context);
		Exam exam = ExamDAOImpl.getInstance(context).get(examId);
		List<Integer> problemList = exam.getProblemList();
		AnswerUpload answerUpload = new AnswerUpload();
		ArrayList<AnswerObj> ansList = new ArrayList<AnswerObj>();

		Problem problem = null;
		AnswerObj answerObj = null;
		for (Integer pid : problemList) {
			problem = problemDAO.get(pid, examId);
			answerObj = new AnswerObj();
			answerObj.uid = App.uid;
			answerObj.problemId = pid;
			if (problem.getType() == I.choice.problemType_xz) {
				// 选择题
				answerObj.textAnswer = problem.getGivenAnswer() == null ? ""
						: problem.getGivenAnswer();
				// 批阅选择题分数
				final double xzScore = answerObj.textAnswer.equals(problem
						.getAnswer()) ? problem.getPoint() : 0;
				problemDAO.saveScore(problem.getId(), problem.getExamId(),
						xzScore);
				// 因为已经有成绩，更改状态为等待讲评
				problemDAO.updateStatus(problem.getId(), problem.getExamId(),
						I.choice.answerState_waitComment);
				answerObj.point = xzScore;

			} else if (problem.getType() == I.choice.problemType_jd) {
				// 简答题
				String picPath = problem.getGivenAnswer();
				if (picPath == null || "".equals(picPath)) {
					// 该题没有作答
					answerObj.picAnswerUrl = "";
				} else {
					File picFile = new File(picPath);
					ftpClient.upload(picFile);
					answerObj.picAnswerUrl = picFile.getName();
				}
				problemDAO.updateStatus(problem.getId(), problem.getExamId(),
						I.choice.answerState_waitMark);// 更改状态为等待批阅
			}
			ansList.add(answerObj);
		}
		answerUpload.ansList = ansList;
		Intent intent = new Intent(context, NetService.class);
		intent.putExtra(NetService.EXTRA_DATA_SERVICE_TYPE, NetService.SERVICE_CHOICE_SEND_OBJECT);
		intent.putExtra(NetService.EXTRA_DATA_OBJECT_TO_BE_SENT, answerUpload);
		context.startService(intent);
	}

	/**
	 * 得到一个URL的响应状态
	 * 
	 * @param url
	 * @return
	 */
	public static int getRespStatus(String url) {
		int status = -1;
		try {
			HttpHead head = new HttpHead(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(head);
			status = resp.getStatusLine().getStatusCode();
		} catch (IOException e) {
		}
		return status;
	}

	/**
	 * 重置webview的访问出错页面
	 * 
	 * @param web
	 */
	public static void resetWebView404Page(WebView web) {
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("resp status:" + getRespStatus(url));
				if (url.startsWith("http://") && getRespStatus(url) == 404) {
					view.stopLoading();
					// 载入本地assets文件夹下面的错误提示页面404.html
					view.loadUrl("file:///android_asset/404.html");
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
	}

}
