package com.pickrecalled.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class StudyVolatile {

	@Test
	// 验证volatile不保证原子性
	public void test02() throws InterruptedException {
		MyData md = new MyData();
		for (int i = 1; i <= 20; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 1; j <= 1000; j++) {
						md.addPlusPlus();
						md.addAtomic();
					}
				}
			}, "volatile不保证原子性实验" + i).start();
		}

		// 需要等20个子线程执行完，再让主线程取得最终的结果值看是多少
		while (Thread.activeCount() > 2) {
			Thread.yield();
		}

		System.out.println(Thread.currentThread().getName() + "\t int type finally number value:" + md.number);
		System.out.println(Thread.currentThread().getName() + "\t atomicInteger type finally number value:" + md.atomicInteger);
	}

	@Test
	// volatile可以保证可见性，及时通知其它线程，主物理内存共享变量值已被修改
	public void test01() {
		// 资源类
		MyData md = new MyData();
		// 私线程更新值
		new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + "\t come in");
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			md.addTo60();
			System.out.println(Thread.currentThread().getName() + "\t update number value:" + md.number);
		}, "AAA").start();

		// 主线程
		while (md.number == 0) {
			// main 线程一直在这里等待循环，直到number的值不为零
		}

		// 任务
		System.out.println(Thread.currentThread().getName() + "\t mission is over value:" + md.number);
	}
}

/**
 * 资源类
 */
class MyData {
	// 使用volatile修饰共享变量，使得各线程之间对number是可见性
	volatile int number = 0;

	public void addTo60() {
		this.number = 60;
	}

	// 注意:此时number前面是加了volatile关键字修饰的,volatile不保证原子性
	public void addPlusPlus() {
		number++;
	}

	//保证原子性的int类型的加加
    volatile AtomicInteger atomicInteger = new AtomicInteger();

	public void addAtomic() {
		atomicInteger.getAndIncrement();
	}
}
