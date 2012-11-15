package com.todayedu.exam.student.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ebag.net.obj.I;

/**
 * 本地定义 一场考试信息
 * 
 * @author Craig Lee
 * 
 */
public class Exam implements Serializable {

	private static final long serialVersionUID = -6480096635866057180L;

	/**
	 * 考试未开始
	 */
	public static final int STATUS_UNFINISHED = 0;

	/**
	 * 考试进行中
	 */
	public static final int STATUS_UNDER_FINISHED = 2;
	/**
	 * 考试完成,但是老师还没批阅
	 */
	public static final int STATUS_FINISHED_WITH_NO_GRADE = 4;

	/**
	 * 老师批阅完试卷
	 */
	public static final int STATUS_FINISHED_WITH_GRADE = 8;

	/**
	 * 试卷id
	 */
	private Integer id;
	/**
	 * 考试名称
	 */
	private String name;
	/**
	 * 考试时长
	 */
	private Long time;

	/**
	 * 考试类型
	 */
	private Integer type;

	/**
	 * 考试剩余时间
	 */
	private Long remainTime;

	/**
	 * 考试状态
	 */
	private Integer status;

	/**
	 * 考试完成日期
	 */
	private Date finishDate;

	/**
	 * 考试成绩
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
	 * 解析类型
	 * 
	 * @param type
	 * @return
	 */
	public static String parseType(int type) {
		if (type == I.choice.examType_exam)
			return "随堂考试";
		else if (type == I.choice.examType_homework)
			return "家庭作业";
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
