package org.example.ch11._5;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestVolatile
 * @Description: volatile内存屏障
 * @Date: 2022/4/6
 **/
@Slf4j
public class TestVolatile {

	static class Obj {

		private String a;

		private String b;

		private volatile boolean flag;

//		private boolean flag;

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}
	}

	static class Producer implements Runnable {

		private final Obj obj;

		public Producer(Obj obj) {
			this.obj = obj;
		}

		@SneakyThrows
		@Override
		public void run() {
			obj.setA("aaa");
			obj.setB("bbb");
			Thread.sleep(5000L);
			obj.setFlag(true);
//			log.info("success write");
		}
	}

	static class Consumer implements Runnable {

		private final Obj obj;

		public Consumer(Obj obj) {
			this.obj = obj;
		}

		@Override
		public void run() {
			while (!obj.isFlag()) {
			}
			log.info("{},{},{},{}", Thread.currentThread().getName(), obj.getA(), obj.getB(), obj.isFlag());
		}
	}

	static class XThreadFactory implements ThreadFactory {

		private final String prefix;

		private final AtomicInteger atomicInteger = new AtomicInteger(1);

		public XThreadFactory(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, prefix + atomicInteger.getAndIncrement());
			thread.setDaemon(false);
			thread.setPriority(Thread.NORM_PRIORITY);
//			log.info("{} is created", thread.getName());
			return thread;
		}
	}

	public static void main(String[] args) {
		ThreadFactory producerThreadFactory = new XThreadFactory("producer");
		ThreadPoolExecutor producer = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(10), producerThreadFactory);

		ThreadFactory consumerThreadFactory = new XThreadFactory("consumer");
		ThreadPoolExecutor consumer = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(20), consumerThreadFactory);

		Obj obj = new Obj();
		Producer p = new Producer(obj);
		producer.execute(p);
		producer.shutdown();
		Consumer c = new Consumer(obj);

		for (int i = 0; i < 20; i++) {
			consumer.execute(c);
		}
		consumer.shutdown();
	}
}
