package org.ait;


public class DeathLock2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				while (true) {
					method1();
				}
			}
		};

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				while (true) {
					method2();
				}
			}
		};

		new Thread(r1).start();
		new Thread(r2).start();		
	}

	public static void method1() {
		synchronized (Integer.class) {
			System.out.println("Integer method1");

			synchronized (String.class) {
				System.out.println("String method1");
			}
		}
	}

	public static void method2() {
		synchronized (String.class) {
			System.out.println("String method2");
			synchronized (Integer.class) {
				System.out.println("Integer method2");
			}
		}

	}

}
