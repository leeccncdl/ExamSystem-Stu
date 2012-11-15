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
 * ������
 * 
 * @author Craig Lee
 * 
 */
public class Util {

	/**
	 * ���ڸ�ʽ��
	 */
	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy/MM/dd");

	/**
	 * ������ʱ��ת���ɷ�����
	 * 
	 * @param time
	 * @return
	 */
	public static String parseExamTime(long time) {
		int minute = (int) (time / 60000);
		if (minute == 0)
			return "����1����,���������棡";
		return minute + "����";
	}

	/**
	 * ��ȡ��Ŀ������ݵ�URL
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
	 * ��ȡ��ʦ�ľ�����ͼƬ
	 * 
	 * @param picUrl
	 * @return
	 */
	public static String getMarkedAnswerUrl(String picUrl) {
		return "http://" + I.tupload.mina_server_site
				+ ":8080/ServerOfEbag/getpic.jsp?url=" + picUrl;
	}

	/**
	 * �ύ�Ծ�
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
				// ѡ����
				answerObj.textAnswer = problem.getGivenAnswer() == null ? ""
						: problem.getGivenAnswer();
				// ����ѡ�������
				final double xzScore = answerObj.textAnswer.equals(problem
						.getAnswer()) ? problem.getPoint() : 0;
				problemDAO.saveScore(problem.getId(), problem.getExamId(),
						xzScore);
				// ��Ϊ�Ѿ��гɼ�������״̬Ϊ�ȴ�����
				problemDAO.updateStatus(problem.getId(), problem.getExamId(),
						I.choice.answerState_waitComment);
				answerObj.point = xzScore;

			} else if (problem.getType() == I.choice.problemType_jd) {
				// �����
				String picPath = problem.getGivenAnswer();
				if (picPath == null || "".equals(picPath)) {
					// ����û������
					answerObj.picAnswerUrl = "";
				} else {
					File picFile = new File(picPath);
					ftpClient.upload(picFile);
					answerObj.picAnswerUrl = picFile.getName();
				}
				problemDAO.updateStatus(problem.getId(), problem.getExamId(),
						I.choice.answerState_waitMark);// ����״̬Ϊ�ȴ�����
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
	 * �õ�һ��URL����Ӧ״̬
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
	 * ����webview�ķ��ʳ���ҳ��
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
					// ���뱾��assets�ļ�������Ĵ�����ʾҳ��404.html
					view.loadUrl("file:///android_asset/404.html");
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
	}

}
