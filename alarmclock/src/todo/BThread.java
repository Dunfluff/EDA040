package todo;

import done.ClockInput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class BThread extends Thread {

	private ClockInput input;
	private Semaphore inputSemaphore;
	private Manager man;
	private int setTime = -1;

	public BThread(ClockInput i, Manager m) {
		input = i;
		inputSemaphore = input.getSemaphoreInstance();
		man = m;
	}

	@Override
	public void run() {
		while (man.isAlive()) {
			inputSemaphore.take();
			switch (input.getChoice()) {
			case ClockInput.SET_TIME:
				setTime = input.getValue();
				break;
			case ClockInput.SET_ALARM:
				man.setAlarm(input.getValue());
				setTime = -1;
				break;
			case ClockInput.SHOW_TIME:
				if (setTime > -1) {
					man.setTime(setTime);
					setTime = -1;
				}
				man.disableAlarm();
				break;
			}
		}
	}
}
