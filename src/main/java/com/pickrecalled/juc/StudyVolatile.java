package com.pickrecalled.juc;

public class StudyVolatile {
	// volatile可以保证可见性，及时通知其它线程，主物理内存共享变量值已被修改
	public static void main(String[] args) {
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

class MyData {
	// 使用volatile修饰共享变量，使得各线程之间对number是可见性
	volatile int number = 0;

	public void addTo60() {
		this.number = 60;
	}
}