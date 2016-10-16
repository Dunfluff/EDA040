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

		//Lock Hatch
		
	}

}
