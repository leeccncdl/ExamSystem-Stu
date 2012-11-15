package com.todayedu.exam.student.dao;

import java.util.List;

import org.ebag.net.obj.exam.ExamObj;

import com.todayedu.exam.student.model.Exam;

/**
 * 存取考试信息
 * 
 * @author Craig Lee
 * 
 */
public interface ExamDAO {

	/**
	 * 保存一组最新的考试信息
	 * 
	 * @param e
	 * @return
	 */
	public boolean save(List<ExamObj> list);

	/**
	 * 保存一个考试信息
	 * 
	 * @param exam
	 * @return
	 */
	public boolean save(ExamObj exam);

	/**
	 * 获取已经考过的考试卷
	 * 
	 * @return
	 */
	public List<Exam> getFinished();

	/**
	 * 获取没有进行的考试
	 * 
	 * @return
	 */
	public List<Exam> getUnfinished();

	/**
	 * 得到一条考试信息
	 * 
	 * @param examid
	 * @return
	 */
	public Exam get(int examid);

	/**
	 * 通过日期选择考试
	 * 
	 * @param date
	 * @return
	 */
	public List<Exam> getFinishedByDate(int year, int month, int day);

	/**
	 * 更新考试剩余时间
	 * 
	 * @param examid
	 * @param remainTime
	 * @return
	 */
	public boolean updateRemainTime(int examid, long remainTime);

	/**
	 * 更新考试状态
	 * 
	 * @param examid
	 * @param status
	 * @return
	 */
	public boolean updateStatus(int examid, int status);

	/**
	 * 获取考试状态
	 * 
	 * @param examid
	 * @return
	 */
	public int getStatus(int examid);

	/**
	 * 判断这次考试每道题是否已经有了成绩
	 * 
	 * @param examid
	 * @return
	 */
	public boolean isAllScored(int examId);

	/**
	 * 计算总的考试成绩
	 * 
	 * @param examId
	 * @return
	 */
	public boolean calculateTotalScore(int examId);
}
