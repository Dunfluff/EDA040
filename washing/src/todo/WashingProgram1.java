package todo;

import done.AbstractWashingMachine;

public class WashingProgram1 extends WashingProgram {

	public WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void wash() throws InterruptedException {

		mainWash();

		rinse();

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));

		centrifuge();

		// unlock
		myMachine.setLock(false);
	}

	private void centrifuge() throws InterruptedException {
		// centrifuge 5 min
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));

		Thread.sleep(5 * 60 * 1000);
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
	}

	private void rinse() throws InterruptedException {
		// rinse 5 times a 2 min
		for (int i = 0; i < 5; i++) {
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.8));

			mailbox.doFetch(); // Wait for Ack

			// Set water regulation to idle => drain pump stops
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));

			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

			Thread.sleep(2 * 60 * 1000);

			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
			mailbox.doFetch(); // Wait for Ack
		}
	}

	private void mainWash() throws InterruptedException {
		// Lock Hatch
		myMachine.setLock(true);

		// Fill with water
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.8));

		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));

		// Heat water to 60
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

		Thread.sleep(30 * 60 * 1000);

		// drain
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));

		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));
	}

}
