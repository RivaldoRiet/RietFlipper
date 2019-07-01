package RietFlipper;

import org.dreambot.api.utilities.Timer;

public class rietSlot {
	Timer t = new Timer();
	boolean timerstarted = false;
	public rietItem item = null;
	public rietSlot(rietItem item) {
		this.item = item;
	}
	
	public void stopTimer() {
		t.reset();
		this.timerstarted = true;
	}
}
