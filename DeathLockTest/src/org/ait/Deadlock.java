package org.ait;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This creates 3 threads that are in deadlock.
 */
public class Deadlock {
    private CyclicBarrier barrier = new CyclicBarrier(3);
    private Object a = new Object();
    private Object b = new Object();
    private Object c = new Object();
    public Deadlock() {
        // start 3 deadlocked threads 
        Thread d1 = new DeadlockThread1();
        Thread d2 = new DeadlockThread2();
        Thread d3 = new DeadlockThread3();
        d1.setDaemon(true); 
        d1.start();
        d2.setDaemon(true);
        d2.start();
        d3.setDaemon(true);
        d3.start();
    }

    class DeadlockThread1 extends Thread {
        public DeadlockThread1() {
            super("DeadlockedThread-1");
        }
        public void run() {
            A();
        }
        private void A() {
            synchronized (a) {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    System.exit(1);
                } catch (BrokenBarrierException e) {
                    System.exit(1);
                }
                B();
            }
        }
        private void B() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                System.exit(1);
            } catch (BrokenBarrierException e) {
                System.exit(1);
            }
            synchronized (b) {
                throw new RuntimeException("D1 should not reach here.");
            }
        }
    }

    class DeadlockThread2 extends Thread {
        public DeadlockThread2() {
            super("DeadlockedThread-2");
        }
        public void run() {
            B();
        }
        private void B() {
            synchronized (b) {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    System.exit(1);
                } catch (BrokenBarrierException e) {
                    System.exit(1);
                }
                C();
            }
        }
       private void C() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                System.exit(1);
            } catch (BrokenBarrierException e) {
                System.exit(1);
            }
            synchronized (c) {
                throw new RuntimeException("D2 should not reach here.");
            }
        }

    }

    class DeadlockThread3 extends Thread {
        public DeadlockThread3() {
            super("DeadlockedThread-3");
        }
        public void run() {
            C();
        }
        private void C() {
            synchronized (c) {
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    System.exit(1);
                } catch (BrokenBarrierException e) {
                    System.exit(1);
                }
                A();
            }
        }
        private void A() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                System.exit(1);
            } catch (BrokenBarrierException e) {
                System.exit(1);
            }
            synchronized (a) {
                throw new RuntimeException("D3 should not reach here.");
            }
        }
    }
}
