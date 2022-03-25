package org.example.ch5._5;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestSemaphore
 * @Description:
 * @Date: 2022/3/24
 **/
@Slf4j
public class TestSemaphore {

	static class Limit<T> {

		// 无界阻塞队列
		private final BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<>();

		private final Semaphore semaphore;

		public Limit(int limit) {
			semaphore = new Semaphore(limit, true);
		}

		public void put(T data) throws InterruptedException {
			try {
				semaphore.acquire();
				blockingQueue.put(data);
				Thread.sleep(5000L);
			} finally {
				semaphore.release();
			}
		}

		public T take() throws InterruptedException {
			return blockingQueue.take();
		}
	}

	static class Producer<T> implements Runnable {

		private final Limit<T> limit;

		private final T data;

		public Producer(Limit<T> limit, T data) {
			this.limit = limit;
			this.data = data;
		}

		@SneakyThrows
		@Override
		public void run() {
			limit.put(data);
		}
	}

	static class Consumer<T> implements Runnable {

		private final Limit<T> limit;

		public Consumer(Limit<T> limit) {
			this.limit = limit;
		}

		@SneakyThrows
		@Override
		public void run() {
			while (true) {
				log.info("{}", limit.take());
			}
		}
	}

	public static void main(String[] args) {
		int num = 3;
		Limit<String> limit = new Limit<>(num);

		for (int i = 0; i < 10; i++) {
			Thread producer = new Thread(new Producer<String>(limit, String.valueOf(i)));
			producer.start();
		}

		Thread consumer = new Thread(new Consumer<String>(limit));
		consumer.start();
	}
}
