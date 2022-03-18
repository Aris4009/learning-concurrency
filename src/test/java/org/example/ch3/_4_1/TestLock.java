package org.example.ch3._4_1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestLock
 * @Description: 使用显示锁来实现发号器
 * @Date: 2022/3/18
 **/
@Slf4j
public class TestLock {

	private int sequence = 0;

	private static final int UPPER = 10;

	private static final int LOWER = -1;

	private final Lock lock = new ReentrantLock();

	public int nextSequence() throws Exception {
//		lock.lock();
//		try {
//			sequence++;
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		} finally {
//			lock.unlock();
//		}
//		return sequence;
		if (lock.tryLock()) {
			try {
				sequence++;
			} finally {
				lock.unlock();
			}
			return sequence;
		} else {
			return -1;
		}
	}

	static class MyRunnable implements Runnable {

		private final TestLock testLock;

		public MyRunnable(TestLock testLock) {
			this.testLock = testLock;
		}

		@Override
		public void run() {
			try {
				int seq = testLock.nextSequence();
				while (seq <= UPPER && seq > LOWER) {
					log.info("name:{},count:{}", Thread.currentThread().getName(), seq);
					seq = testLock.nextSequence();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) {
		int num = 3;
		TestLock testLock = new TestLock();
		MyRunnable myRunnable = new MyRunnable(testLock);
		for (int i = 0; i < num; i++) {
			Thread thread = new Thread(myRunnable, "thread-" + i);
			thread.start();
		}
	}
}
