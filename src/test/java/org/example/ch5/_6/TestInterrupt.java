package org.example.ch5._6;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestInterrupt
 * @Description:
 * @Date: 2022/3/24
 **/
@Slf4j
public class TestInterrupt {

	/**
	 * 无中断
	 */
	static class NoInterrupt implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 100000; i++) {
				log.info("{}", i);
			}
		}
	}

	/**
	 * 有中断
	 */
	static class Interrupt implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 100000; i++) {
				try {
					if (Thread.interrupted()) {
						log.info("{}", Thread.interrupted());
						throw new InterruptedException("线程被中断");
					}
					log.info("{}", i);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Runnable runnable = new Interrupt();
		Thread thread = new Thread(runnable);
		thread.start();
		Thread.sleep(50L);
		thread.interrupt();
		while (true) {
			if (thread.isInterrupted()) {
				log.info("主线程退出");
				break;
			}
		}
	}
}
