package RietFlipper;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

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
public class RietFlipper extends AbstractScript implements InventoryListener{
    Area geArea = new Area(3160, 3482, 3172, 3495);
    Tile geTile = new Tile(3163, 3485);
    Timer t = new Timer();
    ArrayList < rietItem > riArr = new ArrayList < rietItem > ();
    ArrayList < rietSlot > rsArr = new ArrayList < rietSlot > ();
    int moneyMade = 0;
    // rietItem[] riArr = new rietItem[] {
    //   new rietItem("Fly fishing rod"), new rietItem("Wizard hat"), new rietItem("Studded chaps (g)")
    //};

    @Override
    public void onPaint(Graphics graphics) {
        graphics.setColor(new Color(204, 187, 154));
        graphics.fillRect(0, 300, 500, 200);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Time ran: " + t.formatTime(), 8, 490);
        graphics.drawString("Profit: " + moneyMade + " (" + t.getHourlyRate(moneyMade) + ")", 8, 475);
        for (int i = 0; i < riArr.size(); i++) {
            if (riArr.get(i).timerstarted) {
                graphics.drawString(riArr.get(i).name + " - Margin: " + riArr.get(i).margin + " - boughtprice: " + riArr.get(i).boughtprice + " - soldprice: " + riArr.get(i).soldprice + " - time: " + riArr.get(i).t.formatTime(), 8, 460 - 15 * i);
            } else {
                graphics.drawString(riArr.get(i).name + " - Margin: " + riArr.get(i).margin + " - boughtprice: " + riArr.get(i).boughtprice + " - soldprice: " + riArr.get(i).soldprice, 8, 460 - 15 * i);
            }
        }
        
      
        for (int i = 0; i < rsArr.size(); i++) {
        	//if(rsArr.get(i).timerstarted) {
        		graphics.drawString("Slot " + rsArr.get(i).item.slot  + ": " + rsArr.get(i).t.formatTime() , 8, 340 - 15 * i);
        	//}
		}
       

        super.onPaint(graphics);
    }

    @Override
    public void onStart() {
        //this.riArr.add(new rietItem("Ruby"));
        //this.riArr.add(new rietItem("Diamond"));
        //this.riArr.add(new rietItem("Sapphire"));
    	this.riArr.add(new rietItem("Diamond"));
    	this.riArr.add(new rietItem("Uncut diamond"));
    	this.riArr.add(new rietItem("Uncut ruby"));
    	this.riArr.add(new rietItem("Emerald"));
    	this.riArr.add(new rietItem("Uncut sapphire"));
    	this.riArr.add(new rietItem("Ruby"));
    }

    @Override
    public int onLoop() {
        Inventory i = getInventory();
        //Player p = getLocalPlayer();
        //NPC clerk = getNpcs().closest("Grand Exchange Clerk");
        GrandExchange g = getGrandExchange();
        log("cooking");
        if (!geArea.contains(getLocalPlayer())) {
            getWalking().walkExact(geTile);
        } else {
            if (!i.contains("Coins")) {
                if (getBank().isOpen()) {
                    if (getBank().contains("Coins")) {
                        getBank().withdrawAll("Coins");
                        sleepUntil(() -> i.contains("Coins"), Calculations.random(3500, 5500));
                    }
                } else {
                    if (getBank().open(BankLocation.GRAND_EXCHANGE)) {
                        sleepUntil(() -> getBank().isOpen(), Calculations.random(5000, 8500));
                    }
                }
            } else {
                if (getBank().isOpen()) {
                    getBank().close();
                } else {
                    if (!g.isOpen()) {
                        g.open();
                        sleepUntil(() -> g.isOpen(), Calculations.random(5000, 8500));
                    } else {
                        if (g.isOpen()) {
                        	
                        	checkSlots();
                        	
                        	
                        	for(GrandExchangeItem geItem : g.getItems()){
                        		if(geItem.isReadyToCollect()) {
                        			g.collect();
                            		sleepUntil(() -> !g.isReadyToCollect(), Calculations.random(5000, 8500));
                        			
                        			for(rietSlot a : this.rsArr){
                        				if(a.item.name == geItem.getName()) {
                        					this.rsArr.remove(a);
                        				}
                        				if(a.item.slot == geItem.getSlot()) {
                        					this.rsArr.remove(a);
                        				}
                        			}
                        		}
                        	}
                        	
                        	if(g.isReadyToCollect()) {
                        		g.collect();
                        		sleepUntil(() -> !g.isReadyToCollect(), Calculations.random(5000, 8500));
                        	}
                        	
                            for (int j = 0; j < riArr.size(); j++) {
                                String minutesPassed = riArr.get(j).t.formatTime().toString().split(":")[1];
                                int minutes = Integer.parseInt(minutesPassed);
                                if (riArr.get(j).boughtprice == 0 || riArr.get(j).soldprice == 0 || minutes >= 10) {
                                    getBuyPrice(riArr.get(j));
                                    riArr.get(j).margin = riArr.get(j).boughtprice - riArr.get(j).soldprice;
                                    sleep(Calculations.random(400, 550));


                                    if (riArr.get(j).boughtprice != 0) {
                                        riArr.get(j).flipbuyprice = riArr.get(j).soldprice + 1;
                                    }

                                    if (riArr.get(j).soldprice != 0) {
                                        riArr.get(j).flipsellprice = riArr.get(j).boughtprice - 1;
                                        if(riArr.get(j).margin == 0 || riArr.get(j).margin <= 2) {
                                        	this.riArr.remove(j);
                                        	break;
                                        }
                                    }
                                    int flipmoney = (int)(getInventory().get("Coins").getAmount() * 0.25);
                                    riArr.get(j).flipAmount = flipmoney / riArr.get(j).flipbuyprice;
                                    riArr.get(j).stopTimer();
                                }
                                flipItem(riArr.get(j));
                            }
                            
                            
	                        
                            
                            
                        }
                    }
                }
            }
        }
        return 300;
    }

    
    
    @Override
    public void onItemChange(Item[] items) {
    	for(rietItem a : this.riArr){
	    	 for (Item item : items) {
	             if (a.name == item.getName()) {
	                this.moneyMade = item.getAmount() * a.margin ;
	             }
	         }
    	 }
    }
    
    
    public void checkSlots() {
    	GrandExchange g = getGrandExchange();
    	
    	//remove double slots
    	ArrayList<String> count = new ArrayList<String>();
    	for (int i = 0; i < g.getItems().length; i++) {
			count.add(g.getItems()[i].getName());
		}
    	log("debug: " + count.toString());
    	ArrayList<Integer> removeSlots = new ArrayList<Integer>();
    	
    	for (int i = 0; i < g.getItems().length; i++) {
    		int occurrences = Collections.frequency(count, g.getItems()[i].getName());
    		log("debug1: " + occurrences);
    		if(occurrences > 1) {
    			removeSlots.add(g.getItems()[i].getSlot());
    		}
    	}
    	
    	for (int i = 0; i < removeSlots.size(); i++) {
			if(g.isSlotEnabled(removeSlots.get(i))) {
				if(g.cancelOffer(removeSlots.get(i))) {
					sleep(Calculations.random(1000, 2500));
					int a = removeSlots.get(i);
					sleepUntil(() -> !g.isSlotEnabled(a), Calculations.random(5000, 8500));
					g.close();
					sleep(Calculations.random(100, 250));
				}
			}
		}
    	
    	
    	for (int i = 0; i < this.rsArr.size(); i++) {
    		String minutesPassed = rsArr.get(i).t.formatTime().toString().split(":")[1];
            int minutes = Integer.parseInt(minutesPassed);
			if(minutes >= 2) {
				for(GrandExchangeItem geItem : g.getItems()){
					log("transvalue: " + geItem.getTransferredAmount());
					if(geItem.getStatus().toString() == "BUY") {
						if(g.cancelOffer(geItem.getSlot())) {
							sleep(Calculations.random(1000, 2500));
							sleepUntil(() -> !g.slotContainsItem(geItem.getSlot()), Calculations.random(5000, 8500));
							g.close();
							sleep(Calculations.random(100, 250));
							this.rsArr.remove(i);
						}
					}
				}
			}
			/*
    		 minutesPassed = rsArr.get(i).t.formatTime().toString().split(":")[1];
             minutes = Integer.parseInt(minutesPassed);
             
             if(minutes >= 10) {
            	 for(GrandExchangeItem geItem : g.getItems()){
            		 if(geItem.getStatus().toString() == "SELL") {
            			 if(g.cancelOffer(geItem.getSlot())) {
 							sleep(Calculations.random(1000, 2500));
 							sleepUntil(() -> g.slotContainsItem(geItem.getSlot()), Calculations.random(5000, 8500));
 							g.close();
 							sleep(Calculations.random(100, 250));
 							this.rsArr.remove(i);
 						}
            		 }
            	 }
             }
             */

			
		}
		
    }
    
    public void flipItem(rietItem item) {
        GrandExchange g = getGrandExchange();
        for (int j = 0; j < riArr.size(); j++) {
            String minutesPassed = riArr.get(j).t.formatTime().toString().split(":")[1];
            int minutes = Integer.parseInt(minutesPassed);
            
            if (riArr.get(j).boughtprice > 0 & riArr.get(j).soldprice > 0 & minutes < 10) {
                if (!getInventory().contains(item.name) && g.getFirstOpenSlot() != -1 && !isItemCurrentlyInGE(item)) {
                    item.slot = g.getFirstOpenSlot();
                    if (g.openBuyScreen(item.slot)) {
                        sleepUntil(() -> g.isBuyOpen(), Calculations.random(5000, 8500));
                        g.searchItem(item.name);
                        sleep(Calculations.random(100, 250));
                        g.addBuyItem(item.name);
                        sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
                        sleep(Calculations.random(100, 250));
                        g.buyItem(item.name, item.flipAmount, item.flipbuyprice);
                        sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
                        sleep(Calculations.random(1000, 2500));
                        if(g.slotContainsItem(item.slot)) {
                        	this.rsArr.add(new rietSlot(item));
                        }
                    }
                }

                if (getInventory().contains(item.name)) {
                	if (g.getFirstOpenSlot() != -1) {
                        if (g.addSellItem(item.name)) {
                            sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                            sleep(Calculations.random(1000, 2500));
                            if (g.setPrice(item.flipsellprice)) {
                            	if(g.setQuantity(getInventory().get(item.name).getAmount())) {
                                sleep(Calculations.random(1000, 2500));
                                if (g.confirm()) {
                                    sleep(Calculations.random(1000, 2500));
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

    public boolean isItemCurrentlyInGE(rietItem item) {
    	GrandExchange g = getGrandExchange();
    	
    	if(!g.isGeneralOpen()) {
    		if(g.open()) {
    			sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
    		}
    	}
    	
    	
    	for(GrandExchangeItem geItem : g.getItems()){
        	if(geItem.getName().equals(item.name)) {
        		return true;
        	}
        }
    	return false;
    }


    public void getBuyPrice(rietItem item) {
        GrandExchange g = getGrandExchange();
        if (g.isGeneralOpen()) {
            if (!getInventory().contains(item.name) && g.getFirstOpenSlot() != -1) {
                item.slot = g.getFirstOpenSlot();
                if (g.openBuyScreen(item.slot)) {
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
                            g.cancelOffer(item.slot);
                            sleep(Calculations.random(1000, 2500));
                            col1.interact();
                            sleep(Calculations.random(1000, 2500));
                            this.riArr.remove(item);
                        } else {
                            item.boughtprice = boughtPrice;
                            col1.interact();
                            col2.interact();
                        }
                    }
                }
            }

            sleep(Calculations.random(1000, 2500));
            if (g.getFirstOpenSlot() != -1) {
            	if(getInventory().get(item.name).getAmount() == 1) {
	                if (g.addSellItem(item.name)) {
	                    sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
	                    sleep(Calculations.random(1000, 2500));
	                    if (g.setPrice(1)) {
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
	                }
            	}
            }

        }
    }

}