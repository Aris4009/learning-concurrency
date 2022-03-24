package org.example.ch5._2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: BoundedBuffer
 * @Description: 使用条件变量实现一个有界队列
 * @Date: 2022/3/22
 **/
@Slf4j
public class BoundedBuffer<T> {

	private final Lock lock = new ReentrantLock();

	private final Condition notFull = lock.newCondition();

	private final Condition notEmpty = lock.newCondition();

	private final Object[] buffer = new Object[10];

	private int putIndex;

	private int takeIndex;

	private int count;

	public void put(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == buffer.length) {
				notFull.await();
			}
			buffer[putIndex] = t;
			putIndex++;
			if (putIndex == buffer.length) {
				putIndex = 0;
			}
			count++;
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public T take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();
			}
			T t = (T) buffer[takeIndex];
			takeIndex++;
			if (takeIndex == buffer.length) {
				takeIndex = 0;
			}
			count--;
			notFull.signal();
			return t;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		BoundedBuffer<String> boundedBuffer = new BoundedBuffer<>();
		Thread thread = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				try {
					boundedBuffer.put(String.valueOf(i));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Thread thread1 = new Thread(() -> {
			try {
				while (true) {
					log.info("{}", boundedBuffer.take());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start();
		thread1.start();
	}
}
