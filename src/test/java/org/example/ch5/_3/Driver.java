package org.example.ch5._3;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: Driver
 * @Description:
 * @Date: 2022/3/23
 **/
@Slf4j
public class Driver {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			new Thread(new Worker(startSignal, doneSignal)).start();
		}
		doSomethingElse();
		startSignal.countDown();
		doSomethingElse();
		doneSignal.await();
	}

	private static void doSomethingElse() {
		log.info("do something else");
	}

	static class Worker implements Runnable {

		private final CountDownLatch startSignal;

		private final CountDownLatch doneSignal;

		public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}

		@Override
		public void run() {
			try {
				startSignal.await();
				doWork();
				doneSignal.countDown();
			} catch (Exception e) {

			}
		}

		private void doWork() {
			log.info("do work");
		}
	}
}
