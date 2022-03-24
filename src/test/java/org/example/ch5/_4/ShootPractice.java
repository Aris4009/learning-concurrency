package org.example.ch5._4;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: ShootPractice
 * @Description: 使用CyclicBarrier模拟士兵打把。有N个士兵，每次上一排。一排士兵同时射击，同时撤离并替换下一批士兵。
 * @Date: 2022/3/23
 **/
@Slf4j
public class ShootPractice {

	final Soldier[][] soldiers;

	final int N;

	final int lineCount;

	final AtomicInteger nextLine = new AtomicInteger(1);

	final CyclicBarrier shiftBarrier;

	final CyclicBarrier startBarrier;

	final Random random = new Random();

	public ShootPractice(int N, int lineCount) {
		this.N = N;
		this.lineCount = lineCount;
		this.soldiers = new Soldier[lineCount][N];
		for (int i = 0; i < lineCount; i++) {
			for (int j = 0; j < N; j++) {
				this.soldiers[i][j] = new Soldier(String.valueOf(i) + "-" + String.valueOf(j));
			}
		}
		this.shiftBarrier = new CyclicBarrier(N, () -> {
			int line = nextLine.getAndIncrement();
			if (line < lineCount) {
				log.info("next turn is {}", line++);
			} else {
				log.info("last turn is finished");
			}
		});
		this.startBarrier = new CyclicBarrier(N, () -> {
			log.info("current turn is ready {}", nextLine.get());
		});
	}

	class Soldier {

		private final String name;

		public Soldier(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	interface IShoot {
		void shoot();
	}

	class Shoot implements IShoot {

		private final Soldier soldier;

		public Shoot(Soldier soldier) {
			this.soldier = soldier;
		}

		@Override
		public void shoot() {
			log.info("{} shoot {}", soldier.getName(), random.nextInt(10));
		}
	}

	class ShootingThread extends Thread {

		private final int index;

		public ShootingThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			try {
				Soldier soldier;
				int line;
				while ((line = nextLine.get()) <= lineCount) {
					soldier = soldiers[line - 1][index];
					startBarrier.await();
					IShoot shoot = new Shoot(soldier);
					shoot.shoot();
					shiftBarrier.await();
				}
			} catch (Exception e) {

			}
		}
	}

	public void start() throws InterruptedException {
		Thread[] threads = new Thread[N];
		for (int i = 0; i < N; i++) {
			threads[i] = new ShootingThread(i);
			threads[i].start();
		}
		for (Thread t : threads) {
			t.join();
		}
		log.info("finished");
	}

	public static void main(String[] args) throws InterruptedException {
		ShootPractice shootPractice = new ShootPractice(3, 6);
		shootPractice.start();
	}
}
