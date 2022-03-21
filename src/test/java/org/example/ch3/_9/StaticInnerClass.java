package org.example.ch3._9;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: StaticInnerClass
 * @Description: 静态内部类实现单例模式
 * @Date: 2022/3/21
 **/
@Slf4j
public class StaticInnerClass {

	private StaticInnerClass() {
		log.info("StaticInnerClass is init");
	}

	static class InstanceHolder {
		final static StaticInnerClass INSTANCE = new StaticInnerClass();
	}

	public static StaticInnerClass getInstance() {
		log.info("getInstance");
		return InstanceHolder.INSTANCE;
	}

	public void someService() {
		log.info("someService is invoked");
	}

	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			StaticInnerClass staticInnerClass = StaticInnerClass.getInstance();
			log.info("{}", staticInnerClass.hashCode());
			staticInnerClass.someService();
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
