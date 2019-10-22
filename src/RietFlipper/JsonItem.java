package RietFlipper;


public class JsonItem {
    private int id, sellingPrice, buyAverage, buyQuanity, sellAverage, sellQuanity,
    overallAverage, overallQuanity;
    private String name;
    private boolean members;

    public JsonItem(int id, int sellingPrice, int buyAverage, int buyQuanity, int sellAverage, int sellQuanity, int overallAverage, int overallQuanity, String name, boolean members) {
        this.id = id;
        this.sellingPrice = sellingPrice;
        this.buyAverage = buyAverage;
        this.buyQuanity = buyQuanity;
        this.sellAverage = sellAverage;
        this.sellQuanity = sellQuanity;
        this.overallAverage = overallAverage;
        this.overallQuanity = overallQuanity;
        this.name = name;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getBuyAverage() {
        return buyAverage;
    }

    public void setBuyAverage(int buyAverage) {
        this.buyAverage = buyAverage;
    }

    public int getBuyQuanity() {
        return buyQuanity;
    }

    public void setBuyQuanity(int buyQuanity) {
        this.buyQuanity = buyQuanity;
    }

    public int getSellAverage() {
        return sellAverage;
    }

    public void setSellAverage(int sellAverage) {
        this.sellAverage = sellAverage;
    }

    public int getSellQuanity() {
        return sellQuanity;
    }

    public void setSellQuanity(int sellQuanity) {
        this.sellQuanity = sellQuanity;
    }

    public int getOverallAverage() {
        return overallAverage;
    }

    public void setOverallAverage(int overallAverage) {
        this.overallAverage = overallAverage;
    }

    public int getOverallQuanity() {
        return overallQuanity;
    }

    public void setOverallQuanity(int overallQuanity) {
        this.overallQuanity = overallQuanity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMembers() {
        return members;
    }

    public void setMembers(boolean members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return id + "/" + name + " : Selling price: " + sellingPrice + " Buy average: " + buyAverage
                + " Buy quantity: " + buyQuanity + " Sell average: " + sellAverage + "" +
                " Sell quantity: " + sellQuanity + " Overall average: " + overallAverage +
                " Overall quanity: " + overallQuanity + " isMembers: " + members;
    }
}