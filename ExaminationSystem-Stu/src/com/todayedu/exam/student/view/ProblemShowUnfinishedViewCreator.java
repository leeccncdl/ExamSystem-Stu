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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.YansuanBoard;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.dao.impl.ProblemDAOImpl;
import com.todayedu.exam.student.paintpad.PaintPadViewCreator;
import com.todayedu.exam.student.utils.Util;

/**
 * 创建未完成考试的答题界面View
 * 
 * @author Craig Lee
 * 
 */
public class ProblemShowUnfinishedViewCreator {
	private View view;
	private BaseActivity context;
	private ViewGroup viewGroup;

	// 界面控件
	private Button btn_backProblem;
	private Button btn_problemList;
	private Button btn_forwardProblem;
	private Button btn_yansuanBoard;
	private Button btn_loadPaintPad;
	private Button btn_up;
	private Button btn_down;

	private TextView tv_timeRemain;
	private TextView tv_problemNum;
	private TextView tv_problemPoint;

	private WebView wv_problemContent;
	private WebView wv_problemHint;

	private LinearLayout ll_xz;
	private RadioGroup rg_choice;
	private RadioButton rb_A;
	private RadioButton rb_B;
	private RadioButton rb_C;
	private RadioButton rb_D;

	private RelativeLayout rl_jd;
	private RelativeLayout rl_paintPad;
	private MyScrollView sv_scrollView;

	// 界面控制变量
	private ProblemDAO problemDAO;

	private PaintPadViewCreator paintPadViewCreator;

	public ProblemShowUnfinishedViewCreator(BaseActivity context,
			ViewGroup viewGroup) {
		this.context = context;
		this.viewGroup = viewGroup;
		init();

	}

	private void init() {
		problemDAO = ProblemDAOImpl.getInstance(context);
		this.view = View.inflate(context, R.layout.problemshowunfinished,
				viewGroup);
		btn_backProblem = (Button) findViewById(R.id.ProblemShowUnfinished_btn_backProblem);
		btn_problemList = (Button) findViewById(R.id.ProblemShowUnfinished_btn_problemList);
		btn_forwardProblem = (Button) findViewById(R.id.ProblemShowUnfinished_btn_forwardProblem);

		tv_timeRemain = (TextView) findViewById(R.id.ProblemShowTitleBar_tv_timeRemain);

		btn_yansuanBoard = (Button) findViewById(R.id.ProblemShowUnfinished_btn_yansuanBoard);
		tv_problemNum = (TextView) findViewById(R.id.ProblemShowUnfinished_tv_problemNum);
		tv_problemPoint = (TextView) findViewById(R.id.ProblemShowUnfinished_tv_problemPoint);

		wv_problemContent = (WebView) findViewById(R.id.ProblemShow_wv_problemContent);
		wv_problemHint = (WebView) findViewById(R.id.ProblemShow_wv_problemHint);
		wv_problemContent.getSettings().setBuiltInZoomControls(true);
		wv_problemContent.getSettings().setSupportZoom(true);
		wv_problemContent.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		Util.resetWebView404Page(wv_problemContent);
		wv_problemHint.getSettings().setBuiltInZoomControls(true);
		wv_problemHint.getSettings().setSupportZoom(true);
		wv_problemHint.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		Util.resetWebView404Page(wv_problemHint);

		ll_xz = (LinearLayout) findViewById(R.id.ProblemShowUnfinished_ll_xz);
		rg_choice = (RadioGroup) findViewById(R.id.ProblemShowUnfinished_rg_choice);
		rb_A = (RadioButton) findViewById(R.id.ProblemShowUnfinished_rb_A);
		rb_B = (RadioButton) findViewById(R.id.ProblemShowUnfinished_rb_B);
		rb_C = (RadioButton) findViewById(R.id.ProblemShowUnfinished_rb_C);
		rb_D = (RadioButton) findViewById(R.id.ProblemShowUnfinished_rb_D);

		rl_jd = (RelativeLayout) findViewById(R.id.ProblemShowUnfinished_rl_jd);
		rl_paintPad = (RelativeLayout) findViewById(R.id.ProblemShowUnfinished_rl_paintPad);
		rl_paintPad.setVisibility(View.INVISIBLE);

		btn_loadPaintPad = (Button) findViewById(R.id.ProblemShowUnfinished_btn_loadPaintPad);

		// 绑定监听器
		btn_backProblem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 保存答案
				App.problemPosition--;
				judgeProblemPosition();
				// 载入题目
				loadProblem();

			}
		});
		btn_problemList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				context.finish();
			}
		});
		btn_forwardProblem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存答案
				App.problemPosition++;
				judgeProblemPosition();
				// 载入题目
				loadProblem();
			}
		});

		btn_yansuanBoard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 启动演算板
				Intent intent = new Intent(context, YansuanBoard.class);
				context.startActivity(intent);
			}
		});

		btn_loadPaintPad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 载入答题板
				rl_paintPad.setVisibility(View.VISIBLE);
				paintPadViewCreator.reset(App.exam.getId(), App.problem.getId());
				paintPadViewCreator.loadExsitedImage();
				btn_loadPaintPad.setVisibility(View.GONE);
			}
		});

		rg_choice
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == rb_A.getId())
							problemDAO.updateGivenAnswer(App.problem.getId(),
									App.exam.getId(), "A");
						else if (checkedId == rb_B.getId())
							problemDAO.updateGivenAnswer(App.problem.getId(),
									App.exam.getId(), "B");
						else if (checkedId == rb_C.getId())
							problemDAO.updateGivenAnswer(App.problem.getId(),
									App.exam.getId(), "C");
						else if (checkedId == rb_D.getId())
							problemDAO.updateGivenAnswer(App.problem.getId(),
									App.exam.getId(), "D");
					}
				});

		sv_scrollView = (MyScrollView) findViewById(R.id.ProblemShowUnfinished_sv_scrollView);
		btn_up = (Button) findViewById(R.id.ProblemShowUnfinished_btn_up);
		btn_down = (Button) findViewById(R.id.ProblemShowUnfinished_btn_down);
		btn_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sv_scrollView.scrollBy(0, -150);

			}
		});
		btn_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sv_scrollView.scrollBy(0, 150);
			}
		});

		App.problem = problemDAO.get(
				App.exam.getProblemList().get(App.problemPosition),
				App.exam.getId());
		this.paintPadViewCreator = new PaintPadViewCreator(context, null,
				App.exam.getId(), App.problem.getId(), true);
		rl_paintPad.addView(paintPadViewCreator.getView());
		judgeProblemPosition();
		loadProblem();

	}

	// 载入题目
	private void loadProblem() {
		App.problem = problemDAO.get(
				App.exam.getProblemList().get(App.problemPosition),
				App.exam.getId());
		tv_problemNum.setText("第 " + (App.problemPosition + 1) + " 题");
		tv_problemPoint.setText(" (" + App.problem.getPoint() + "分 ) ");
		// 载入题目内容
		String contentUrl = Util.getRequestUrl(App.problem.getId(),
				I.url.problem);
		wv_problemContent.loadUrl(contentUrl);

		// 载入题目提示
		String hintUrl = Util.getRequestUrl(App.problem.getId(), I.url.hint);
		wv_problemHint.loadUrl(hintUrl);

		// 设置界面模式
		if (App.problem.getType() == I.choice.problemType_xz) {
			// 选择题界面
			rl_jd.setVisibility(View.GONE);
			ll_xz.setVisibility(View.VISIBLE);
			// 重新设置选择状态
			rg_choice.clearCheck();
			String givenAnswer = App.problem.getGivenAnswer();
			if (givenAnswer != null && !"".equals(givenAnswer)) {
				// 已经作答过了
				if ("A".equals(givenAnswer))
					rg_choice.check(rb_A.getId());
				else if ("B".equals(givenAnswer))
					rg_choice.check(rb_B.getId());
				else if ("C".equals(givenAnswer))
					rg_choice.check(rb_C.getId());
				else if ("D".equals(givenAnswer))
					rg_choice.check(rb_D.getId());
			}

		} else if (App.problem.getType() == I.choice.problemType_jd) {
			// 简答题界面
			rl_jd.setVisibility(View.VISIBLE);
			ll_xz.setVisibility(View.GONE);
			if (btn_loadPaintPad.getVisibility() == View.GONE)
				this.paintPadViewCreator.reset(App.exam.getId(),
						App.problem.getId());
		} else {
			context.showLongToast("程序内部数据传输错误！");
			context.finish();
		}
	}

	// 判断上一题和下一题按钮的状态
	private void judgeProblemPosition() {
		List<Integer> list = App.exam.getProblemList();
		if (App.problemPosition == list.size() - 1) {
			btn_forwardProblem.setEnabled(false);
			context.showShortToast("已经到最后一题啦~");
		} else {
			btn_forwardProblem.setEnabled(true);
		}

		if (App.problemPosition == 0) {
			btn_backProblem.setEnabled(false);
			context.showShortToast("已经到第一题啦~");
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

	public TextView getTv_timeRemain() {
		return tv_timeRemain;
	}

}
