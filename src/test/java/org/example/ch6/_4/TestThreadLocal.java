package org.example.ch6._4;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestThreadLocal
 * @Description:
 * @Date: 2022/3/28
 **/
@Slf4j
public class TestThreadLocal {

	static class IDGenerator {

		private static final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

		private static final ThreadLocal<Map<String, Object>> threadMap = InheritableThreadLocal
				.withInitial(HashMap::new);

		public static int getId() {
			int id = threadLocal.get();
			int next = id + 1;
			threadLocal.set(next);
			return id;
		}

		public static void print() {
			Map<String, Object> map = threadMap.get();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				log.info("k:{},v:{}", entry.getKey(), entry.getValue());
			}
		}

		public static void setMap(String key, String value) {
			Map<String, Object> map = threadMap.get();
			map.put(key, value);
		}
	}

	static class MyRunnable implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 2; i++) {
				log.info("{},id:{}", Thread.currentThread().getName(), IDGenerator.getId());
			}
			IDGenerator.setMap(String.valueOf(Thread.currentThread().getId()), Thread.currentThread().getName());
			IDGenerator.print();
		}
	}

	static class MyRunnable2 implements Runnable {

		private long count;

		public MyRunnable2(long count) {
			this.count = count;
		}

		@Override
		public void run() {
//			synchronized (this) {
//				count();
//			}
			count();
		}

		private void count() {
			for (int i = 0; i < 10000; i++) {
				count++;
			}
			log.info("{}", count);
		}
	}

	public static void main(String[] args) {
//		IDGenerator.setMap(String.valueOf(Thread.currentThread().getId()), Thread.currentThread().getName());
//		Runnable runnable = new MyRunnable();
//		int num = 4;
//		for (int i = 0; i < num; i++) {
//			Thread t = new Thread(runnable);
//			t.start();
//		}

		int num = 4;
		for (int i = 0; i < num; i++) {
			Runnable runnable = new MyRunnable2(0L);
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}
}
