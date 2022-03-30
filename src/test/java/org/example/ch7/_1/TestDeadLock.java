package org.example.ch7._1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestDeadLock
 * @Description: 死锁
 * @Date: 2022/3/29
 **/
@Slf4j
public class TestDeadLock {

	static class DeadLock {

		private final Object lock1 = new Object();

		private final Object lock2 = new Object();

		private final Lock lock3 = new ReentrantLock();

		private final Lock lock4 = new ReentrantLock();

		public void test1() {
			synchronized (lock1) {
				log.info("get lock1");
				synchronized (lock2) {
					log.info("get lock2");
				}
			}
		}

		public void test2() {
			synchronized (lock2) {
				log.info("get lock2");
				synchronized (lock1) {
					log.info("get lock1");
				}
			}
		}

		public void test3() {
			lock3.lock();
			log.info("get lock3");
			try {
				lock4.lock();
				log.info("get lock4");
			} finally {
				lock4.unlock();
				lock3.unlock();
			}
		}

		public void test4() {
			lock4.lock();
			log.info("get lock4");
			try {
				lock3.lock();
				log.info("get lock3");
			} finally {
				lock3.unlock();
				lock4.unlock();
			}
		}
	}

	public static void main(String[] args) {
		DeadLock deadLock = new DeadLock();
//		Thread thread1 = new Thread(deadLock::test1);
//		Thread thread2 = new Thread(deadLock::test2);
//		thread1.start();
//		thread2.start();

		Thread thread3 = new Thread(deadLock::test3);
		Thread thread4 = new Thread(deadLock::test4);
		thread3.start();
		thread4.start();
	}
}
