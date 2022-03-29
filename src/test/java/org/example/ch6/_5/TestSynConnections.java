package org.example.ch6._5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestSynConnections
 * @Description: 装饰器返回的同步集合，在使用迭代器时，需要实现同步机制
 * @Date: 2022/3/29
 **/
@Slf4j
public class TestSynConnections {

	/**
	 * 未使用同步机制
	 */
	static class NonIterator {

		private final List<Integer> list;

		public NonIterator(List<Integer> list) {
			this.list = Collections.synchronizedList(list);
		}

		public void dump() {
			Iterator<Integer> iterator = list.listIterator();
			while (iterator.hasNext()) {
				log.info("{}", iterator.next());
				iterator.remove();
			}
		}
	}

	static class ThreadSafeIterator {

		private final List<Integer> list;

		public ThreadSafeIterator(List<Integer> list) {
			this.list = Collections.synchronizedList(list);
		}

		public void dump() {
			Iterator<Integer> iterator = list.listIterator();
			synchronized (list) {
				while (iterator.hasNext()) {
					log.info("{}", iterator.next());
					iterator.remove();
				}
			}
		}
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}

		NonIterator nonIterator = new NonIterator(list);
		ThreadSafeIterator threadSafeIterator = new ThreadSafeIterator(list);
		int num = 4;
		for (int i = 0; i < num; i++) {
//			Thread thread = new Thread(nonIterator::dump);
			Thread thread = new Thread(threadSafeIterator::dump);
			thread.start();
		}
	}
}
