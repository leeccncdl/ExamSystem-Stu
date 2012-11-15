package com.todayedu.exam.student.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.todayedu.exam.student.App;
import com.todayedu.exam.student.BaseActivity;
import com.todayedu.exam.student.ProblemChoose;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.utils.Util;

/**
 * 生成已经考过的试的view
 * 
 * @author Craig Lee
 * 
 */
public class ExamFinishedViewCreator {

	private View view;
	private BaseActivity context;
	private ViewGroup viewGroup;
	private ListView lv_examList;
	private RadioGroup rg_filter;
	private RadioButton rb_all;
	private RadioButton rb_selectByDate;
	private TextView tv_date;
	private DatePickerDialog datePicker;

	private ExamDAO examDAO;
	private List<Exam> examlist;

	public ExamFinishedViewCreator(BaseActivity context, ViewGroup vg) {
		this.context = context;
		this.examDAO = ExamDAOImpl.getInstance(context);
		this.viewGroup = vg;
		init();
		// 列出所有本地保存的考试信息
		listAllExams();
	}

	private void init() {
		view = View.inflate(context, R.layout.examlistfinished, viewGroup);
		rg_filter = (RadioGroup) findViewById(R.id.ExamListFinished_rg_filter);
		rb_all = (RadioButton) findViewById(R.id.ExamListFinished_rb_all);
		rb_selectByDate = (RadioButton) findViewById(R.id.ExamListFinished_rb_selectByDate);
		tv_date = (TextView) findViewById(R.id.ExamListFinished_tv_date);
		lv_examList = (ListView) findViewById(R.id.ExamListFinished_lv_examList);

		rg_filter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb_all.getId()) {
					tv_date.setText("");
					listAllExams();
				} else if (checkedId == rb_selectByDate.getId()) {
					showDatePicker();
				}
			}

		});
		rb_selectByDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePicker();
			}
		});
	}

	/**
	 * 根据日期列出考试列表
	 */
	private void listExamByDate(int year, int month, int day) {
		this.examlist = examDAO.getFinishedByDate(year, month, day);
		addToExamListView();
	}

	/**
	 * 列出所有本地保存的考试信息
	 */
	private void listAllExams() {
		this.examlist = examDAO.getFinished();
		addToExamListView();
	}

	/**
	 * 添加到考试列表中
	 */
	private void addToExamListView() {
		lv_examList.setAdapter(null);
		if (examlist.size() > 0) {
			List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			for (Exam e : examlist) {
				map = new HashMap<String, String>();
				map.put("name", e.getName());
				map.put("type", Exam.parseType(e.getType()));
				map.put("date", Util.sdf.format(e.getFinishDate()));
				data.add(map);
			}

			SimpleAdapter sa = new SimpleAdapter(context, data,
					R.layout.examitemfinished, new String[] { "name", "type",
							"date" }, new int[] {
							R.id.ExamItemFinished_tv_name,
							R.id.ExamItemFinished_tv_type,
							R.id.ExamItemFinished_tv_date });
			lv_examList.setAdapter(sa);

			lv_examList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// 查看一次考过的考试
					App.exam = examlist.get(position);
					App.examMode = App.EXAM_MODE_FINISHED;
					Intent intent = new Intent(context, ProblemChoose.class);
					context.startActivity(intent);
				}
			});

		} else {
			context.showShortToast(R.string.common_noData);
		}
	}

	// 显示日期选择对话框
	private void showDatePicker() {
		Calendar cal = Calendar.getInstance();
		if (datePicker == null) {
			datePicker = new DatePickerDialog(context, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					listExamByDate(year, (monthOfYear + 1), dayOfMonth);
					tv_date.setText(year + "-" + (monthOfYear + 1) + "-"
							+ dayOfMonth);
				}
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));
		}

		datePicker.show();
	}

	private View findViewById(int id) {
		return this.view.findViewById(id);
	}

	public View getView() {
		return view;
	}

}
