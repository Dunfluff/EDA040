package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

//borde kunna g�ra alla set metoder och larm kollar i denna, och d�rmed g�ra den icke publik. Borde fortfarande vara statisk.
//beh�ver d�rmed inneh�lla input och output vid start.
public class Manager {
	private Semaphore semaphore;
	private boolean alive;
	private int time, alarmTime, alarmCount;
	private ClockInput input;
	private ClockOutput output;

	public Manager(ClockInput i, ClockOutput o) {
		alive = true;
		input = i;
		output = o;
		time = 0;
		alarmTime = 0;
		alarmCount = 0;
		semaphore = new MutexSem();
	}
	
    private void alarm() {
    	if((alarmTime == time) && input.getAlarmFlag()){
    		alarmCount = 20;
    	}
    	if(alarmCount > 0){
    		alarmCount -= 1;
    		output.doAlarm();
    	}
    }

    public void setAlarm(int newTime) {
    	semaphore.take();
    	alarmTime = newTime;
    	semaphore.give();
    }

    public void disableAlarm() {
    	semaphore.take();
    	alarmCount = 0;
    	semaphore.give();
    }

    public void setTime(int newTime) {
    	semaphore.take();
    	time = newTime;
    	semaphore.give();
    }

    public boolean isAlive() {
		return alive;
    }

    public void kill() {
    	semaphore.take();
    	alive = false;
    	semaphore.give();
    }

	public void tick() {
		semaphore.take();
        time ++;
        if (time % 100 > 59)
            time += 40;
        if (time % 10000 / 100 > 59)
            time += 4000;
        if (time > 235959)
            time -= 240000;

        output.showTime(time);
        alarm();
        semaphore.give();

	}


}
