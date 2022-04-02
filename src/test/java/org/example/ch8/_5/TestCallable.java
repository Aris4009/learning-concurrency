package org.example.ch8._5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: TestCallable
 * @Description:
 * @Date: 2022/4/2
 **/
@Slf4j
public class TestCallable {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		RunnableFuture<String> runnableFuture = new FutureTask<>(() -> Thread.currentThread().getName());
		Thread thread = new Thread(runnableFuture);
		thread.start();
//		String s = runnableFuture.get();
		log.info("xxxx");
//		log.info(s);
	}
}
