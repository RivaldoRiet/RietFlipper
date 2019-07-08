package RietFlipper;

import org.dreambot.api.utilities.Timer;


public class rietUnsoldSlot {
		Timer t = new Timer();
		boolean timerstarted = false;
		public rietItem item = null;
		public boolean binSellItem = false;
		public String binItemName = "";
		public int sellSlotNumber = -1;
		public rietUnsoldSlot(int sellSlotNumber, String itemName) {
			this.sellSlotNumber = sellSlotNumber;
			this.binItemName = itemName;
		}
}
