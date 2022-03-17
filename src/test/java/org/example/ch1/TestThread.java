package org.example.ch1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestThread
 * @Description:
 * @Date: 2022/3/15
 **/
@DisplayName("线程API测试")
@Slf4j
class TestThread {

	@Test
	@DisplayName("创建线程")
	void extendThread() {
		log.info("main state {}", Thread.currentThread().getState());
		Thread myThread = new MyThread();
		myThread.setName("myThread");
		log.info("MyThread state {}", myThread.getState());
		myThread.start();

		Thread myRunnable = new Thread(new MyRunnable(), "myRunnable");
		myRunnable.start();

		log.info("ExtendThread is running,the thread name is {}, the thread id is {}", Thread.currentThread().getName(),
				Thread.currentThread().getId());
	}

	/**
	 * 继承Thread类，实现run方法
	 */
	static class MyThread extends Thread {
		@Override
		public void run() {
			log.info("MyThread state {}", this.getState());
			log.info("MyThread is running,the thread name is {}, the thread id is {}", Thread.currentThread().getName(),
					Thread.currentThread().getId());
		}
	}

	/**
	 * 实现Runnable接口
	 */
	static class MyRunnable implements Runnable {
		@Override
		public void run() {
			log.info("MyRunnable is running,the thread name is {}, the thread id is {}",
					Thread.currentThread().getName(), Thread.currentThread().getId());
		}
	}
}
