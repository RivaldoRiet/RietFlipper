package RietFlipper;

public class rietItem {
	int boughtprice;
	int soldprice;
	int margin;
	String name;
	int slot;
	public rietItem(String name) {
		this.name = name;
		this.boughtprice = 0;
		this.soldprice = 0;
		this.margin = 0;
		this.slot = -1;
	}

}
