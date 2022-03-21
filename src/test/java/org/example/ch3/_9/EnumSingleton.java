package org.example.ch3._9;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: EnumSingleton
 * @Description:
 * @Date: 2022/3/21
 **/
@Slf4j
public enum EnumSingleton {
	INSTANCE;

	EnumSingleton() {
		System.out.println("Singleton is init");
	}

	public void someService() {
		log.info("someService is invoked");
	}

	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			log.info("{}", EnumSingleton.INSTANCE.hashCode());
		}
	}

	public static void main(String[] args) {
		int num = 10;
		Runnable runnable = new MyRunnable();
		for (int i = 0; i < num; i++) {
			Thread thread = new Thread(runnable, "thread-" + i);
			thread.start();
		}
	}
}
