package todo;

import done.*;
import se.lth.cs.realtime.RTThread;

public class WashingController implements ButtonListener {
	// TODO: add suitable attributes
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private SpinController sc;
	private TemperatureController tc;
	private WaterController wc;
	private int previous = 0;
	private RTThread currentThread = new WashingProgram0(theMachine, theSpeed, tc, wc, sc);

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		// TODO: Create and start your controller threads here
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;

		tc = new TemperatureController(theMachine, theSpeed);
		wc = new WaterController(theMachine, theSpeed);
		sc = new SpinController(theMachine, theSpeed);

		tc.start();
		wc.start();
		sc.start();

	}

	public void processButton(int theButton){
		// TODO: implement this method
    	// Handle button presses (0, 1, 2, or 3). A button press
    	// corresponds to starting a new washing program. What should
    	// happen if there is already a running washing program?
    	if(theButton == 0 && currentThread.isAlive())currentThread.interrupt();
    	
    	if(!currentThread.isAlive()){
    	switch(theButton){
    	case 0 :
    		currentThread = new WashingProgram0(theMachine, theSpeed, tc, wc, sc);
    		break;
    	case 1 :
    		currentThread = new WashingProgram1(theMachine, theSpeed, tc, wc, sc);
    		break;
    	case 2:
    		currentThread = new WashingProgram2(theMachine, theSpeed, tc, wc, sc);
    		break;
    	case 3:
    		currentThread = new WashingProgram3(theMachine, theSpeed, tc, wc, sc);
    		break;
    	}
    	currentThread.start();
    	}
    }
}
