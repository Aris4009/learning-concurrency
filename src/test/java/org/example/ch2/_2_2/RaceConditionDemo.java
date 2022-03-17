package org.example.ch2._2_2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: RaceConditionDemo
 * @Description:
 * @Date: 2022/3/16
 **/
@Slf4j
public class RaceConditionDemo {

	private static final Map<String, Object> map = new ConcurrentHashMap<>();

	public static void main(String[] args) throws InterruptedException {
		int numberOfThread = 4;
		Thread[] threads = new Thread[numberOfThread];
		for (int i = 0; i < numberOfThread; i++) {
			threads[i] = new WorkThread(i, 10);
		}
		for (Thread t : threads) {
			t.start();
		}
	}

	static class WorkThread extends Thread {

		private final int id;

		private final int requestCount;

		public WorkThread(int id, int requestCount) {
			super("worker-" + id);
			this.id = id;
			this.requestCount = requestCount;
		}

		@SneakyThrows
		@Override
		public void run() {
			int i = requestCount;
			String requestID;
			RequestIDGenerator requestIDGenerator = RequestIDGenerator.getInstance();
			while (i > 0) {
				requestID = requestIDGenerator.nextID();
				processRequestID(requestID);
				i--;
			}
		}

		private void processRequestID(String requestID) {
			map.put(requestID, requestID);
			log.info("count:{}, {} got requestID: {}", map.size(), Thread.currentThread().getName(), requestID);
		}
	}
}
