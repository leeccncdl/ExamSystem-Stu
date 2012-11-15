package com.todayedu.exam.student.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.ebag.net.obj.I;
import org.ebag.net.obj.exam.ExamObj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import com.todayedu.exam.student.dao.DBOpenHelper;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.utils.Util;

public class ExamDAOImpl implements ExamDAO {
	private static ExamDAO instance;

	private DBOpenHelper dbHelper = null;

	private SQLiteDatabase db = null;

	private ProblemDAO problemDAO = null;

	private ExamDAOImpl(Context context) {
		this.dbHelper = DBOpenHelper.getInstance(context);
		this.problemDAO = ProblemDAOImpl.getInstance(context);
	}

	public static ExamDAO getInstance(Context context) {
		if (instance == null)
			instance = new ExamDAOImpl(context);
		return instance;
	}

	@Override
	public boolean save(List<ExamObj> list) {
		boolean result = true;
		if (list != null) {
			for (ExamObj e : list) {
				result = save(e);
				if (!result)
					return result;
			}
		}
		return result;
	}

	@Override
	public boolean save(ExamObj e) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		// 看是否已经存在该考试
		Cursor ec = db.query(DBOpenHelper.TABLE_EXAM, null, "id = ?",
				new String[] { String.valueOf(e.id) }, null, null, null);
		if (!ec.moveToFirst()) {
			ContentValues v = new ContentValues();
			v.put("id", e.getId());
			v.put("name", e.getName());
			v.put("type", e.getType());
			v.put("time", e.getTime());
			v.put("remainTime", e.getTime());
			v.put("status", Exam.STATUS_UNFINISHED);
			if (db.insert(DBOpenHelper.TABLE_EXAM, null, v) <= 0)
				return false;
			result = problemDAO.save(e);
		}
		return result;
	}

	@Override
	public List<Exam> getFinished() {
		db = dbHelper.getReadableDatabase();
		Cursor ec = db.query(DBOpenHelper.TABLE_EXAM, null,
				"status = ? or status = ?",
				new String[] {
						String.valueOf(Exam.STATUS_FINISHED_WITH_NO_GRADE),
						String.valueOf(Exam.STATUS_FINISHED_WITH_GRADE) }, null,
				null, null);
		List<Exam> list = new ArrayList<Exam>();
		Exam e;
		while (ec.moveToNext()) {
			e = new Exam();
			e.setId(ec.getInt(ec.getColumnIndex("id")));
			e.setName(ec.getString(ec.getColumnIndex("name")));
			e.setStatus(ec.getInt(ec.getColumnIndex("status")));
			e.setType(ec.getInt(ec.getColumnIndex("type")));
			e.setTime(ec.getLong(ec.getColumnIndex("time")));
			e.setRemainTime(ec.getLong(ec.getColumnIndex("remainTime")));
			try {
				String time = ec.getString(ec.getColumnIndex("finishDate"));
				if (time != null && !"".equals(time))
					e.setFinishDate(Util.sdf.parse(time));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			e.setProblemList(problemDAO.get(ec.getInt(ec.getColumnIndex("id"))));
			e.setScore(ec.getDouble(ec.getColumnIndex("score")));
			list.add(e);
		}
		ec.close();
		return list;

	}

	@Override
	public List<Exam> getFinishedByDate(int year, int month, int day) {
		db = dbHelper.getReadableDatabase();
		Cursor ec = db.query(DBOpenHelper.TABLE_EXAM, null, "finishDate = ?",
				new String[] { year + "/" + month + "/" + day }, null, null,
				null);
		List<Exam> list = new ArrayList<Exam>();
		Exam e;
		while (ec.moveToNext()) {
			int status = ec.getInt(ec.getColumnIndex("status"));
			if (status == Exam.STATUS_FINISHED_WITH_GRADE
					|| status == Exam.STATUS_FINISHED_WITH_NO_GRADE) {
				e = new Exam();
				e.setId(ec.getInt(ec.getColumnIndex("id")));
				e.setName(ec.getString(ec.getColumnIndex("name")));
				e.setStatus(ec.getInt(ec.getColumnIndex("status")));
				e.setType(ec.getInt(ec.getColumnIndex("type")));
				e.setTime(ec.getLong(ec.getColumnIndex("time")));
				e.setRemainTime(ec.getLong(ec.getColumnIndex("remainTime")));
				try {
					String time = ec.getString(ec.getColumnIndex("finishDate"));
					if (time != null && !"".equals(time))
						e.setFinishDate(Util.sdf.parse(time));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				e.setProblemList(problemDAO.get(ec.getInt(ec
						.getColumnIndex("id"))));
				e.setScore(ec.getDouble(ec.getColumnIndex("score")));
				list.add(e);
			}
		}
		ec.close();
		return list;
	}

	@Override
	public List<Exam> getUnfinished() {
		List<Exam> list = new ArrayList<Exam>();
		Exam e = null;
		db = dbHelper.getReadableDatabase();
		Cursor ec = db.query(
				DBOpenHelper.TABLE_EXAM,
				null,
				"status = ? or status = ?",
				new String[] { String.valueOf(Exam.STATUS_UNFINISHED),
						String.valueOf(Exam.STATUS_UNDER_FINISHED) }, null, null,
				null);
		while (ec.moveToNext()) {
			e = new Exam();
			e.setId(ec.getInt(ec.getColumnIndex("id")));
			e.setName(ec.getString(ec.getColumnIndex("name")));
			e.setStatus(ec.getInt(ec.getColumnIndex("status")));
			e.setType(ec.getInt(ec.getColumnIndex("type")));
			e.setTime(ec.getLong(ec.getColumnIndex("time")));
			e.setRemainTime(ec.getLong(ec.getColumnIndex("remainTime")));
			e.setProblemList(problemDAO.get(ec.getInt(ec.getColumnIndex("id"))));
			list.add(e);
		}
		ec.close();
		return list;
	}

	@Override
	public Exam get(int examid) {
		Exam e = null;
		db = dbHelper.getReadableDatabase();
		Cursor ec = db.query(DBOpenHelper.TABLE_EXAM, null, "id = ?",
				new String[] { String.valueOf(examid) }, null, null, null);
		if (ec.moveToNext()) {
			e = new Exam();
			e.setId(ec.getInt(ec.getColumnIndex("id")));
			e.setName(ec.getString(ec.getColumnIndex("name")));
			e.setStatus(ec.getInt(ec.getColumnIndex("status")));
			e.setType(ec.getInt(ec.getColumnIndex("type")));
			e.setTime(ec.getLong(ec.getColumnIndex("time")));
			e.setRemainTime(ec.getLong(ec.getColumnIndex("remainTime")));

			if (e.getStatus() == Exam.STATUS_FINISHED_WITH_GRADE
					|| e.getStatus() == Exam.STATUS_FINISHED_WITH_NO_GRADE) {
				try {
					String time = ec.getString(ec.getColumnIndex("finishDate"));
					if (time != null && !"".equals(time))
						e.setFinishDate(Util.sdf.parse(time));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			e.setScore(ec.getDouble(ec.getColumnIndex("score")));
			e.setProblemList(problemDAO.get(ec.getInt(ec.getColumnIndex("id"))));
		}
		ec.close();
		return e;
	}

	@Override
	public boolean updateRemainTime(int examid, long remainTime) {
		db = dbHelper.getWritableDatabase();
		boolean result = true;
		ContentValues v = new ContentValues();
		v.put("remainTime", remainTime);
		if (db.update(DBOpenHelper.TABLE_EXAM, v, "id = ?",
				new String[] { String.valueOf(examid) }) <= 0)
			result = false;
		return result;
	}

	@Override
	public boolean updateStatus(int examid, int status) {
		db = dbHelper.getWritableDatabase();
		boolean result = true;
		ContentValues v = new ContentValues();
		v.put("status", status);
		if (status == Exam.STATUS_FINISHED_WITH_NO_GRADE) {
			Time time = new Time("GMT+8");// 设置时间为第八时区标准时间
			time.setToNow();// 设置为系统当前时间
			v.put("finishDate", time.year + "/" + (time.month + 1) + "/"
					+ time.monthDay);
		}
		if (db.update(DBOpenHelper.TABLE_EXAM, v, "id = ?",
				new String[] { String.valueOf(examid) }) <= 0)
			result = false;
		return result;

	}

	@Override
	public int getStatus(int examid) {
		db = dbHelper.getReadableDatabase();
		int result = -1;

		Cursor ec = db.query(DBOpenHelper.TABLE_EXAM, null, "id = ?",
				new String[] { String.valueOf(examid) }, null, null, null);
		if (ec.moveToNext()) {
			result = ec.getInt(ec.getColumnIndex("status"));
		}
		ec.close();
		return result;
	}

	@Override
	public boolean isAllScored(int examId) {
		db = dbHelper.getReadableDatabase();
		boolean result = true;
		Cursor ec = db.query(
				DBOpenHelper.TABLE_PROBLEM,
				null,
				"examId = ? and status = ?",
				new String[] { String.valueOf(examId),
						String.valueOf(I.choice.answerState_waitMark) }, null,
				null, null);// 查询出本次考试还没有评分的题目
		if (ec.moveToFirst())
			result = false;
		ec.close();
		return result;
	}

	@Override
	public boolean calculateTotalScore(int examId) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		Cursor ec = db.query(DBOpenHelper.TABLE_PROBLEM, null, "examId = ?",
				new String[] { String.valueOf(examId) }, null, null, null);
		double sum = 0d;
		while (ec.moveToNext()) {
			sum += ec.getDouble(ec.getColumnIndex("score"));
		}
		ec.close();
		ContentValues v = new ContentValues();
		v.put("score", sum);
		if (db.update(DBOpenHelper.TABLE_EXAM, v, "id = ?",
				new String[] { String.valueOf(examId) }) <= 0)
			result = false;
		return result;
	}

}
