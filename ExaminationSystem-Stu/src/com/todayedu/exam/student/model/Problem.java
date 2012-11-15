package com.todayedu.exam.student.model;

import java.io.Serializable;

/**
 * 本地定义 题目
 * 
 * @author Craig Lee
 * 
 */
public class Problem implements Serializable {

	private static final long serialVersionUID = 720093452853278935L;
	/**
	 * 服务器题库中的id
	 */
	private Integer id;

	/**
	 * 隶属于哪场考试
	 */
	private Integer examId;

	/**
	 * 题目类型
	 */
	private Integer type;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 本题分值
	 */
	private Double point;

	/**
	 * 本题得分
	 */
	private Double score;

	/**
	 * 学生做出的答案,选择题就是A-D，简答题就是图片的路径
	 */
	private String givenAnswer;

	/**
	 * 被老师批阅了的主观题
	 */
	private String markedAnswer;
	
	/**
	 * 当前状态，等待回答、回答等待批注、批注等待讲评、全做完了
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
