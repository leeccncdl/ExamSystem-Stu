package com.todayedu.exam.student.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ebag.net.obj.I;

/**
 * ���ض��� һ��������Ϣ
 * 
 * @author Craig Lee
 * 
 */
public class Exam implements Serializable {

	private static final long serialVersionUID = -6480096635866057180L;

	/**
	 * ����δ��ʼ
	 */
	public static final int STATUS_UNFINISHED = 0;

	/**
	 * ���Խ�����
	 */
	public static final int STATUS_UNDER_FINISHED = 2;
	/**
	 * �������,������ʦ��û����
	 */
	public static final int STATUS_FINISHED_WITH_NO_GRADE = 4;

	/**
	 * ��ʦ�������Ծ�
	 */
	public static final int STATUS_FINISHED_WITH_GRADE = 8;

	/**
	 * �Ծ�id
	 */
	private Integer id;
	/**
	 * ��������
	 */
	private String name;
	/**
	 * ����ʱ��
	 */
	private Long time;

	/**
	 * ��������
	 */
	private Integer type;

	/**
	 * ����ʣ��ʱ��
	 */
	private Long remainTime;

	/**
	 * ����״̬
	 */
	private Integer status;

	/**
	 * �����������
	 */
	private Date finishDate;

	/**
	 * ���Գɼ�
	 */
	private Double score;

	private List<Integer> problemList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(Long remainTime) {
		this.remainTime = remainTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public List<Integer> getProblemList() {
		return problemList;
	}

	public void setProblemList(List<Integer> problemList) {
		this.problemList = problemList;
	}

	/**
	 * ��������
	 * 
	 * @param type
	 * @return
	 */
	public static String parseType(int type) {
		if (type == I.choice.examType_exam)
			return "���ÿ���";
		else if (type == I.choice.examType_homework)
			return "��ͥ��ҵ";
		return "";
	}

	@Override
	public String toString() {
		return "Exam [id=" + id + ", name=" + name + ", time=" + time
				+ ", type=" + type + ", remainTime=" + remainTime + ", status="
				+ status + ", finishDate=" + finishDate + ", score=" + score
				+ ", problemList=" + problemList + "]";
	}

}
