package org.example.ch8._2;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestUncaughtEx
 * @Description:
 * @Date: 2022/3/30
 **/
@Slf4j
public class TestUncaughtEx {

	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			while (true) {
				int i = 1 / 0;
			}
		}
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new MyRunnable());
		thread.setUncaughtExceptionHandler((t, ex) -> {
			log.error("Thread-name:{},ex:{}", t.getName(), ex.getMessage());
		});
		thread.start();
	}
}
