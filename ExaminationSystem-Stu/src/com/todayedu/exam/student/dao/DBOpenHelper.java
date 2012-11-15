package com.todayedu.exam.student.dao;

import com.todayedu.exam.student.utils.L;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���������ݿ������
 * 
 * @author Craig Lee
 * 
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	public static int VERSION = 1;

	public static final String DB_NAME = "examinationsystem_stu.db";

	private static DBOpenHelper instance = null;
	/**
	 * ���Ա���
	 */
	public static final String TABLE_EXAM = "exam";
	/**
	 * ��Ŀ��
	 */
	public static final String TABLE_PROBLEM = "problem";

	private DBOpenHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	private DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * ��ȡhelperʵ��
	 * 
	 * @param context
	 * @return
	 */
	public static DBOpenHelper getInstance(Context context) {
		if (instance == null)
			instance = new DBOpenHelper(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE if exists " + TABLE_EXAM);
		db.execSQL("DROP TABLE if exists " + TABLE_PROBLEM);

		StringBuffer sb = new StringBuffer(20);
		sb.append("CREATE TABLE " + TABLE_EXAM + "(");
		sb.append("id integer not null,");
		sb.append("name text,");
		sb.append("type integer,");
		sb.append("time integer,");
		sb.append("remainTime integer,");
		sb.append("status integer,");
		sb.append("finishDate date,");
		sb.append("score integer");
		sb.append(")");
		// ����
		L.i(sb.toString());
		db.execSQL(sb.toString());

		sb = new StringBuffer(20);
		sb.append("CREATE TABLE " + TABLE_PROBLEM + "(");
		sb.append("examId integer not null,");
		sb.append("id integer not null,");
		sb.append("type integer,");
		sb.append("answer text,");
		sb.append("point text,");// �����ֵ
		sb.append("score integer,");// ѧ������÷�
		sb.append("givenAnswer text,");
		sb.append("markedAnswer text,");
		sb.append("status integer");
		sb.append(")");
		L.i(sb.toString());
		db.execSQL(sb.toString());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// �������ݿ�
		db.execSQL("DROP TABLE if exists " + TABLE_EXAM);
		db.execSQL("DROP TABLE if exists " + TABLE_PROBLEM);
		VERSION = newVersion;
		onCreate(db);
	}

}
