package RietFlipper;

import org.dreambot.api.utilities.Timer;

public class rietItem {
	int boughtprice;
	int soldprice;
	int flipbuyprice;
	int flipsellprice;
	int margin;
	int flipAmount;
	String name;
	int slot;
	boolean timerstarted = false;
	public StatusEnum status;
	Timer t = new Timer();
	public rietItem(String name) {
		this.name = name;
		this.boughtprice = 0;
		this.soldprice = 0;
		this.flipbuyprice = 0;
		this.flipsellprice = 0;
		this.margin = 0;
		this.flipAmount = 0;
		this.slot = -1;
		this.status = StatusEnum.Buying;
	}

	public enum StatusEnum {
		Buying,
	    Selling,
	    FlipBuy,
	    FlipSell;
	}
	
	
	public void stopTimer() {
		t.reset();
		this.timerstarted = true;
	}

}