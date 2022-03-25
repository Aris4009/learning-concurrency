package org.example.ch5._5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestBlock
 * @Description:
 * @Date: 2022/3/24
 **/
@Slf4j
public class TestBlock {

	static class MyRunnable<T> implements Runnable {

		private final BlockingQueue<T> blockingQueue;

		private final Ops ops;

		private final T data;

		public MyRunnable(BlockingQueue<T> blockingQueue, Ops ops, T data) {
			this.blockingQueue = blockingQueue;
			this.ops = ops;
			this.data = data;
		}

		@SneakyThrows
		@Override
		public void run() {
			T d = data;
			switch (ops) {
			case PUT:
				blockingQueue.put(data);
//				log.info("Thread:{},ops:{},value:{}", Thread.currentThread().getName(), ops, d);
				break;
			case TAKE:
				d = blockingQueue.take();
				log.info("{}", d);
				break;
			case OFFER:
				blockingQueue.offer(data);
				break;
			case POLL:
				d = blockingQueue.poll();
				break;
			}
		}
	}

	static enum Ops {
		PUT, TAKE, OFFER, POLL;
	}

	public static void main(String[] args) {
		BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
		BlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(5);
		BlockingQueue<String> synchronousQueue = new SynchronousQueue<>();

		int n = 20;
		for (int i = 0; i < n; i++) {
			Thread producer = new Thread(new MyRunnable<String>(synchronousQueue, Ops.PUT, String.valueOf(i)),
					"producer-" + i);
			producer.start();
			Thread consumer = new Thread(new MyRunnable<String>(synchronousQueue, Ops.TAKE, null));
			consumer.start();
		}
	}
}
