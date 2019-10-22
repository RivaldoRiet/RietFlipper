package RietFlipper;

public class OSBuddyItem {

    private String name;
    private final int id, buyAverage, buyQuantity, sellAverage,sellQuantity,overallAverage,overallQuantity;
    private boolean isMember = false;
    /**
     * @param id
     * @param name
     * @param buyAverage
     * @param buyQuantity
     * @param sellAverage
     * @param sellQuantity
     * @param overallAverage
     * @param overallQuantity
     * @param isMember
     */
    public OSBuddyItem(int id, String name, int buyAverage, int buyQuantity, int sellAverage, int sellQuantity, int overallAverage, int overallQuantity, boolean isMember) {
        this.name = name;
        this.id = id;
        this.buyAverage = buyAverage;
        this.buyQuantity = buyQuantity;
        this.sellAverage = sellAverage;
        this.sellQuantity = sellQuantity;
        this.overallAverage = overallAverage;
        this.overallQuantity = overallQuantity;
        this.isMember = isMember;
    }


    public boolean isMember() {
		return isMember;
	}


	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}


	public int getBuyAverage() {
        return buyAverage;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getSellAverage() {
        return sellAverage;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public int getOverallAverage() {
        return overallAverage;
    }

    public int getOverallQuantity() {
        return overallQuantity;
    }
}