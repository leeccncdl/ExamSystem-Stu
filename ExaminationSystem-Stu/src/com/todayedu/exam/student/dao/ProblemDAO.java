package com.todayedu.exam.student.dao;

import java.util.List;

import org.ebag.net.obj.exam.ExamObj;

import com.todayedu.exam.student.model.Problem;

/**
 * ��ȡ������Ŀ��Ϣ
 * 
 * @author Craig Lee
 * 
 */
public interface ProblemDAO {

	/**
	 * ������Ŀ�б�
	 * 
	 * @param list
	 * @return
	 */
	public boolean save(ExamObj e);

	/**
	 * ��ȡһ��������Ŀ
	 * 
	 * @param id
	 * @param examId
	 * @return
	 */
	public Problem get(int id, int examId);

	/**
	 * ��ȡ��Ŀ�б�
	 * 
	 * @param examId
	 * @return
	 */
	public List<Integer> get(int examId);

	/**
	 * ����ѧ��������ѡ��
	 * 
	 * @param answer
	 * @return
	 */
	public boolean updateGivenAnswer(int id, int examId, String givenAnswer);

	/**
	 * ������ʦ���ĺ�ļ����
	 * 
	 * @param id
	 * @param examId
	 * @param markedAnswer
	 * @return
	 */
	public boolean saveMarkedAnswer(int id, int examId, String markedAnswer);

	/**
	 * ����һ����ĳɼ�
	 * 
	 * @param id
	 * @param examId
	 * @param score
	 * @return
	 */
	public boolean saveScore(int id, int examId, Double score);

	/**
	 * ������Ŀ״̬
	 * 
	 * @param id
	 * @param examId
	 * @param status
	 * @return
	 */
	public boolean updateStatus(int id, int examId, int status);
}
