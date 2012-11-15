package com.todayedu.exam.student.service;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.ebag.net.obj.I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.todayedu.exam.student.Const;
import com.todayedu.exam.student.R;

/**
 * �������ӷ���,ʹ�ù㲥��Ϣ����
 * 
 * @author Craig Lee
 * 
 */
public class NetService extends Service {
	/**
	 * ѡ��������������
	 */
	public static final String EXTRA_DATA_SERVICE_TYPE = "extra_data_service_type";
	/**
	 * Ҫ���͵�ExtraData�����name
	 */
	public static final String EXTRA_DATA_OBJECT_TO_BE_SENT = "extra_data_object_to_be_sent";
	/**
	 * ����������ͣ���������
	 */
	public static final int SERVICE_CHOICE_CONNECT = 1;
	/**
	 * ����������ͣ��Ͽ�����
	 */
	public static final int SERVICE_CHOICE_DISCONNECT = 0;
	/**
	 * ����������ͣ����Ͷ���
	 */
	public static final int SERVICE_CHOICE_SEND_OBJECT = 2;

	private static final Logger logger = LoggerFactory
			.getLogger(NetService.class);
	private IoSession session = null;
	private IoConnector connector = null;
	// ��ʱ��Handler
	private Handler handler;

	public NetService() {
		super();
		// �����߳���������
		HandlerThread handlerThread = new HandlerThread(
				"com.todayedu.examinationsystem.student.netHandler");
		handlerThread.start();
		this.handler = new Handler(handlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = (Intent) msg.obj;
				int msgType = intent.getIntExtra(EXTRA_DATA_SERVICE_TYPE, -1);
				switch (msgType) {
					case SERVICE_CHOICE_CONNECT:
						connect(intent);
						break;
					case SERVICE_CHOICE_DISCONNECT:
						disconnect();
						break;
					case SERVICE_CHOICE_SEND_OBJECT: {
						// �������������Ϣ
						session.write(intent
								.getSerializableExtra(EXTRA_DATA_OBJECT_TO_BE_SENT));
						break;
					}
				}
			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = this.handler.obtainMessage();
		msg.obj = intent;
		msg.sendToTarget();
		return super.onStartCommand(intent, flags, startId);
	}

	// ���ӷ�����
	private void connect(Intent intent) {

		connector = new NioSocketConnector();
		connector
				.setConnectTimeoutMillis(Const.Network.CONNECT_TIME_OUT_MILLIS);
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("objectFilter",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(new ClientHandler(getApplicationContext()));

		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(
					I.tupload.mina_server_site, I.tupload.mina_server_port));
			future.awaitUninterruptibly();
			session = future.getSession();

			session.write(intent
					.getSerializableExtra(EXTRA_DATA_OBJECT_TO_BE_SENT));
		} catch (RuntimeIoException e) {
			logger.error(e.getMessage(), e);
			Toast.makeText(getApplicationContext(), R.string.common_netError,
					Toast.LENGTH_LONG).show();
		}
	}

	// ��������Ͽ�����
	private void disconnect() {
		CloseFuture future = session.getCloseFuture();
		future.awaitUninterruptibly(1000);
		connector.dispose();
		this.stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
