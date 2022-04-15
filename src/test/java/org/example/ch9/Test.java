package org.example.ch9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: Test
 * @Description:
 * @Date: 2022/4/11
 **/
@Slf4j
public class Test {

	public static void main(String[] args) {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(100));
		List<Callable<Void>> tasks = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			tasks.add(new Task());
		}
		try {
//			List<Future<Void>> results = threadPoolExecutor.invokeAll(tasks);
//			for (Future<Void> r : results) {
//				r.get(2, TimeUnit.SECONDS);
//			}
			Future<Void> r = threadPoolExecutor.submit(new Task());
			r.get(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("test");
		threadPoolExecutor.shutdown();
	}

	static class Task implements Callable<Void> {

		@Override
		public Void call() {
			log.info(Thread.currentThread().getName());
			try {
				Thread.sleep(100000L);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
			return null;
		}
	}
}
