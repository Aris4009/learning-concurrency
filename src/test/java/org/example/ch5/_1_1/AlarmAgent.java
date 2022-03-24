package org.example.ch5._1_1;

import java.util.Random;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: AlarmAgent
 * @Description:
 * @Date: 2022/3/22
 **/
@Slf4j
public class AlarmAgent {

	private static final AlarmAgent INSTANCE = new AlarmAgent();

	private final HeartBeatThread heartBeatThread = new HeartBeatThread();

	private AlarmAgent() {

	}

	public static AlarmAgent getInstance() {
		return INSTANCE;
	}

	public void init() {
		connectToServer();
		heartBeatThread.setDaemon(true);
		heartBeatThread.start();
	}

	public void sendAlarm(String message) throws InterruptedException {
		synchronized (this) {
			while (!connectedToServer) {
				log.info("Alarm agent was not connected to server");
				this.wait();
			}
			doSendAlarm(message);
		}
	}

	private void doSendAlarm(String message) {
		log.info("send msg:{}", message);
	}

	private boolean connectedToServer = false;

	private void connectToServer() {
		new Thread() {
			@SneakyThrows
			@Override
			public void run() {
				doConnect();
			}
		}.start();
	}

	private void doConnect() throws InterruptedException {
		Thread.sleep(100L);
		synchronized (this) {
			connectedToServer = true;
			log.info("Alarm agent is connected");
			notify();
		}
	}

	class HeartBeatThread extends Thread {

		@SneakyThrows
		@Override
		public void run() {
			Thread.sleep(1000L);
			while (true) {
				if (checkConnection()) {
					connectedToServer = true;
				} else {
					connectedToServer = false;
					log.info("Alarm agent was disconnected from server");
					connectToServer();
				}
				Thread.sleep(2000L);
			}
		}

		private boolean checkConnection() {
			boolean isConnected = true;
			final Random random = new Random();
			int rand = random.nextInt(1000);
			if (rand < 500) {
				isConnected = false;
			}
			return isConnected;
		}
	}

	public static void main(String[] args) {
		AlarmAgent agent = AlarmAgent.getInstance();
		agent.init();
		for (int i = 0; i < 100; i++) {
			try {
				agent.sendAlarm(String.valueOf(i));
				Thread.sleep(1000L);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
