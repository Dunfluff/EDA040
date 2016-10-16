package todo;

import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;
import se.lth.cs.realtime.event.*;

public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	private RTEvent currentEvent;
	private AbstractWashingMachine machine;
	private int currentAction = SpinEvent.SPIN_OFF;
	private int spinDir = AbstractWashingMachine.SPIN_LEFT;
	private int changeDirDelay = 60000;
	private int dirTimer = 0;
	private long lastUse = System.currentTimeMillis();

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
		machine = mach;
	}

	public void perform() {
		currentEvent = mailbox.tryFetch();
		if (currentEvent != null && currentEvent instanceof SpinEvent
				&& currentEvent.getSource() instanceof WashingProgram) {
			switch (((SpinEvent) currentEvent).getMode()) {
			case SpinEvent.SPIN_OFF:
				currentAction = SpinEvent.SPIN_OFF;
				machine.setSpin(AbstractWashingMachine.SPIN_OFF);
				break;
			case SpinEvent.SPIN_SLOW:
				currentAction = SpinEvent.SPIN_SLOW;
				spinDir = (spinDir == AbstractWashingMachine.SPIN_LEFT) ? AbstractWashingMachine.SPIN_RIGHT : AbstractWashingMachine.SPIN_LEFT;
				machine.setSpin(spinDir);
				lastUse = System.currentTimeMillis();
				dirTimer = 0;
				break;
			case SpinEvent.SPIN_FAST:
				currentAction = SpinEvent.SPIN_FAST;
				break;
			}
		}
		 if (currentAction == SpinEvent.SPIN_SLOW && machine.isLocked() && (dirTimer += System.currentTimeMillis() - lastUse) >= changeDirDelay) {
	            dirTimer -= changeDirDelay;
	            spinDir = (spinDir == AbstractWashingMachine.SPIN_LEFT) ? AbstractWashingMachine.SPIN_RIGHT : AbstractWashingMachine.SPIN_LEFT;
	            machine.setSpin(spinDir);
	            lastUse = System.currentTimeMillis();
	        } else if (currentAction == SpinEvent.SPIN_FAST && machine.getWaterLevel() <= 0d && machine.isLocked()) {
	            machine.setSpin(AbstractWashingMachine.SPIN_FAST);
	            currentAction = -1;
	        }
	}
}
