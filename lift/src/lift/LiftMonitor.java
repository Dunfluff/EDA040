package lift;

import se.lth.cs.realtime.semaphore.Semaphore;

public class LiftMonitor {
	private final static int FLOORS = 7;
	private int here = 0;
	private int there = 0;
	private int load = 0;
	private int direction = 1;
	private int[] waitEntry = new int[FLOORS];
	private int[] waitExit = new int[] { -1, -1, -1, -1 };
	private LiftView liftView;

	public LiftMonitor(LiftView liftView) {
		this.liftView = liftView;
	}

	public synchronized int move() {
		if (here == FLOORS - 1) {
			direction = -1;
		} else if (here == 0) {
			direction = 1;
		}
		there += direction;
		return there;
	}

	private synchronized int waitingList() {
		int waitList = 0;

		for (int count : waitEntry)
			waitList += count;

		return waitList;

	}

	public synchronized int stop() {
		here = there;
		notifyAll();
		

		while ((load == 0 && waitingList() == 0) || (waitEntry[here] > 0 && load < 4) || quitters()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return here;
	}

	public synchronized void call(int from, int to) {
		waitEntry[from]++;
		liftView.drawLevel(from, waitEntry[from]);
		notifyAll();

		while(here != there || from != here || load == 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int mySpot = nextSpot();
		waitExit[mySpot] = to;
		load++;
		waitEntry[from]--;
		liftView.drawLevel(here, waitEntry[from]);
		liftView.drawLift(here, load);
		notifyAll();

		while (here != there || here != to) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitExit[mySpot] = -1;
		load--;
		liftView.drawLift(here, load);
		notifyAll();

	}

	private boolean quitters() {
		for (int i : waitExit) {
			if (i == here) {
				return true;
			}
		}
		return false;
	}

	private int nextSpot() {
		int index = 0;
		for (int i : waitExit) {
			if (i < 0) {
				return index;
			}
			index++;
		}
		return -1;
	}

}