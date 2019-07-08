package RietFlipper;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.GrandExchangeItem;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.InventoryListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

@ScriptManifest(category = Category.MONEYMAKING, name = "new RietFlipper grand exchange flipper v1.0", author = "Riet/Rivaldo/Oh okay", version = 0)
public class RietFlipper extends AbstractScript implements InventoryListener {
    Area geArea = new Area(3160, 3482, 3172, 3495);
    Tile geTile = new Tile(3163, 3485);
    Timer t = new Timer();
    boolean moneySet = false;
    int flipmoney = 0;
    String status = "none";
    ArrayList < rietItem > riArr = new ArrayList < rietItem > ();
    ArrayList < rietBuySlot > rsArr = new ArrayList < rietBuySlot > ();
    ArrayList < rietSellSlot > rssArr = new ArrayList < rietSellSlot > ();
    ArrayList < rietUnsoldSlot > rsuArr = new ArrayList < rietUnsoldSlot > ();
    int moneyMade = 0;
    // rietItem[] riArr = new rietItem[] {
    //   new rietItem("Fly fishing rod"), new rietItem("Wizard hat"), new rietItem("Studded chaps (g)")
    //};

    @Override
    public void onPaint(Graphics graphics) {
    	super.onPaint(graphics);
        graphics.setColor(new Color(204, 187, 154));
        graphics.fillRect(0, 300, 500, 200);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Time ran: " + t.formatTime(), 8, 490);
        graphics.drawString("Profit: " + moneyMade + " (" + t.getHourlyRate(moneyMade) + ")", 8, 475);
        graphics.drawString("Status: " + this.status, 8, 460);
        
        ArrayList < rietItem > paintArr = new ArrayList < rietItem > ();
        
        for (int i = 0; i < riArr.size(); i++) {
            if (riArr.get(i).timerstarted) {
            	paintArr.add(riArr.get(i));
            }
        }
        for (int i = 0; i < paintArr.size(); i++) {
			graphics.drawString(paintArr.get(i).name + " - Margin: " + paintArr.get(i).margin + " - boughtprice: " + paintArr.get(i).boughtprice + " - soldprice: " + paintArr.get(i).soldprice, 8, 445 - 12 * i);
		}
        

        for (int i = 0; i < rsArr.size(); i++) {
            graphics.drawString("Slot " + rsArr.get(i).item.slot + ": " + rsArr.get(i).t.formatTime(), 8 * 15 * i, 340);
        }

        for (int i = 0; i < rssArr.size(); i++) {
            graphics.drawString("Slot " + rssArr.get(i).sellSlotNumber + ": " + rssArr.get(i).t.formatTime(), 8 * 15 * i, 325);
        }
        
    }

    @Override
    public void onStart() {
        this.status = "Starting up";
        
        this.riArr.add(new rietItem("Uncut ruby"));
        this.riArr.add(new rietItem("Ruby"));
        this.riArr.add(new rietItem("Diamond"));
        this.riArr.add(new rietItem("Uncut diamond"));


        this.riArr.add(new rietItem("Sapphire"));
        this.riArr.add(new rietItem("Uncut sapphire"));
        this.riArr.add(new rietItem("Emerald"));
        this.riArr.add(new rietItem("Uncut emerald"));

        this.riArr.add(new rietItem("Gold ore"));
        this.riArr.add(new rietItem("Maple longbow"));

        
        this.riArr.add(new rietItem("Gold amulet"));
        this.riArr.add(new rietItem("Gold bar"));
        this.riArr.add(new rietItem("Gold ring"));

        this.riArr.add(new rietItem("Gold necklace"));
        this.riArr.add(new rietItem("Gold amulet (u)"));

        this.riArr.add(new rietItem("Big bones"));

        this.riArr.add(new rietItem("Rune axe"));
        this.riArr.add(new rietItem("Rune scimitar"));
        this.riArr.add(new rietItem("Rune dagger"));
        this.riArr.add(new rietItem("Rune mace"));
      
    }

    @Override
    public int onLoop() {
        Inventory i = getInventory();
        //Player p = getLocalPlayer();
        //NPC clerk = getNpcs().closest("Grand Exchange Clerk");
        GrandExchange g = getGrandExchange();
        if (!geArea.contains(getLocalPlayer())) {
            this.status = "Walking to the ge";
            getWalking().walkExact(geTile);
        } else {
            if (!i.contains("Coins")) {
                if (getBank().isOpen()) {
                    this.status = "Checking bank for coins";
                    if (getBank().contains("Coins")) {
                        getBank().withdrawAll("Coins");
                        sleepUntil(() -> i.contains("Coins"), Calculations.random(3500, 5500));
                    }
                } else {
                    if (getBank().open(BankLocation.GRAND_EXCHANGE)) {
                        this.status = "Opening bank";
                        sleepUntil(() -> getBank().isOpen(), Calculations.random(5000, 8500));
                    }
                }
            } else {
                if (getBank().isOpen()) {
                    this.status = "Closing bank";
                    getBank().close();
                } else {
                    if (!g.isOpen()) {
                        this.status = "Opening GE";
                        g.open();
                        sleepUntil(() -> g.isOpen(), Calculations.random(5000, 8500));
                    } else {
                        if (g.isOpen()) {
                            checkSlots();
                            collectMoney();
                            
                            for (int j = 0; j < riArr.size(); j++) {
                            	sortByHighestMargin();
                                if (riArr.get(j).boughtprice == 0 || riArr.get(j).soldprice == 0) {
                                	if(!isItemCurrentlyInGE(riArr.get(j))) {
	                                    getBuyPrice(riArr.get(j));
	                                    riArr.get(j).margin = riArr.get(j).boughtprice - riArr.get(j).soldprice;
	
	
	                                    if (riArr.get(j).boughtprice != 0) {
	                                        riArr.get(j).flipbuyprice = riArr.get(j).soldprice + 1;
	                                    }
	
	                                    if (riArr.get(j).soldprice != 0) {
	                                        riArr.get(j).flipsellprice = riArr.get(j).boughtprice - 1;
	                                        riArr.get(j).profitmargin = riArr.get(j).flipbuyprice - riArr.get(j).flipsellprice;
	                                        //riArr.get(j).margin = riArr.get(j).flipbuyprice - riArr.get(j).flipsellprice;
	                                        if (riArr.get(j).margin == 0 || riArr.get(j).margin <= 2) {
	                                            this.riArr.remove(j);
	                                            break;
	                                        }
	                                    }
                                	}
                                }
                                
                                if (getInventory().get("Coins") != null) {
                                    flipmoney = (int)(getInventory().get("Coins").getAmount() * 0.50);
                                    if (flipmoney > 0 && riArr.get(j).flipbuyprice > 0) {
                                    	if(riArr.get(j).flipbuyprice < flipmoney && flipmoney / riArr.get(j).flipbuyprice > 1) {
                                            riArr.get(j).flipAmount = flipmoney / riArr.get(j).flipbuyprice;
                                            riArr.get(j).stopTimer();
                                    	}
                                    }
                                }
                                
                                if(allObjectsHaveMargins()) {
	                                flipItem(riArr.get(j));
	                                checkSlots();
	                                collectMoney();
                                }else {
                                	sortByHighestMargin();
                                }
                            }
                        }
                    }
                }
            }
        }
        
        this.status = "idle";
        return 1;
    }


    public void collectMoney() {
        GrandExchange g = getGrandExchange();
        for (GrandExchangeItem geItem: g.getItems()) {
            int currSlot = geItem.getSlot();
            if (g.isReadyToCollect(currSlot) && !g.isSellOpen() && !g.isBuyOpen()) {
                this.status = "Collecting items from slots";
               
                for (int i = 0; i < this.rssArr.size(); i++) {
                	if(this.rssArr.get(i).sellSlotNumber == currSlot) {
                		this.rssArr.remove(this.rssArr.get(i));
                	}
                }
                
                log("status shit: " + geItem.getStatus());
               /* for (int i = 0; i < this.rssArr.size(); i++) {
                	if(this.rssArr.get(i).sellSlotNumber == currSlot) {
                		this.rssArr.remove(this.rssArr.get(i));
                	}
                }
				}*/
                for (int i = 0; i < this.rsArr.size(); i++) {
                    if (this.rsArr.get(i).item.slot == currSlot) {
                        this.rsArr.remove(this.rsArr.get(i));
                    }
                }

        
                sleep(Calculations.random(500, 750));
                g.openSlotInterface(currSlot);
                sleep(Calculations.random(2000, 2500));
                WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
                WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
                
                WidgetChild col3 = getWidgets().getWidget(465).getChild(23).getChild(2);
                WidgetChild col4 = getWidgets().getWidget(465).getChild(23).getChild(3);
                
                
                WidgetChild checkmoney = getWidgets().getWidget(465).getChild(23).getChild(2);
                int checkmoneyid = checkmoney.getItemId();
                
                String buyText = x.getText();
                buyText = buyText.split("price of <col=ffb83f>")[1];
                buyText = buyText.split("<")[0];
                buyText = buyText.replace(",", "");
                // int collectedMoney = Integer.parseInt(buyText);
                
                log("col1: " + col3.getItemId() + " - col2: " + col4.getItemId());
                
                col1.interact();
                sleep(Calculations.random(500, 750));
                sleepUntil(() -> g.isGeneralOpen(), Calculations.random(500, 800));
                col2.interact();
                sleep(Calculations.random(500, 750));
                sleepUntil(() -> g.isGeneralOpen(), Calculations.random(500, 800));
                
                if(checkmoneyid != 995) {
                	//als we alleen geld hebben opgehaald mogen we het item verwijderen uit de array
                	
                	log("REEEEEE: " + checkmoney.getItem().getName() + " - "  + checkmoney.getItemId());
                	for (int i = 0; i < this.rsuArr.size(); i++) {
                    	if(this.rsuArr.get(i).binItemName == checkmoney.getItem().getName()) {
                    		log("Slotty: " + this.rsuArr.get(i).sellSlotNumber + " - name: " + this.rsuArr.get(i).binItemName);
                        	if (inventoryContainsGeItem()) {
                            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                                if (slotWeAreUsingToSell != -1) {
                                    if (g.addSellItem(this.rsuArr.get(i).binItemName)) {
                                    	this.status = "Selling item: " + this.rsuArr.get(i).binItemName;
                                    log("wtf1 - " + " - " + this.rsuArr.get(i).binItemName + this.rsuArr.get(i).sellSlotNumber);
                                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                                        sleep(Calculations.random(1000, 2500));
                                        if (g.setPrice(1)) {
                                        	log("we are definitly hereXXXX");
                                            if (g.setQuantity(getInventory().count(this.rsuArr.get(i).binItemName))) {
                                                sleep(Calculations.random(1000, 2500));
                                                if (g.confirm()) {
                                                	sleep(Calculations.random(1000, 2500));
                                                	this.rsuArr.remove(this.rsuArr.get(i));
                                                	for (int j = 0; j < this.rsuArr.size(); j++) {
                										if(this.rsuArr.get(i).binItemName.equals(this.riArr.get(j).name)) {
                											this.riArr.remove(this.riArr.get(j));
                										}
                									}
                                                	log("setting bool false");
                                                	this.rsuArr.remove(this.rsuArr.get(i));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    	}
                    }
                }
                	
              
                
                
            }else if(g.isSellOpen() || g.isBuyOpen()) {
            	WidgetChild goback = getWidgets().getWidget(465).getChild(4);
                goback.interact();
                sleep(Calculations.random(100, 250));
            }
        }
    }


    @Override
    public void onItemChange(Item[] items) {
        for (Item item: items) {
            for (int i = 0; i < this.riArr.size(); i++) {
                if (this.riArr.get(i).name.equals(item.getName()) && this.riArr.get(i).margin > 0) {
                    this.moneyMade += item.getAmount() * (this.riArr.get(i).profitmargin * -1);
                }
            }
        }
    }

    
    

    public void checkSlots() {
        GrandExchange g = getGrandExchange();

        
        for (int i = 0; i < this.rssArr.size(); i++) {
        	String minutesPassed = rssArr.get(i).t.formatTime().toString().split(":")[1];
            int minutes = Integer.parseInt(minutesPassed);
            if (minutes >= 10) {
            	//this.rsuArr.add(new rietUnsoldSlot(this.rssArr.get(i).sellSlotNumber, this.rssArr.get(i).binItemName));
                for (GrandExchangeItem geItem: g.getItems()) {
                    if(geItem.getName().equals(rssArr.get(i).binItemName)) {
                    	 this.rssArr.get(i).binItemName = geItem.getName();
                         this.rssArr.get(i).setBinSellItemTrue();
                    	log("55555555555555555: " + geItem.getStatus());
                    	log("debug2: " + rssArr.get(i).binItemName + " - " + rssArr.get(i).getSellBinItem());
                        if (geItem.getStatus().toString() == "SELL") {
                        	log("66666666666666");
                            this.status = "offer for sell slot " + geItem.getName() + " has expired";
                                log("slotnum = " + geItem.getSlot() + " - " + geItem.getName());
                                this.status = "Cancelling item";
                                if (geItem.getSlot() != -1) {
                                    if (g.cancelOffer(geItem.getSlot())) {
                                        sleep(Calculations.random(1000, 2500));
                                        if (g.cancelOffer(geItem.getSlot())) {
                                        WidgetChild goback = getWidgets().getWidget(465).getChild(4);
                                        goback.interact();
                                        sleep(Calculations.random(100, 250));
                                        sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                        this.rsuArr.add(new rietUnsoldSlot(rssArr.get(i).sellSlotNumber, rssArr.get(i).binItemName));
		                            }
	                            }
                            }
		            }
                }
		        }
		    }
		}
        
        for (int i = 0; i < this.rsuArr.size(); i++) {
        	log("Slotty: " + this.rsuArr.get(i).sellSlotNumber + " - name: " + this.rsuArr.get(i).binItemName);
        	if (inventoryContainsGeItem()) {
            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                if (slotWeAreUsingToSell != -1) {
                    if (g.addSellItem(this.rsuArr.get(i).binItemName)) {
                    	this.status = "Selling item: " + this.rsuArr.get(i).binItemName;
                    log("wtf1 - " + " - " + this.rsuArr.get(i).binItemName + this.rsuArr.get(i).sellSlotNumber);
                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                        sleep(Calculations.random(1000, 2500));
                        if (g.setPrice(1)) {
                        	log("we are definitly hereXXXX");
                            if (g.setQuantity(getInventory().count(this.rsuArr.get(i).binItemName))) {
                                sleep(Calculations.random(1000, 2500));
                                if (g.confirm()) {
                                	sleep(Calculations.random(1000, 2500));
                                	this.rsuArr.remove(this.rsuArr.get(i));
                                	for (int j = 0; j < this.rsuArr.size(); j++) {
										if(this.rsuArr.get(i).binItemName.equals(this.riArr.get(j).name)) {
											this.riArr.remove(this.riArr.get(j));
										}
									}
                                	log("setting bool false");
                                	this.rsuArr.remove(this.rsuArr.get(i));
                                }
                            }
                        }
                    }
                }
            }
          
        }
        
        
       

        
     
        
        for (int i = 0; i < this.rsArr.size(); i++) {
            String minutesPassed = rsArr.get(i).t.formatTime().toString().split(":")[1];
            int minutes = Integer.parseInt(minutesPassed);
            if (minutes >= 5) {
                for (GrandExchangeItem geItem: g.getItems()) {
                    if (geItem.getName().equals(this.rsArr.get(i).item.name)) {
                        if (geItem.getStatus().toString() == "BUY") {
                            this.status = "offer for buy slot " + geItem.getName() + " has expired";
                            int slotnum = geItem.getSlot();
                            if (geItem.getTransferredAmount() == 0) {
                                    this.status = "Detected that item: " + geItem.getName() + " is not buying.. skipping";
                                    if (g.cancelOffer(geItem.getSlot())) {
                                        sleep(Calculations.random(1000, 2500));
                                        if (g.cancelOffer(geItem.getSlot())) {
                                        WidgetChild goback = getWidgets().getWidget(465).getChild(4);
                                        goback.interact();
                                        sleep(Calculations.random(100, 250));
                                        sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                        this.rsArr.remove(geItem.getSlot());
                                        this.riArr.remove(this.riArr.get(i));
                                    }
                                    }
                            } else {
                                log("item: " + geItem.getName() + " - " + slotnum);
                                if (slotnum != -1) {
                                	this.status = "Cancelling item";
                                    if (g.cancelOffer(slotnum)) {
                                        sleep(Calculations.random(1000, 2500));
                                        if (g.cancelOffer(geItem.getSlot())) {
                                        WidgetChild goback = getWidgets().getWidget(465).getChild(4);
                                        goback.interact();
                                        sleep(Calculations.random(100, 250));
                                        sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                    }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //remove double slots
        ArrayList < String > count = new ArrayList < String > ();
        for (int i = 0; i < g.getItems().length; i++) {
            if (g.getItems()[i] != null && g.getItems()[i].getName() != null && g.getItems()[i].getName() != "null" && g.getItems()[i].getStatus().toString() == "SELL") {
                count.add(g.getItems()[i].getName());
            }
        }

        ArrayList < Integer > removeSlots = new ArrayList < Integer > ();
        for (int i = 0; i < g.getItems().length; i++) {
            if (g.getItems()[i] != null && g.getItems()[i].getName() != null && g.getItems()[i].getName() != "null") {
                int occurrences = Collections.frequency(count, g.getItems()[i].getName());
                if (occurrences > 1) {
                    removeSlots.add(g.getItems()[i].getSlot());
                }
            }
        }


        for (int i = 0; i < removeSlots.size(); i++) {
            if (g.isSlotEnabled(removeSlots.get(i))) {
                this.status = "Removing double slots";
                if (g.cancelOffer(removeSlots.get(i))) {
                    sleep(Calculations.random(1000, 2500));
                    WidgetChild goback = getWidgets().getWidget(465).getChild(4);
                    goback.interact();
                    sleep(Calculations.random(100, 250));
                    sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                    if (g.cancelOffer(removeSlots.get(i))) {
                        sleep(Calculations.random(100, 250));
                        if (g.cancelOffer(removeSlots.get(i))) {
                        goback = getWidgets().getWidget(465).getChild(4);
                        goback.interact();
                        sleep(Calculations.random(100, 250));
                        sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                    }
                    }
                }
            }
        }



    }

    
    public void sortByHighestMargin() {
    	Collections.sort(this.riArr, new Comparator<rietItem>() {
            @Override public int compare(rietItem p1, rietItem p2) {
                return p2.margin - p1.margin; // Descending
            }
    	});	
    }

    public boolean allObjectsHaveMargins() {
    	for (int i = 0; i < this.riArr.size(); i++) {
			if(this.riArr.get(i).margin == 0 && !this.riArr.get(i).hasBeenReset) {
				return false;
			}
		}
    	return true;
    }

    public int getSlotByItem(String itemName) {
        for (int i = 0; i < this.riArr.size(); i++) {
            if (this.riArr.get(i).name.equals(itemName)) {
                return this.riArr.get(i).slot;
            }

        }
        return -1;
    }

    public void flipItem(rietItem item) {
        GrandExchange g = getGrandExchange();
        
        if (item.boughtprice > 0 && item.soldprice > 0) {
            if (inventoryContainsGeItem()) {
            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                if (slotWeAreUsingToSell != -1 && item.flipsellprice > 1) {
                    
                    if (g.addSellItem(item.name)) {
                    	log("WAT DE KENKER");
                    	this.status = "Selling item: " + item.name;
                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                        sleep(Calculations.random(1000, 2500));
                        if (g.setPrice(item.flipsellprice)) {
                        	log("we are definitly here@");
                            if (g.setQuantity(getInventory().count(item.name))) {
                                sleep(Calculations.random(1000, 2500));
                                if (g.confirm()) {
                                	item.slot = slotWeAreUsingToSell;
                                	this.rssArr.add(new rietSellSlot(slotWeAreUsingToSell, item.name));
                                    sleep(Calculations.random(1000, 2500));
                                    sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                    for (int i = 0; i < this.riArr.size(); i++) {
                                        this.riArr.get(i).resetFields(item.name);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!inventoryContainsGeItem() && g.getFirstOpenSlot() != -1 && !isItemCurrentlyInGE(item)) {
                item.slot = g.getFirstOpenSlot();
                if (g.openBuyScreen(item.slot)) {
                    this.status = "Buying item: " + item.name;
                    sleepUntil(() -> g.isBuyOpen(), Calculations.random(5000, 8500));
                    g.searchItem(item.name);
                    sleep(Calculations.random(100, 250));
                    g.addBuyItem(item.name);
                    sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    g.buyItem(item.name, item.flipAmount, item.flipbuyprice);
                    sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
                    sleep(Calculations.random(1000, 2500));
                    if (g.slotContainsItem(item.slot)) {
                        this.rsArr.add(new rietBuySlot(item));
                    }
                }
            }
        }

    }
    
  

    public boolean inventoryContainsGeItem() {
        for (int i = 0; i < this.riArr.size(); i++) {
            if (getInventory().contains(this.riArr.get(i).name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isItemCurrentlyInGE(rietItem item) {
        GrandExchange g = getGrandExchange();

        if (!g.isGeneralOpen()) {
            if (g.open()) {
                sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
            }
        }


        for (GrandExchangeItem geItem: g.getItems()) {
            if (geItem.getName().equals(item.name)) {
                return true;
            }
        }
        return false;
    }

    public boolean allSlotsFull() {
        GrandExchange g = getGrandExchange();
        for (GrandExchangeItem geItem: g.getItems()) {
            if (geItem.getStatus().toString() == "EMPTY") {
                return false;
            }
        }
        return true;
    }
    
    public boolean shouldSellItemForBinPrice(rietItem item) {
    	for (int i = 0; i < this.rssArr.size(); i++) {
			if(this.rssArr.get(i).binItemName.equals(item.name)) {
				return true;
			}
		}
    	
    	return false;
    }

    public void getBuyPrice(rietItem item) {
        GrandExchange g = getGrandExchange();
        if (g.isGeneralOpen() && !inventoryContainsGeItem() && !g.isReadyToCollect()) {
            if (!getInventory().contains(item.name) && g.getFirstOpenSlot() != -1) {
                item.slot = g.getFirstOpenSlot();
                if (g.openBuyScreen(item.slot)) {
                    this.status = "Getting buy price for flip of item: " + item.name;
                    sleepUntil(() -> g.isBuyOpen(), Calculations.random(5000, 8500));
                    g.searchItem(item.name);
                    sleep(Calculations.random(100, 250));
                    g.addBuyItem(item.name);
                    sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    g.buyItem(item.name, 1, g.getCurrentPrice() * 2);
                    sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    if (g.slotContainsItem(item.slot)) {
                        g.openSlotInterface(item.slot);
                        sleep(Calculations.random(1000, 2500));
                        WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
                        WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                        WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
                        String buyText = x.getText();
                        buyText = buyText.split("price of <col=ffb83f>")[1];
                        buyText = buyText.split("<")[0];
                        buyText = buyText.replace(",", "");
                        int boughtPrice = Integer.parseInt(buyText);

                        if (boughtPrice == 0 || boughtPrice < 3) {
                           if(g.cancelOffer(item.slot)) {
                            sleep(Calculations.random(1000, 2500));
                            col1.interact();
                            sleep(Calculations.random(1000, 2500));
                            this.riArr.remove(item);
                            sleep(Calculations.random(100, 250));
                            sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                           }
                        } else {
                            item.boughtprice = boughtPrice;
                            col1.interact();
                            col2.interact();
                            sleep(Calculations.random(100, 250));
                            sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                            
                        }
                    }
                }
            }
             }
            if (g.getFirstOpenSlot() != -1 && !g.isBuyOpen() && item.soldprice == 0 && !shouldSellItemForBinPrice(item) && getInventory().count(item.name) == 1) {
                //if(getInventory().get(item.name).getAmount() == 1) {
            	log("ZZZZZZZZZZZZ WE ZIJN HIER KANKER2");
                this.status = "Getting sell price for item: " + item.name;
                if (g.addSellItem(item.name)) {
                    sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                    sleep(Calculations.random(1000, 2500));
                    if (g.setPrice(1)) {
                    	log("ZZZZZZZZZZZZ WE ZIJN HIER KANKER");
                        sleep(Calculations.random(1000, 2500));
                        if (g.confirm()) {
                            sleep(Calculations.random(1000, 2500));
                            sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                            if (g.openSlotInterface(item.slot)) {
                                WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
                                WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                                WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
                                String soldText = x.getText();
                                soldText = soldText.split("price of <col=ffb83f>")[1];
                                soldText = soldText.split("<")[0];
                                soldText = soldText.replace(",", "");
                                int soldprice = Integer.parseInt(soldText);
                                item.soldprice = soldprice;
                                col1.interact();
                                col2.interact();
                            }
                        }
                    }
                    // }
                }
            }

       
    }

}