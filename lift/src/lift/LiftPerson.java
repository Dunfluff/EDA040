package lift;

import java.util.Random;

import se.lth.cs.realtime.semaphore.*;


public class LiftPerson extends Thread {
	private LiftMonitor monitor;

    public  LiftPerson(LiftMonitor monitor) {
        this.monitor = monitor;
    }

    private int randomFloor() {
        return (int) (Math.random() * 7);
    }

    public void run() {
        while (true) {
            int from = randomFloor();
            int to = randomFloor();

            while (from == to) {
                to = randomFloor();
            }

            try { Thread.sleep((int) (46000 * Math.random())); }
            catch (InterruptedException e) { e.printStackTrace(); }

            monitor.call(from, to);
        }
    }
}