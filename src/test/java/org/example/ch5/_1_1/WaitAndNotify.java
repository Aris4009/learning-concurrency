package org.example.ch5._1_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: WaitAndNotify
 * @Description:
 * @Date: 2022/3/21
 **/
@Slf4j
public class WaitAndNotify {

	private final Object lock = new Object();

	private final List<String> list = new ArrayList<>();

	private final Random random = new Random();

	public void producer() throws InterruptedException {
		synchronized (lock) {
			while (list.size() == 10) {
				lock.wait();
			}
			list.add(Thread.currentThread().getName());
//			if (list.size() == 10) {
//				log.info("thread-{},{}", Thread.currentThread().getName(),
//						Arrays.toString(list.toArray(new Integer[0])));
//			}
//			log.info("thread-{},{}", Thread.currentThread().getName(), Arrays.toString(list.toArray(new Integer[0])));
			lock.notifyAll();
		}
	}

	public void consumer() throws InterruptedException {
		synchronized (lock) {
			while (list.size() < 10) {
				lock.wait();
			}
			for (String str : list) {
				log.info(str);
			}
			list.clear();
			lock.notifyAll();
		}
	}

	static class MyProducer implements Runnable {

		private final WaitAndNotify waitAndNotify;

		public MyProducer(WaitAndNotify waitAndNotify) {
			this.waitAndNotify = waitAndNotify;
		}

		@SneakyThrows
		@Override
		public void run() {
			int i = 10;
			while (i > 0) {
				waitAndNotify.producer();
				i--;
			}
		}
	}

	static class MyConsumer implements Runnable {

		private final WaitAndNotify waitAndNotify;

		public MyConsumer(WaitAndNotify waitAndNotify) {
			this.waitAndNotify = waitAndNotify;
		}

		@SneakyThrows
		@Override
		public void run() {
			while (true) {
				waitAndNotify.consumer();
			}
		}
	}

	public static void main(String[] args) {
		WaitAndNotify waitAndNotify = new WaitAndNotify();
		Runnable runnable = new MyProducer(waitAndNotify);
		for (int i = 0; i < 20; i++) {
			Thread producer = new Thread(runnable, "producer-" + i);
			producer.start();
		}
		Thread consumer = new Thread(new MyConsumer(waitAndNotify), "consumer");
		consumer.start();
	}
}
