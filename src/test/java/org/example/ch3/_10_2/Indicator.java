package org.example.ch3._10_2;

import java.util.concurrent.atomic.AtomicLong;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: Indicator
 * @Description:
 * @Date: 2022/3/21
 **/
@Slf4j
public class Indicator {

	private Indicator() {
	}

	private static final Indicator INSTANCE = new Indicator();

	// 请求总数
	private final AtomicLong requestCount = new AtomicLong(0L);

	// 成功总数
	private final AtomicLong successCount = new AtomicLong(0L);

	// 失败总数
	private final AtomicLong failureCount = new AtomicLong(0L);

	public static Indicator getInstance() {
		return INSTANCE;
	}

	public void newRequestReceived() {
		requestCount.incrementAndGet();
	}

	public void newRequestProcessed() {
		successCount.incrementAndGet();
	}

	public void requestProcessedFailed() {
		failureCount.incrementAndGet();
	}

	public long getRequestCount() {
		return requestCount.get();
	}

	public long getSuccessCount() {
		return successCount.get();
	}

	public long getFailureCount() {
		return failureCount.get();
	}

	public void reset() {
		requestCount.set(0L);
		successCount.set(0L);
		failureCount.set(0L);
	}

	static class MyRunnable implements Runnable {

		private final Indicator indicator;

		public MyRunnable(Indicator indicator) {
			this.indicator = indicator;
		}

		@SneakyThrows
		@Override
		public void run() {
			synchronized (indicator) {
				indicator.newRequestReceived();
				long requestCount = indicator.getRequestCount();
				if (requestCount % 2 == 0) {
					indicator.newRequestProcessed();
				} else {
					indicator.requestProcessedFailed();
				}
				log.info("count:{},success:{},failed:{}", indicator.getRequestCount(), indicator.getSuccessCount(),
						indicator.getFailureCount());
			}
		}
	}

	public static void main(String[] args) {
		int num = 20;
		Runnable runnable = new MyRunnable(Indicator.getInstance());
		for (int i = 0; i < num; i++) {
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}
}
