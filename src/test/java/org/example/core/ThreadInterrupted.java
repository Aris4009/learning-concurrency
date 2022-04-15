package org.example.core;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: ThreadInterrupted
 * @Description:
 * @Date: 2022/4/8
 **/
@Slf4j
public class ThreadInterrupted {

	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 50000; i++) {
				int a = i + 1;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new MyRunnable());
		thread.start();
		Thread.sleep(1000L);
		thread.interrupt();
		log.info("{}", thread.isInterrupted());
		log.info("{}", thread.isInterrupted());
	}
}
