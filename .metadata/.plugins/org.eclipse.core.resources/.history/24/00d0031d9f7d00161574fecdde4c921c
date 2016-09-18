package done;

import se.lth.cs.realtime.semaphore.Semaphore;

public class TThread extends Thread{
	Semaphore sem;
	String string;

	public TThread(Semaphore sem, SharedData sD){
		this.sem = sem;
	}
	public void addSecond(){
		//stuff
	}
	public void run(){
		while(true){
			sem.take();
			addSecond();
			sem.give();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
