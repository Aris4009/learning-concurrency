package org.example.ch3._9;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: DoubleCheckedLocking
 * @Description: 双重检查锁定实现的单例模式
 * @Date: 2022/3/21
 **/
@Slf4j
public class DoubleCheckedLocking {

	private DoubleCheckedLocking() {
	}

	private volatile static DoubleCheckedLocking INSTANCE;

	public static DoubleCheckedLocking getInstance() {
		if (INSTANCE == null) {
			synchronized (DoubleCheckedLocking.class) {
				if (INSTANCE == null) {
					INSTANCE = new DoubleCheckedLocking();
				}
			}
		}
		return INSTANCE;
	}

	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			DoubleCheckedLocking doubleCheckedLocking = DoubleCheckedLocking.getInstance();
			log.info("{}", doubleCheckedLocking.hashCode());
		}
	}

	public static void main(String[] args) {
		int num = 20;
		Runnable runnable = new MyRunnable();
		for (int i = 0; i < num; i++) {
			Thread thread = new Thread(runnable, "thread-" + i);
			thread.start();
		}
	}
}
