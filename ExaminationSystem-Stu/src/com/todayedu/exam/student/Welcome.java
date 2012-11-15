package com.todayedu.exam.student;

import org.ebag.net.obj.I;
import org.ebag.net.request.LoginRequest;
import org.ebag.net.response.LoginResponse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.todayedu.exam.student.service.NetService;
import com.todayedu.exam.student.utils.L;

/**
 * ��ʼ����
 * 
 * @author Craig Lee
 * 
 */
public class Welcome extends BaseActivity {
	/**
	 * ��¼���ؽ������
	 */
	public static final String LOGIN_RESPONSE = "login_response";

	private LoginReciver loginReciver = null;

	private long loginTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginTime = System.currentTimeMillis();
		setContentView(R.layout.welcome);
		// ��¼��Ϣ������
		registLoginReceiver();
		// ������̨�������
		connectToServer();

	}

	// ��¼��Ϣ������
	private void registLoginReceiver() {
		// ��¼��Ϣ������
		loginReciver = new LoginReciver();
		IntentFilter loginFilter = new IntentFilter();
		loginFilter.addAction(Const.Broadcast.LOGIN_RESPONSE);
		this.registerReceiver(loginReciver, loginFilter);
	}

	// ������̨�������
	private void connectToServer() {
		Intent startNetIntent = new Intent(Welcome.this, NetService.class);
		startNetIntent.putExtra(NetService.EXTRA_DATA_SERVICE_TYPE,
				NetService.SERVICE_CHOICE_CONNECT);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.uname = "student";
		loginRequest.upwd = "student";
		startNetIntent.putExtra(NetService.EXTRA_DATA_OBJECT_TO_BE_SENT,
				loginRequest);
		startService(startNetIntent);
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(loginReciver);
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}
	
	/**
	 * ���ܵ�¼���ص���Ϣ
	 * 
	 * @author Craig Lee
	 * 
	 */
	private class LoginReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LoginResponse loginResponse = (LoginResponse) intent
					.getSerializableExtra(LOGIN_RESPONSE);
			if (loginResponse.result == I.signal.login_true) {
				// ��¼�ɹ�
				L.i("LoginResponse--->" + loginResponse.user.getId() + "-"
						+ loginResponse.user.getName());
				App.uid = loginResponse.getUser().getId();
				showShortToast(R.string.Welcome_login_success);
				long interval = System.currentTimeMillis() - loginTime;
				if (interval < 3000) {
					// С��3��͵�¼�ɹ�
					try {
						Thread.sleep(3000 - interval);
					} catch (InterruptedException e) {
						L.i(e);
					}
				}
				startActivityForResult(new Intent(Welcome.this, ExamChoose.class), 1);
			} else {
				// ��¼ʧ��
				showLongToast(R.string.Welcome_login_fail);
				finish();
			}

		}

	}

}