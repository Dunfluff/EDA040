package todo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TThread extends Thread {

	private Manager man;
	private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	private int test = 0;

	public TThread(Manager man) {
		this.man = man;
	}

	@Override
	public void run() {
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(!man.isAlive())
					return;
				man.tick();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
}
