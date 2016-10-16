package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class WaterController extends PeriodicThread {
	private AbstractWashingMachine machine;
	private RTEvent currentEvent;
	private WashingProgram wp;
	private int currentAction = WaterEvent.WATER_IDLE;
	private double targetLevel = 0;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
	}

	public void perform() {
		// TODO: check if there are any messages in the mailbox (tryFetch ).
		// Take action depending on the last received message.
		currentEvent = mailbox.tryFetch();
		if (currentEvent != null && currentEvent instanceof WaterEvent
				&& currentEvent.getSource() instanceof WashingProgram) {
			switch (((WaterEvent) currentEvent).getMode()) {
			case WaterEvent.WATER_IDLE:
				currentAction = WaterEvent.WATER_IDLE;
				machine.setDrain(false);
				machine.setFill(false);
				break;
			case WaterEvent.WATER_DRAIN:
				currentAction = WaterEvent.WATER_DRAIN;
				targetLevel = ((WaterEvent) currentEvent).getLevel();
				machine.setFill(false);
				machine.setDrain(true);
				wp = ((WashingProgram) currentEvent.getSource());
			case WaterEvent.WATER_FILL:
				currentAction = WaterEvent.WATER_FILL;
				targetLevel = ((WaterEvent) currentEvent).getLevel();
				wp = ((WashingProgram) currentEvent.getSource());
				if (machine.isLocked()) {
					machine.setFill(true);
					machine.setDrain(false);
				} else {
					System.err.println("Fill water command failed. Machine not locked.");
				}
			}
			if (wp != null && ((currentAction == WaterEvent.WATER_FILL && machine.getWaterLevel() >= targetLevel)
					|| (currentAction == WaterEvent.WATER_DRAIN && machine.getWaterLevel() <= targetLevel))) {
				wp.putEvent(new AckEvent(this));
				wp = null;
			}
		}
	}
}