package com.todayedu.exam.student.model;

import java.io.Serializable;

/**
 * ���ض��� ��Ŀ
 * 
 * @author Craig Lee
 * 
 */
public class Problem implements Serializable {

	private static final long serialVersionUID = 720093452853278935L;
	/**
	 * ����������е�id
	 */
	private Integer id;

	/**
	 * �������ĳ�����
	 */
	private Integer examId;

	/**
	 * ��Ŀ����
	 */
	private Integer type;

	/**
	 * ��
	 */
	private String answer;

	/**
	 * �����ֵ
	 */
	private Double point;

	/**
	 * ����÷�
	 */
	private Double score;

	/**
	 * ѧ�������Ĵ�,ѡ�������A-D����������ͼƬ��·��
	 */
	private String givenAnswer;

	/**
	 * ����ʦ�����˵�������
	 */
	private String markedAnswer;
	
	/**
	 * ��ǰ״̬���ȴ��ش𡢻ش�ȴ���ע����ע�ȴ�������ȫ������
	 */
	private int status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getGivenAnswer() {
		return givenAnswer;
	}

	public void setGivenAnswer(String givenAnswer) {
		this.givenAnswer = givenAnswer;
	}

	public String getMarkedAnswer() {
		return markedAnswer;
	}

	public void setMarkedAnswer(String markedAnswer) {
		this.markedAnswer = markedAnswer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", examId=" + examId + ", type=" + type
				+ ", answer=" + answer + ", point=" + point + ", score="
				+ score + ", givenAnswer=" + givenAnswer + ", markedAnswer="
				+ markedAnswer + ", status=" + status + "]";
	}

}
