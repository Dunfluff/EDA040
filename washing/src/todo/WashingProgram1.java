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

		// Lock Hatch
		myMachine.setLock(true);
//		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
//		mailbox.doFetch();

		// Fill with water
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.8));
		
		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		
		// Heat water to 60
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60));
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		
		//SLEEEEEEP? FOR 30 MIN
		
		// drain
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
		
		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
	
		// rinse 5 times a 2 min
		for(int i = 0; i < 5; i++){
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.8));
			
			mailbox.doFetch(); // Wait for Ack

			// Set water regulation to idle => drain pump stops
			myWaterController.putEvent(new WaterEvent(this,
					WaterEvent.WATER_IDLE,
					0.0));
			
			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		//SLEEEEEEEP for 3 min
			mySpinController.putEvent(new SpinEvent(this,SpinEvent.SPIN_OFF));
			myWaterController.putEvent(new WaterEvent(this,WaterEvent.WATER_DRAIN, 0.0));
		}
		
		mailbox.doFetch(); // Wait for Ack

		// Set water regulation to idle => drain pump stops
		myWaterController.putEvent(new WaterEvent(this,
				WaterEvent.WATER_IDLE,
				0.0));
		
		// centrifuge 5 min
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		// unlock
		myMachine.setLock(false);
	}

}
