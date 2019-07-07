package RietFlipper;

import org.dreambot.api.utilities.Timer;

public class rietBuySlot {
	Timer t = new Timer();
	boolean timerstarted = false;
	public rietItem item = null;
	public rietBuySlot(rietItem item) {
		this.item = item;
	}
	
	public void stopTimer() {
		t.reset();
		this.timerstarted = true;
	}
}
