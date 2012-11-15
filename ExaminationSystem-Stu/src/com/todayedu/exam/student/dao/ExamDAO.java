package com.todayedu.exam.student.dao;

import java.util.List;

import org.ebag.net.obj.exam.ExamObj;

import com.todayedu.exam.student.model.Exam;

/**
 * ��ȡ������Ϣ
 * 
 * @author Craig Lee
 * 
 */
public interface ExamDAO {

	/**
	 * ����һ�����µĿ�����Ϣ
	 * 
	 * @param e
	 * @return
	 */
	public boolean save(List<ExamObj> list);

	/**
	 * ����һ��������Ϣ
	 * 
	 * @param exam
	 * @return
	 */
	public boolean save(ExamObj exam);

	/**
	 * ��ȡ�Ѿ������Ŀ��Ծ�
	 * 
	 * @return
	 */
	public List<Exam> getFinished();

	/**
	 * ��ȡû�н��еĿ���
	 * 
	 * @return
	 */
	public List<Exam> getUnfinished();

	/**
	 * �õ�һ��������Ϣ
	 * 
	 * @param examid
	 * @return
	 */
	public Exam get(int examid);

	/**
	 * ͨ������ѡ����
	 * 
	 * @param date
	 * @return
	 */
	public List<Exam> getFinishedByDate(int year, int month, int day);

	/**
	 * ���¿���ʣ��ʱ��
	 * 
	 * @param examid
	 * @param remainTime
	 * @return
	 */
	public boolean updateRemainTime(int examid, long remainTime);

	/**
	 * ���¿���״̬
	 * 
	 * @param examid
	 * @param status
	 * @return
	 */
	public boolean updateStatus(int examid, int status);

	/**
	 * ��ȡ����״̬
	 * 
	 * @param examid
	 * @return
	 */
	public int getStatus(int examid);

	/**
	 * �ж���ο���ÿ�����Ƿ��Ѿ����˳ɼ�
	 * 
	 * @param examid
	 * @return
	 */
	public boolean isAllScored(int examId);

	/**
	 * �����ܵĿ��Գɼ�
	 * 
	 * @param examId
	 * @return
	 */
	public boolean calculateTotalScore(int examId);
}
