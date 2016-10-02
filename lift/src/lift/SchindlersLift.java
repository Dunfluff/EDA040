package lift;

public class SchindlersLift {
    public static void main(String[] args) {
        int personCount = 20;

        LiftView liftView = new LiftView();
        LiftMonitor monitor = new LiftMonitor(liftView);


        for (int i = 0; i < personCount; i++) {
        	new LiftPerson(monitor).start();
        }

        new LiftLift(monitor, liftView).start();
    }
}