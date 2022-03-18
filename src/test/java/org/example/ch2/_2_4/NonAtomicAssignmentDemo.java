package org.example.ch2._2_4;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @Author: Aris
 * @ClassName: NonAtomicAssignmentDemo
 * @Description: long、double在32位虚拟机下，是非原子操作 运行结果如下
 *               C:\Users\Administrator\Desktop\test>java
 *               NonAtomicAssignmentDemo Unexpected data: 4294967295(0x
 *               ffffffff) C:\Users\Administrator\Desktop\test>java
 *               NonAtomicAssignmentDemo Unexpected data:
 *               -4294967296(0xffffffff00000000)
 *               C:\Users\Administrator\Desktop\test>java
 *               NonAtomicAssignmentDemo Unexpected data:
 *               -4294967296(0xffffffff00000000)
 *               C:\Users\Administrator\Desktop\test>java
 *               NonAtomicAssignmentDemo
 * 
 *               long类型会占用64位（8字节）的存储空间，一个线程在写低32位，另一个线程在写高32位
 *               volatile关键字可以是long/double变量的写操作具有原子性
 * @Date: 2022/3/17
 **/
public class NonAtomicAssignmentDemo implements Runnable {

	static volatile long value = 0;

	private final long valueToSet;

	public NonAtomicAssignmentDemo(long valueToSet) {
		this.valueToSet = valueToSet;
	}

	@Override
	public void run() {
		for (;;) {
			value = valueToSet;
		}
	}

	public static void main(String[] args) {
		Thread updateThread1 = new Thread(new NonAtomicAssignmentDemo(0L));
		Thread updateThread2 = new Thread(new NonAtomicAssignmentDemo(-1L));
		PrintStream ps = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {

			}
		});
		updateThread1.start();
		updateThread2.start();

		long snapshot;
		while ((snapshot = value) == 0 || snapshot == -1) {
			ps.println(snapshot);
		}
		System.err.printf("Unexpected data: %d(0x%16x)", snapshot, snapshot);
		ps.close();
		System.exit(0);
	}
}
