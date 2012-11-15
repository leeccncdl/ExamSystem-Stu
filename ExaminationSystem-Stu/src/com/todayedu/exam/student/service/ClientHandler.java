package com.todayedu.exam.student.service;

import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.ebag.net.obj.I;
import org.ebag.net.obj.answer.AnswerObj;
import org.ebag.net.response.AnswerResponse;
import org.ebag.net.response.ExamResponse;
import org.ebag.net.response.LoginResponse;
import org.ebag.net.servermsg.StartExamMessage;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.todayedu.exam.student.Const.Broadcast;
import com.todayedu.exam.student.R;
import com.todayedu.exam.student.Welcome;
import com.todayedu.exam.student.dao.ExamDAO;
import com.todayedu.exam.student.dao.ProblemDAO;
import com.todayedu.exam.student.dao.impl.ExamDAOImpl;
import com.todayedu.exam.student.dao.impl.ProblemDAOImpl;
import com.todayedu.exam.student.model.Exam;
import com.todayedu.exam.student.utils.L;

/**
 * 处理接收到的数据对象，基于广播机制广播消息
 * 
 * @author Craig Lee
 * 
 */
public class ClientHandler extends IoHandlerAdapter {

	private Context context = null;
	private ExamDAO examDAO = null;
	private ProblemDAO problemDAO = null;

	public ClientHandler(Context context) {
		this.context = context;
		examDAO = ExamDAOImpl.getInstance(this.context);
		problemDAO = ProblemDAOImpl.getInstance(this.context);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		L.i("Mina Handler:received msg--->" + message);
		if (message instanceof LoginResponse) {
			// 登录消息返回
			dealLoginResponse((LoginResponse) message);
		} else if (message instanceof ExamResponse) {
			// 主动请求Exam的响应
			dealExamResponse((ExamResponse) message);
		} else if (message instanceof StartExamMessage) {
			// 发放考试题
			dealStartExam((StartExamMessage) message);
		} else if (message instanceof AnswerResponse) {
			// 发放考试答案和成绩
			// 更新考试状态为已获取成绩
			dealAnswerResponse((AnswerResponse) message);
		}

	}

	/**
	 * 处理登录消息返回
	 * 
	 * @param loginResponse
	 */
	private void dealLoginResponse(LoginResponse loginResponse) {
		Intent intent = new Intent(Broadcast.LOGIN_RESPONSE);
		intent.putExtra(Welcome.LOGIN_RESPONSE, loginResponse);
		this.context.sendBroadcast(intent);
	}

	/**
	 * 处理发放考试题
	 * 
	 * @param startExamMessage
	 */
	private void dealStartExam(StartExamMessage startExamMessage) {
		examDAO.save(startExamMessage.getExam());
		// 通知更新界面
		Intent intent = new Intent(Broadcast.EXAM_CHOOSE_NEW_EXAM);
		this.context.sendBroadcast(intent);
	}

	/**
	 * 处理主动请求Exam的响应
	 * 
	 * @param message
	 */
	private void dealExamResponse(ExamResponse message) {
		examDAO.save(message.examList);
		// 通知更新界面
		Intent intent = new Intent(Broadcast.EXAM_CHOOSE_NEW_EXAM);
		this.context.sendBroadcast(intent);
	}

	/**
	 * 处理发放考试答案和成绩
	 * 
	 * @param message
	 */
	private void dealAnswerResponse(AnswerResponse message) {
		final int examId = message.examId;
		List<AnswerObj> answerList = message.getExamList();
		//L.i("AnswerList size:"+answerList.size());
		if (answerList.size() > 0) {
			// 存在数据
			for (AnswerObj ao : answerList) {
				final int type = problemDAO.get(ao.problemId, examId).getType();
				//L.i("type:"+type);
				//L.i("state:"+ao.getState());
				if (type == I.choice.problemType_jd
						&& ao.getState() == I.choice.answerState_waitComment) {
					// 已经批阅
					problemDAO.updateStatus(ao.problemId, examId,
							I.choice.answerState_waitComment);
					problemDAO.saveScore(ao.problemId, examId, ao.getPoint());
					L.i("markedAnswerUrl"+ao.picOfTeacherUrl);
					problemDAO.saveMarkedAnswer(ao.problemId, examId,
							ao.picOfTeacherUrl);
				}
			}
			// 更新考试状态为已获取成绩
			if (examDAO.isAllScored(examId)) {
				examDAO.updateStatus(examId, Exam.STATUS_FINISHED_WITH_GRADE);
				examDAO.calculateTotalScore(examId);
			}
		}
		// 通知更新界面
		Intent intent = new Intent(Broadcast.PROBLEM_CHOOSE_SCORE);
		this.context.sendBroadcast(intent);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
		Toast.makeText(context, R.string.common_netError, Toast.LENGTH_LONG)
				.show();
		L.e(cause.toString());
	}

}
