package RietFlipper;

import org.dreambot.api.utilities.Timer;

public class rietItem {
	public int boughtprice;
	public int soldprice;
	public int flipbuyprice;
	public int flipsellprice;
	public int margin;
	public int profitmargin;
	public int flipAmount;
	public String name;
	public int slot;
	public boolean time_out = false;
	public boolean timerstarted = false;
	public StatusEnum status;
	public boolean hasBeenReset = false;
	public Timer t = new Timer();
	public boolean itemWasNotBuying = false;
	public rietItem(String name) {
		this.name = name;
		this.boughtprice = 0;
		this.soldprice = 0;
		this.flipbuyprice = 0;
		this.flipsellprice = 0;
		this.margin = 0;
		this.profitmargin = 0;
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
	
	public void startTimeout() {
		this.time_out = true;
		t.reset();
	}
	
	public void resetFields(String itemName) {
		if(itemName.equals(this.name)) {
		this.boughtprice = 0;
		this.soldprice = 0;
		this.flipbuyprice = 0;
		this.flipsellprice = 0;
		this.margin = 0;
		this.profitmargin = 0;
		this.flipAmount = 0;
		this.slot = -1;
		this.hasBeenReset = true;
		}
	}
	
	public void stopTimer() {
		t.reset();
		this.timerstarted = true;
	}

}
