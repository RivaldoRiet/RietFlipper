package RietFlipper;

import org.dreambot.api.utilities.Timer;

public class rietSellSlot {
	Timer t = new Timer();
	boolean timerstarted = false;
	public rietItem item = null;
	public boolean binSellItem = false;
	public String binItemName = "";
	public int sellSlotNumber = -1;
	public rietSellSlot(int sellSlotNumber, String itemName) {
		this.sellSlotNumber = sellSlotNumber;
		this.binItemName = itemName;
	}
	
	public void stopTimer() {
		t.reset();
		this.timerstarted = true;
	}

}