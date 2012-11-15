package com.todayedu.exam.student.dao;

import java.util.List;

import org.ebag.net.obj.exam.ExamObj;

import com.todayedu.exam.student.model.Problem;

/**
 * 存取考试题目信息
 * 
 * @author Craig Lee
 * 
 */
public interface ProblemDAO {

	/**
	 * 保存题目列表
	 * 
	 * @param list
	 * @return
	 */
	public boolean save(ExamObj e);

	/**
	 * 获取一条考试题目
	 * 
	 * @param id
	 * @param examId
	 * @return
	 */
	public Problem get(int id, int examId);

	/**
	 * 获取题目列表
	 * 
	 * @param examId
	 * @return
	 */
	public List<Integer> get(int examId);

	/**
	 * 更新学生做出的选项
	 * 
	 * @param answer
	 * @return
	 */
	public boolean updateGivenAnswer(int id, int examId, String givenAnswer);

	/**
	 * 保存老师批阅后的简答题
	 * 
	 * @param id
	 * @param examId
	 * @param markedAnswer
	 * @return
	 */
	public boolean saveMarkedAnswer(int id, int examId, String markedAnswer);

	/**
	 * 保存一道题的成绩
	 * 
	 * @param id
	 * @param examId
	 * @param score
	 * @return
	 */
	public boolean saveScore(int id, int examId, Double score);

	/**
	 * 更改题目状态
	 * 
	 * @param id
	 * @param examId
	 * @param status
	 * @return
	 */
	public boolean updateStatus(int id, int examId, int status);
}
