package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine machine;
	private RTEvent currentEvent;
	private WashingProgram wp;
	private int currentAction = TemperatureEvent.TEMP_IDLE;
	private double targetTemp = 0;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
	}

	public void perform() {
		currentEvent = mailbox.tryFetch();
		if (currentEvent != null && currentEvent instanceof TemperatureEvent
				&& currentEvent.getSource() instanceof WashingProgram) {
			switch (((TemperatureEvent) currentEvent).getMode()) {
			case TemperatureEvent.TEMP_IDLE:
				currentAction = TemperatureEvent.TEMP_IDLE;
				machine.setHeating(false);
				break;
			case TemperatureEvent.TEMP_SET:
				currentAction = TemperatureEvent.TEMP_SET;
				targetTemp = ((TemperatureEvent) currentEvent).getTemperature();
				wp = ((WashingProgram) currentEvent.getSource());
			}
		}
		if (currentAction == TemperatureEvent.TEMP_SET && machine.getWaterLevel() > 0.1) {
			if (machine.getTemperature() < 1.9) {
				machine.setHeating(true);
			} else if (machine.getTemperature() > targetTemp) {
				machine.setHeating(false);
				if (wp != null) {
					wp.putEvent(new AckEvent(this));
					wp = null;
				}
			}
		}
	}
}