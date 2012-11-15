package com.todayedu.exam.student.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ebag.net.request.ExamRequet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.ProblemChoose;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.service.NetService;
import com.todayedu.exam.student.utils.Util;

/**
 * ����δ�������Ե�view
 * 
 * @author Craig Lee
 * 
 */
public class ExamUnfinishedViewCreator {
	private View view;
	private BaseActivity context;
	private ViewGroup viewGroup;

	private ExamDAO examDAO;
	// ����ˢ��ListView
	private PullToRefreshListView ptrlv_examList;
	// ������ListView
	private ListView lv_examList;
	private List<HashMap<String, String>> examListAdapterData;
	private SimpleAdapter examListAdapter;

	private List<Exam> examlist;

	public ExamUnfinishedViewCreator(BaseActivity context, ViewGroup viewGroup) {
		this.context = context;
		this.viewGroup = viewGroup;
		this.examDAO = ExamDAOImpl.getInstance(context);
		init();
	}

	private void init() {
		view = View.inflate(context, R.layout.examlistunfinished, viewGroup);
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		// ������ˢ�µ�ListView
		ptrlv_examList = (PullToRefreshListView) view
				.findViewById(R.id.ExamListUnfinished_ptrlv_examList);
		lv_examList = ptrlv_examList.getRefreshableView();

		// ���ò���
		ptrlv_examList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// ����ˢ�¶���
				// ����ʱ��
				ptrlv_examList.setLastUpdatedLabel(DateUtils.formatDateTime(
						context, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL));
				// �����µĿ���
				requestNewExam();
			}
		});
		// ��ʼ��ListView����
		examListAdapterData = new ArrayList<HashMap<String, String>>();
		this.examlist = examDAO.getUnfinished();
		HashMap<String, String> map;
		for (Exam e : examlist) {
			map = new HashMap<String, String>();
			map.put("name", e.getName());
			map.put("type", Exam.parseType(e.getType()));
			map.put("time", Util.parseExamTime(e.getTime()));
			examListAdapterData.add(map);
		}

		examListAdapter = new SimpleAdapter(context, examListAdapterData,
				R.layout.examitemunfinished, new String[] { "name", "type",
						"time" }, new int[] { R.id.ExamItemUnFinished_tv_name,
						R.id.ExamItemUnFinished_tv_type,
						R.id.ExamItemUnFinished_tv_time });
		lv_examList.setAdapter(examListAdapter);
		lv_examList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ���б��ο���
				Exam exam = examlist.get(position - 1);
				confirmExam(exam);
			}
		});

	}

	// ��ѯ�µĿ���
	private void requestNewExam() {
		Intent intent = new Intent(context, NetService.class);
		intent.putExtra(NetService.EXTRA_DATA_SERVICE_TYPE,
				NetService.SERVICE_CHOICE_SEND_OBJECT);
		ExamRequet examRequest = new ExamRequet();
		examRequest.uid = App.uid;
		examRequest.isTeacher = false;
		intent.putExtra(NetService.EXTRA_DATA_OBJECT_TO_BE_SENT, examRequest);
		context.startService(intent);
	}

	/**
	 * ������ϣ������µĿ����б�
	 */
	public void loadNewExam() {
		this.examlist = examDAO.getUnfinished();
		if (examlist.size() > 0) {
			examListAdapterData.clear();
			HashMap<String, String> map;
			for (Exam e : examlist) {
				map = new HashMap<String, String>();
				map.put("name", e.getName());
				map.put("type", Exam.parseType(e.getType()));
				map.put("time", Util.parseExamTime(e.getTime()));
				examListAdapterData.add(map);
			}
			// ֪ͨ�����б�
			examListAdapter.notifyDataSetChanged();
		} else {
			context.showShortToast(R.string.common_noData);
		}
		// ���ˢ��
		if (ptrlv_examList.isRefreshing())
			ptrlv_examList.onRefreshComplete();

	}

	// ȷ�Ͻ��п���
	private void confirmExam(final Exam exam) {
		new AlertDialog.Builder(context)
				.setTitle("����ȷ��")
				.setMessage("�������ƣ�" + exam.getName())
				.setPositiveButton(
						getResources().getString(R.string.common_confirm),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										ProblemChoose.class);
								App.exam = exam;
								App.examMode = App.EXAM_MODE_UNFINISHED;
								context.startActivity(intent);
							}
						})
				.setNegativeButton(
						getResources().getString(R.string.common_cancle),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						})

				.show();
	}

	private Resources getResources() {
		return this.context.getResources();
	}

	public View getView() {
		return view;
	}

}
