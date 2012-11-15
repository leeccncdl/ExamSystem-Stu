package com.todayedu.exam.student.view;

import java.util.List;

import org.ebag.net.obj.I;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.YansuanBoard;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.dao.impl.ProblemDAOImpl;
import com.todayedu.exam.student.utils.Util;

/**
 * �����Ѿ���ɿ��ԵĴ������View
 * 
 * @author Craig Lee
 * 
 */
public class ProblemShowFinishedViewCreator {
	// ����ؼ�
	private View view;
	private BaseActivity context;
	private ViewGroup viewGroup;

	private Button btn_backProblem;
	private Button btn_problemList;
	private Button btn_forwardProblem;
	private Button btn_yansuanBoard;

	private TextView tv_problemNum;
	private TextView tv_problemPoint;
	private TextView tv_problemScore;

	private WebView wv_problemContent;
	private LinearLayout ll_xz;
	private TextView tv_rightAnswer;
	private TextView tv_yourAnswer;

	private LinearLayout ll_jd;
	private WebView wv_markedAnswer;

	private WebView wv_problemAnswer;
	private WebView wv_problemAnalysis;

	// ������Ʊ���
	private ProblemDAO problemDAO;

	public ProblemShowFinishedViewCreator(BaseActivity context,
			ViewGroup viewGroup) {
		this.context = context;
		this.viewGroup = viewGroup;
		init();
	}

	private void init() {
		problemDAO = ProblemDAOImpl.getInstance(context);
		this.view = View.inflate(context, R.layout.problemshowfinished,
				viewGroup);
		btn_backProblem = (Button) findViewById(R.id.ProblemShowFinished_btn_backProblem);
		btn_problemList = (Button) findViewById(R.id.ProblemShowFinished_btn_problemList);
		btn_forwardProblem = (Button) findViewById(R.id.ProblemShowFinished_btn_forwardProblem);
		btn_yansuanBoard = (Button) findViewById(R.id.ProblemShowFinished_btn_yansuanBoard);

		tv_problemNum = (TextView) findViewById(R.id.ProblemShowFinished_tv_problemNum);
		tv_problemPoint = (TextView) findViewById(R.id.ProblemShowFinished_tv_problemPoint);
		tv_problemScore = (TextView) findViewById(R.id.ProblemShowFinished_tv_problemScore);

		wv_problemContent = (WebView) findViewById(R.id.ProblemShow_wv_problemContent);

		ll_xz = (LinearLayout) findViewById(R.id.ProblemShowFinished_ll_xz);
		tv_rightAnswer = (TextView) findViewById(R.id.ProblemShowFinished_tv_rightAnswer);
		tv_yourAnswer = (TextView) findViewById(R.id.ProblemShowFinished_tv_yourAnswer);

		ll_jd = (LinearLayout) findViewById(R.id.ProblemShowFinished_ll_jd);
		wv_markedAnswer = (WebView) findViewById(R.id.ProblemShowFinished_wv_markedAnswer);
		Util.resetWebView404Page(wv_markedAnswer);

		wv_problemAnswer = (WebView) findViewById(R.id.ProblemShow_wv_problemAnswer);
		wv_problemAnalysis = (WebView) findViewById(R.id.ProblemShow_wv_problemAnalysis);

		wv_problemContent.getSettings().setBuiltInZoomControls(true);
		wv_problemContent.getSettings().setSupportZoom(true);
		wv_problemContent.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		wv_problemContent.setInitialScale(100);
		Util.resetWebView404Page(wv_problemContent);
		wv_problemAnswer.getSettings().setBuiltInZoomControls(true);
		wv_problemAnswer.getSettings().setSupportZoom(true);
		wv_problemAnswer.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		wv_problemAnswer.setInitialScale(100);
		Util.resetWebView404Page(wv_problemAnswer);
		wv_problemAnalysis.getSettings().setBuiltInZoomControls(true);
		wv_problemAnalysis.getSettings().setSupportZoom(true);
		wv_problemAnalysis.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		wv_problemAnalysis.setInitialScale(100);
		Util.resetWebView404Page(wv_problemAnalysis);

		// �󶨼���
		btn_backProblem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				App.problemPosition--;
				judgeProblemPosition();
				// ������Ŀ
				loadProblem();
			}
		});
		btn_backProblem.setEnabled(false);
		btn_problemList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.finish();
			}
		});
		btn_forwardProblem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				App.problemPosition++;
				judgeProblemPosition();
				// ������Ŀ
				loadProblem();
			}
		});
		btn_yansuanBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���������
				context.startActivity(new Intent(context, YansuanBoard.class));
			}
		});
		judgeProblemPosition();
		loadProblem();
	}

	// ������Ŀ
	private void loadProblem() {
		App.problem = problemDAO.get(
				App.exam.getProblemList().get(App.problemPosition),
				App.exam.getId());
		tv_problemNum.setText("�� " + (App.problemPosition + 1) + " ��");
		tv_problemPoint.setText(" (" + App.problem.getPoint() + "�� ) ");
		int status = App.problem.getStatus();
		if (status == I.choice.answerState_waitMark) {
			tv_problemScore.setText("�ȴ�����");
		} else if (status == I.choice.answerState_waitComment) {
			tv_problemScore.setText(String.valueOf(App.problem.getScore()));
		}

		// ������Ŀ����
		String contentUrl = Util.getRequestUrl(App.problem.getId(),
				I.url.problem);
		wv_problemContent.loadUrl(contentUrl);

		// ������Ŀ����
		String analysisUrl = Util.getRequestUrl(App.problem.getId(),
				I.url.analysis);
		wv_problemAnalysis.loadUrl(analysisUrl);

		// ���ý���ģʽ
		if (App.problem.getType() == I.choice.problemType_xz) {
			// ѡ�������
			xzProblem();
		} else if (App.problem.getType() == I.choice.problemType_jd) {
			// ��������
			jdProblem();

		} else {
			context.showLongToast("�����ڲ����ݴ������");
			context.finish();
		}
	}

	private void xzProblem() {
		// ѡ�������
		ll_jd.setVisibility(View.GONE);
		ll_xz.setVisibility(View.VISIBLE);
		tv_rightAnswer.setText(App.problem.getAnswer());
		tv_yourAnswer.setText(App.problem.getGivenAnswer());

	}

	private void jdProblem() {
		// ��������
		ll_jd.setVisibility(View.VISIBLE);
		ll_xz.setVisibility(View.GONE);

		// ���������
		String answerUrl = Util.getRequestUrl(App.problem.getId(), I.url.ans);
		wv_problemAnswer.loadUrl(answerUrl);
		// ����ѧ���Ĵ�
		String markedAnswer = App.problem.getMarkedAnswer();
		if (!("".equals(markedAnswer) || markedAnswer == null)) {
			String markedAnswerUrl = Util.getMarkedAnswerUrl(markedAnswer);
			wv_markedAnswer.loadUrl(markedAnswerUrl);
		}

	}

	// �ж������ⰴť״̬
	private void judgeProblemPosition() {
		List<Integer> list = App.exam.getProblemList();
		if (App.problemPosition == list.size() - 1) {
			btn_forwardProblem.setEnabled(false);
			context.showShortToast("�Ѿ������һ����~");
		} else {
			btn_forwardProblem.setEnabled(true);
		}

		if (App.problemPosition == 0) {
			btn_backProblem.setEnabled(false);
			context.showShortToast("�Ѿ�����һ����~");
		} else {
			btn_backProblem.setEnabled(true);
		}
	}

	private View findViewById(int id) {
		return this.view.findViewById(id);
	}

	public View getView() {
		return view;
	}
}
