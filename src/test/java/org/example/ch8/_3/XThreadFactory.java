package org.example.ch8._3;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: XThreadFactory
 * @Description:
 * @Date: 2022/3/30
 **/
@Slf4j
public class XThreadFactory implements ThreadFactory {

	private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

	private final String name;

	private final AtomicInteger threadNumber = new AtomicInteger(1);

	private XThreadFactory(Thread.UncaughtExceptionHandler uncaughtExceptionHandler, String name) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
		this.name = name;
	}

	public XThreadFactory() {
		this(new LogEx(), "thread");
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = doMakeThread(r);
		t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		t.setName(name + "-" + threadNumber.getAndIncrement());
		t.setDaemon(false);
		t.setPriority(Thread.NORM_PRIORITY);
		log.info("{} is created", t.getName());
		return t;
	}

	protected Thread doMakeThread(final Runnable r) {
		return new Thread(r) {
			@Override
			public String toString() {
				ThreadGroup threadGroup = this.getThreadGroup();
				String groupName;
				if (threadGroup == null) {
					groupName = "";
				} else {
					groupName = threadGroup.getName();
				}
				return this.getClass().getSimpleName() + "[" + this.getName() + "," + this.getId() + "," + groupName
						+ "]@" + this.hashCode();
			}
		};
	}

	static class LogEx implements Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			log.error("thread {} is terminated,msg:{}", t.getName(), e.getMessage());
		}
	}

	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				log.info("{} is running", Thread.currentThread());
			}
		};
		XThreadFactory xThreadFactory = new XThreadFactory();
		for (int i = 0; i < 5; i++) {
			xThreadFactory.newThread(runnable).start();
		}
	}
}
