package com.todayedu.exam.student.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.ebag.net.obj.I;
import org.ebag.net.obj.exam.ExamObj;
import org.ebag.net.obj.exam.ProblemInfoObj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.todayedu.exam.student.dao.DBOpenHelper;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.model.Problem;

public class ProblemDAOImpl implements ProblemDAO {
	private static ProblemDAO instance;

	private DBOpenHelper dbHelper = null;

	private SQLiteDatabase db = null;

	public static ProblemDAO getInstance(Context context) {
		if (instance == null)
			instance = new ProblemDAOImpl(context);
		return instance;
	}

	private ProblemDAOImpl(Context context) {
		this.dbHelper = DBOpenHelper.getInstance(context);
	}

	@Override
	public boolean save(ExamObj e) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		List<ProblemInfoObj> plist = e.getpInfoList();
		int examId = e.getId();
		ContentValues v = null;
		for (ProblemInfoObj i : plist) {
			v = new ContentValues();
			v.put("examId", examId);
			v.put("id", i.getId());
			v.put("point", i.getPoint());
			v.put("type", i.getType());
			v.put("answer", i.getAnswer());
			v.put("status", I.choice.answerState_waitAnser);
			if (db.insert(DBOpenHelper.TABLE_PROBLEM, null, v) <= 0)
				return false;
		}

		return result;
	}

	@Override
	public Problem get(int id, int examId) {
		db = dbHelper.getReadableDatabase();

		Cursor c = db.query(DBOpenHelper.TABLE_PROBLEM, null,
				"examId = ? and id = ?", new String[] { String.valueOf(examId),
						String.valueOf(id) }, null, null, null);
		Problem p = null;
		if (c.moveToNext()) {
			p = new Problem();
			p.setExamId(c.getInt(c.getColumnIndex("examId")));
			p.setId(c.getInt(c.getColumnIndex("id")));
			p.setType(c.getInt(c.getColumnIndex("type")));
			p.setAnswer(c.getString(c.getColumnIndex("answer")));
			p.setPoint(c.getDouble(c.getColumnIndex("point")));
			p.setScore(c.getDouble(c.getColumnIndex("score")));
			p.setGivenAnswer(c.getString(c.getColumnIndexOrThrow("givenAnswer")));
			p.setMarkedAnswer(c.getString(c.getColumnIndex("markedAnswer")));
			p.setStatus(c.getInt(c.getColumnIndex("status")));
		}
		c.close();
		return p;
	}

	@Override
	public List<Integer> get(int examId) {
		db = dbHelper.getReadableDatabase();
		Cursor c = db.query(DBOpenHelper.TABLE_PROBLEM, new String[] { "id" },
				"examId = ?", new String[] { String.valueOf(examId) }, null,
				null, null);
		List<Integer> list = new ArrayList<Integer>();
		while (c.moveToNext()) {
			list.add(c.getInt(c.getColumnIndex("id")));
		}
		c.close();
		return list;
	}

	@Override
	public boolean updateGivenAnswer(int id, int examId, String givenAnswer) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put("givenAnswer", givenAnswer);
		if (db.update(DBOpenHelper.TABLE_PROBLEM, v, "examId = ? and id = ?",
				new String[] { String.valueOf(examId), String.valueOf(id) }) <= 0)
			result = false;

		return result;
	}

	@Override
	public boolean saveMarkedAnswer(int id, int examId, String markedAnswer) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put("markedAnswer", markedAnswer);
		if (db.update(DBOpenHelper.TABLE_PROBLEM, v, "examId = ? and id = ?",
				new String[] { String.valueOf(examId), String.valueOf(id) }) <= 0)
			result = false;
		return result;
	}

	@Override
	public boolean saveScore(int id, int examId, Double score) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put("score", score);
		if (db.update(DBOpenHelper.TABLE_PROBLEM, v, "examId = ? and id = ?",
				new String[] { String.valueOf(examId), String.valueOf(id) }) <= 0)
			result = false;
		return result;
	}

	@Override
	public boolean updateStatus(int id, int examId, int status) {
		boolean result = true;
		db = dbHelper.getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put("status", status);
		if (db.update(DBOpenHelper.TABLE_PROBLEM, v, "examId = ? and id = ?",
				new String[] { String.valueOf(examId), String.valueOf(id) }) <= 0)
			result = false;

		return result;
	}

}
