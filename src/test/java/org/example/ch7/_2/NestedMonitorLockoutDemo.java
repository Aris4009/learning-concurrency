package org.example.ch7._2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: NestedMonitorLockoutDemo
 * @Description: 嵌套监视器锁死
 * @Date: 2022/3/30
 **/
@Slf4j
public class NestedMonitorLockoutDemo {

	private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

	private int processed;

	private int accepted;

	public synchronized void accept(String msg) throws InterruptedException {
		queue.put(msg);
		accepted++;
	}

	// bad code
	public synchronized void process() throws InterruptedException {
		// take操作一直循环，导致同步监视锁一直不会释放
		log.info(queue.take());
		processed++;
	}

	// good code
//	public void process() throws InterruptedException {
//		log.info(queue.take());
//		synchronized (this) {
//			processed++;
//		}
//	}

	public void start() {
		Thread t = new WorkThread();
		t.setName("workThread");
		t.start();
	}

	public synchronized int[] getStat() {
		return new int[] { accepted, processed };
	}

	class WorkThread extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					process();
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
					return;
				}
			}
		}
	}

	public static void main(String[] args) {
		NestedMonitorLockoutDemo demo = new NestedMonitorLockoutDemo();
		demo.start();
		for (int i = 0; i < 100000; i++) {
			try {
				demo.accept(String.valueOf(i));
				Thread.sleep(100L);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
