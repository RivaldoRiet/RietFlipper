package RietFlipper;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
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

import RietFlipper.Util.*;



@ScriptManifest(category = Category.MONEYMAKING, name = "RietFlipper 2.0", author = "Riet/Rivaldo/Oh okay", version = 0)
public class RietFlipper extends AbstractScript implements InventoryListener {
    public Area geArea = new Area(3160, 3482, 3172, 3495);
    public Tile geTile = new Tile(3163, 3485);
    public Timer t = new Timer();
    boolean moneySet = false;
    public int flipmoney = 0;
    public String status = "none";
    public ArrayList < rietItem > riArr = new ArrayList < rietItem > ();
    public ArrayList < rietBuySlot > rsArr = new ArrayList < rietBuySlot > ();
    public ArrayList < rietSellSlot > rssArr = new ArrayList < rietSellSlot > ();
    public ArrayList < rietUnsoldSlot > rsuArr = new ArrayList < rietUnsoldSlot > ();
    public int moneyMade = 0;
    public int buySlotMinutes = 1;
    public int sellSlotMinutes = 1;
    private Node[] nodes;
    private boolean isRunning = false;
	public JFrame frmRietflipperGui;
	private JTextField textField;
	private JTextField textField_1;
	List list = new List();
	List list_1 = new List();
	private JTextField textField_2;
	JCheckBox chckbxNewCheckBox = new JCheckBox("Member items?");
	ArrayList<String> saveList = new ArrayList<String>();


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
           if (riArr.get(i).margin > 0) {
            	paintArr.add(riArr.get(i));
            }
        }
        for (int i = 0; i < paintArr.size(); i++) {
			graphics.drawString(paintArr.get(i).name + " - Margin: " + paintArr.get(i).margin + " - boughtprice: " + paintArr.get(i).boughtprice + " - soldprice: " + paintArr.get(i).soldprice, 8, 445 - 12 * i);
		}
        

        for (int i = 0; i < rsArr.size(); i++) {
            graphics.drawString("Buy Slot: " + rsArr.get(i).item.slot + ": " + rsArr.get(i).t.formatTime(), 8 * 15 * i, 340);
        }

        for (int i = 0; i < rssArr.size(); i++) {
            graphics.drawString("Sell Slot: " + rssArr.get(i).sellSlotNumber + ": " + rssArr.get(i).t.formatTime(), 8 * 15 * i, 325);
        }
        
    }

    @Override
    public void onStart() {
        this.status = "Starting up";
        
        OSBuddy.init();
   
        
        
        //this.scraper = new Scraper();
		list.setMultipleMode(true);
		list_1.setMultipleMode(true);
		
		initialize();
		
        
        /*
        this.riArr.add(new rietItem("Sapphire"));
        this.riArr.add(new rietItem("Uncut sapphire"));
        this.riArr.add(new rietItem("Uncut ruby"));
        this.riArr.add(new rietItem("Ruby"));
        this.riArr.add(new rietItem("Gold bar"));
        this.riArr.add(new rietItem("Gold ore"));
        this.riArr.add(new rietItem("Gold necklace"));
        this.riArr.add(new rietItem("Big bones")); 
        */

        nodes = new Node[]{
                new OpenGe(this),
                new FlipItemSell(this),
                new FlipItemBuy(this),
                new CheckBuySlots(this),
                new CheckSellSlots(this),
                new CollectMoney(this),
                new BuyItem(this),
        };

        


    }

    @Override
    public int onLoop() {
    	
    	if(isRunning) {
    	 for (Node node: nodes){
             if (node.validate()){
            	 return node.execute();
             }
         }
    	}
    	
        return 1;
    }
    
    public boolean inventoryContainsSpecificItem(rietItem item) {
    	if(item != null && getInventory().contains(item.name)) {
    		return true;
    	}
    	return false;
    }

    public void setMargin(rietItem item) {
    	item.margin = item.boughtprice - item.soldprice;
    	
    	
        if (item.boughtprice != 0) {
        	item.flipbuyprice = item.soldprice + 1;
        }

        if (item.soldprice != 0) {
        	item.flipsellprice = item.boughtprice - 1;
        	item.profitmargin = item.flipbuyprice - item.flipsellprice;
            if (item.margin == 0 || item.margin <= 2) {
                this.riArr.remove(item);
            }
        }
    }
    
    public void setFlipMoney(rietItem item) {
    	if (getInventory().get("Coins") != null) {
            flipmoney = (int)(getInventory().get("Coins").getAmount() * 0.50);
            if (flipmoney > 0 && item.flipbuyprice > 0) {
            	if(item.flipbuyprice < flipmoney && flipmoney / item.flipbuyprice > 1) {
            		item.flipAmount = flipmoney / item.flipbuyprice;
            		item.stopTimer();
            	}
            }
        }
    }

    
    
    public rietItem itemWithoutBuyPrice() {
    	for (int j = 0; j < riArr.size(); j++) {
    		sortByHighestMargin();
            if (riArr.get(j).boughtprice == 0 || riArr.get(j).soldprice == 0) {
            	if(!isItemCurrentlyInGE(riArr.get(j))) {
            		if(getInventory().count(riArr.get(j).name) > 1) {
            			return null;
            		}
            		return riArr.get(j);
            	}
            }
    	}
    	return null;
    }

    public boolean geItemIsCollectable() {
    	 GrandExchange g = getGrandExchange();
         if(g.isReadyToCollect()) {
        	 return true;
         }
    	
    	return false;
    }
    
    public void cleanupSlots() {
    	for (int i = 0; i < rssArr.size(); i++) {
			if(rssArr.get(i).sellSlotNumber == -1) {
				this.rssArr.remove(rssArr.remove(i));
			}
		}
    }
    
    public void collectMoney() {
        GrandExchange g = getGrandExchange();
        cleanupSlots();
        for (GrandExchangeItem geItem: g.getItems()) {
            int currSlot = geItem.getSlot();
             if (g.isReadyToCollect(currSlot)) {
            	if(g.isBuyOpen() && !g.isSellOpen()) {
	            	if(g.openSlotInterface(currSlot)) {
	            		sleepUntil(() -> g.isBuyOpen(), Calculations.random(2500, 3800));
	            	}
            	}
            	if(!g.isBuyOpen() && g.isSellOpen()) {
            		if(g.openSlotInterface(currSlot)) {
	            		sleepUntil(() -> g.isSellOpen(), Calculations.random(2500, 3800));
	            	}
            	}
            	if(!g.isBuyOpen() && !g.isSellOpen()) {
            		if(g.openSlotInterface(currSlot)) {
	            		sleepUntil(() -> g.isSellOpen(), Calculations.random(2500, 3800));
	            	}
            	}
            	
                this.status = "Collecting items from slots";
                for (int i = 0; i < this.rssArr.size(); i++) {
                	if(this.rssArr.get(i).sellSlotNumber == currSlot) {
                		this.rssArr.remove(this.rssArr.get(i));
                		log("Removing from rsArr UH WHAT: " + this.rssArr.get(i).binItemName);
                	}
                }
                
                for (int i = 0; i < this.rsArr.size(); i++) {
                    if (this.rsArr.get(i).item.slot == currSlot) {
                        this.rsArr.remove(this.rsArr.get(i));
                        log("removing from rsArr again");
                    }
                }

                WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
                WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                
                
                WidgetChild col3 = getWidgets().getWidget(465).getChild(23).getChild(2);
                WidgetChild col4 = getWidgets().getWidget(465).getChild(23).getChild(3);
                
     
                WidgetChild checkmoney = getWidgets().getWidget(465).getChild(23).getChild(2);
                int checkmoneyid = checkmoney.getItemId();
                
                String buyText = x.getText();
                buyText = buyText.split("price of <col=ffb83f>")[1];
                buyText = buyText.split("<")[0];
                buyText = buyText.replace(",", "");
                
                
                
                if(col1.interact()) {
                sleep(Calculations.random(2500, 2750));
                sleepUntil(() -> g.isGeneralOpen(), Calculations.random(1500, 1800));
                WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
                if(col2 != null && col2.isVisible() && col2.interact()) {
                sleep(Calculations.random(2500, 2750));
                sleepUntil(() -> g.isGeneralOpen(), Calculations.random(1500, 1800));

                /*
                if(checkmoneyid != 995) {
                	//als we alleen geld hebben opgehaald mogen we het item verwijderen uit de array
                	for (int i = 0; i < this.rsuArr.size(); i++) {
                    	if(this.rsuArr.get(i).binItemName == checkmoney.getItem().getName()) {
                        	if (inventoryContainsGeItem()) {
                            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                                if (slotWeAreUsingToSell != -1) {
                                    if (g.addSellItem(this.rsuArr.get(i).binItemName)) {
                                    	this.status = "Selling item: " + this.rsuArr.get(i).binItemName;
                                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                                        sleep(Calculations.random(1000, 2500));
                                        if (g.setPrice(1)) {
                                            if (g.setQuantity(getInventory().count(this.rsuArr.get(i).binItemName))) {
                                                sleep(Calculations.random(1000, 2500));
                                                if (g.confirm()) {
                                                	sleep(Calculations.random(1000, 2500));
                         
                                                	for (int j = 0; j < this.riArr.size(); j++) {
														if(this.rsuArr.get(i).binItemName.equals(this.riArr.get(j).name)) {
								
															this.riArr.remove(this.riArr.get(j));
															log("Removing from riArr: " + this.riArr.get(j).name);
														}
													}
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
                */
               
                
                
                
                
            }
            }
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

    public boolean buyScreenCollectableItemsOnScreen() {
    	GrandExchange g = getGrandExchange();
    	if(g.isBuyOpen() && g.isReadyToCollect()) {
    		return true;
    	}
    	return false;
    }
    
    
    public boolean buyHasExpired() {
    	for (int i = 0; i < this.rsArr.size(); i++) {
	            String minutesPassed = rsArr.get(i).t.formatTime().toString().split(":")[1];
	            int minutes = Integer.parseInt(minutesPassed);
	            if (minutes >= this.buySlotMinutes) {
	            	return true;
	            }
    	}
    	return false;
    }
    
    public boolean sellHasExpired() {
    	  for (int i = 0; i < this.rssArr.size(); i++) {
          	String minutesPassed = rssArr.get(i).t.formatTime().toString().split(":")[1];
              int minutes = Integer.parseInt(minutesPassed);
              if (minutes >= this.sellSlotMinutes) {
            	  return true;
              }
    	  }
    	return false;
    }
    
    
    public void checkBuySlots() {
    	GrandExchange g = getGrandExchange();
    	
    	  for (int i = 0; i < this.rsArr.size(); i++) {
              String minutesPassed = rsArr.get(i).t.formatTime().toString().split(":")[1];
              int minutes = Integer.parseInt(minutesPassed);
              if (minutes >= this.buySlotMinutes) {
                  for (GrandExchangeItem geItem: g.getItems()) {
                      if (geItem.getName().equals(this.rsArr.get(i).item.name)) {
                          if (geItem.getStatus().toString() == "BUY") {
                              this.status = "offer for buy slot " + geItem.getName() + " has expired";
                              int slotnum = geItem.getSlot();
                              if (geItem.getTransferredAmount() == 0) {
                                      this.status = "Detected that item: " + geItem.getName() + " is not buying.. skipping";
                                      if (g.cancelOffer(geItem.getSlot())) {
                                          sleep(Calculations.random(1000, 2500));
                                          goBack();
                                          sleep(Calculations.random(100, 250));
                                          sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                          if(!g.slotContainsItem(geItem.getSlot())) {
                                          this.rsArr.remove(geItem.getSlot());
                                          log("Removing from rsArr: " + this.rsArr.get(i).item.name);
                                          this.riArr.get(i).itemWasNotBuying = true;
                                          }
                                          
                                      }
                              } else {
                                  if (slotnum != -1) {
                                  	this.status = "Cancelling item";
                                      if (g.cancelOffer(slotnum)) {
                                         // goBack();
                                         // sleep(Calculations.random(100, 250));
                                        //  sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                    	  this.collectMoney();
                                  }
                                  }
                              }
                          }
                      }
                  }
              }
          }
    }

    public boolean goBackVisible() {
    	GrandExchange g = getGrandExchange();
    	WidgetChild goback = getWidgets().getWidget(465).getChild(4);
    	if(goback != null && goback.isVisible()) {
	        if(goback.interact()) {
	        	sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
	        	if(g.isGeneralOpen()) {
	        	return true;
	        }
	        }
    	}
    	return false;
    }
    
    public void goBack() {
    	GrandExchange g = getGrandExchange();
    	while(g.isSellOpen() && g.isBuyOpen() && goBackVisible() == false) {
    	}	
    }
    
    
    
    public void checkSellSlots() {
        GrandExchange g = getGrandExchange();

        
        for (int i = 0; i < this.rssArr.size(); i++) {
        	String minutesPassed = rssArr.get(i).t.formatTime().toString().split(":")[1];
            int minutes = Integer.parseInt(minutesPassed);
            if (minutes >= this.sellSlotMinutes) {
            	if(g.getItems() != null && g.getItems().length > 0) {
                for (GrandExchangeItem geItem: g.getItems()) {
                    if(geItem != null && geItem.getName().equals(rssArr.get(i).binItemName)) {
                    	 this.rssArr.get(i).binItemName = geItem.getName();
                         this.rssArr.get(i).setBinSellItemTrue();
                        if (geItem.getStatus().toString() == "SELL") {
                            this.status = "offer for sell slot " + geItem.getName() + " has expired";
                                this.status = "Cancelling item";
                                if (geItem.getSlot() != -1) {
                                    if (g.cancelOffer(geItem.getSlot())) {
                                        sleep(Calculations.random(1000, 2500));
                                        goBack();
                                        sleep(Calculations.random(100, 250));
                                        sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                        this.rsuArr.add(new rietUnsoldSlot(rssArr.get(i).sellSlotNumber, rssArr.get(i).binItemName));
                                        this.collectMoney();
	                            
                            }
                        }
                        }
		            }
                }
		        }
		    }
		}
        
        for (int i = 0; i < this.rsuArr.size(); i++) {
        	if (inventoryContainsGeItem()) {
            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                if (slotWeAreUsingToSell != -1) {
                    if (g.addSellItem(this.rsuArr.get(i).binItemName)) {
                    	this.status = "Selling item: " + this.rsuArr.get(i).binItemName;
                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                        sleep(Calculations.random(1000, 2500));
                        if (g.setPrice(1)) {
                            if (g.setQuantity(getInventory().count(this.rsuArr.get(i).binItemName))) {
                                sleep(Calculations.random(1000, 2500));
                                if (g.confirm()) {
                                	sleep(Calculations.random(1000, 2500));
                                	this.rsuArr.remove(this.rsuArr.get(i));
                                	log("Removing from rsuArr: " + this.rsuArr.get(i).binItemName);
                                	for (int j = 0; j < this.rsuArr.size(); j++) {
										if(this.rsuArr.get(i).binItemName.equals(this.riArr.get(j).name)) {
											this.riArr.remove(this.riArr.get(j));
											log("Removing from rsuArr item: " + this.riArr.get(j).name);
										}
									}

                                	this.rsuArr.remove(this.rsuArr.get(i));
                                	log("Removing from rsuArr rsuarr: " + this.rsuArr.get(i).binItemName);
                                }
                            }
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
    
    public rietItem itemReadyToFlipSell() {
    	for (int i = 0; i < this.riArr.size(); i++) {
    		if (getInventory().contains(riArr.get(i).name)) {
    			return this.riArr.get(i);
    		}
		}
    	
    	return null;
    }
    
    public rietItem itemReadyToFlipBuy() {
    	GrandExchange g = getGrandExchange();
    	for (int i = 0; i < this.riArr.size(); i++) {
    		if (!isItemCurrentlyInGE(this.riArr.get(i)) && !inventoryContainsGeItem()  && g.getFirstOpenSlot() != -1 && this.riArr.get(i).boughtprice > 0 && this.riArr.get(i).soldprice > 0) {
    			return this.riArr.get(i);
    		}
		}
    	
    	return null;
    }
    
    
    public boolean itemGotPrices(rietItem item){
    	if(item != null && item.boughtprice > 0 && item.soldprice > 0) {
    		return true;
    	}
    	return false;
    }

    public void flipItem(rietItem item) {
        GrandExchange g = getGrandExchange();
        
        if (item.boughtprice > 0 && item.soldprice > 0) {
            if (getInventory().contains(item.name)) {
            	int slotWeAreUsingToSell = g.getFirstOpenSlot();
                if (slotWeAreUsingToSell != -1) {
                    if (g.addSellItem(item.name)) {
                    	this.status = "Selling item: " + item.name;
                        sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
                        sleep(Calculations.random(1000, 2500));
                        if (g.setPrice(item.flipsellprice)) {
                            if (g.setQuantity(getInventory().count(item.name))) {
                                sleep(Calculations.random(1000, 2500));
                                if (g.confirm()) {
                                	item.slot = slotWeAreUsingToSell;
                                	this.rssArr.add(new rietSellSlot(slotWeAreUsingToSell, item.name));
                                    sleep(Calculations.random(1000, 2500));
                                    sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                                        item.resetFields(item.name);
                                }
                            }
                        }
                    }
                }
           }

            if (!inventoryContainsGeItem() && g.getFirstOpenSlot() != -1 && !isItemCurrentlyInGE(item) && g.getFirstOpenSlot() != -1) {
                item.slot = g.getFirstOpenSlot();
                if (g.openBuyScreen(item.slot)) {
                    this.status = "Buying item: " + item.name;
                    sleepUntil(() -> g.isBuyOpen(), Calculations.random(5000, 8500));
                    if(g.searchItem(item.name)) {
                    sleep(Calculations.random(100, 250));
                    if(g.addBuyItem(item.name)) {
                    sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    if(g.buyItem(item.name, item.flipAmount, item.flipbuyprice)) {
                    sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
                    sleep(Calculations.random(1000, 2500));
                    if (g.slotContainsItem(item.slot)) {
                        this.rsArr.add(new rietBuySlot(item));
                    }
                }
            }
            
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
                    if(g.searchItem(item.name)) {
                    sleep(Calculations.random(100, 250));
                    if(g.addBuyItem(item.name)) {
                    sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    if(g.buyItem(item.name, 1, g.getCurrentPrice() * 2)) {
                    sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
                    sleep(Calculations.random(100, 250));
                    if (g.slotContainsItem(item.slot)) {
                        if(g.openSlotInterface(item.slot)) {
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
                            col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                            if(col1.interact()) {
                            sleep(Calculations.random(1000, 2500));
                            this.riArr.remove(item);
	                        log("removing single item: " + item.name);
                            sleep(Calculations.random(100, 250));
                            sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                           }
                           }
                        } else {
                            item.boughtprice = boughtPrice;
                            sleep(Calculations.random(1000, 2500));
                            col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
                            col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
                            if(col1.interact()) {
                            	sleep(Calculations.random(1000, 2500));
                            if(col2.interact()) {
                            sleep(Calculations.random(100, 250));
                            sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
                            }
                            }
                        }
                    }
                    }
                    }
                    }
                    }else {
                    	log("Failed searching");
                    }
                }
            }
             }else {
            	 this.collectMoney();
             }
            if (g.getFirstOpenSlot() != -1 && !g.isBuyOpen() && item.soldprice == 0 && !shouldSellItemForBinPrice(item) && getInventory().count(item.name) == 1) {
                this.status = "Getting sell price for item: " + item.name;
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
                                if(col1.interact()) {
                                if(col2.interact()) {
                                }
                                }
                            }
                        }
                    }
    
                }
            }
    }
    
    
    
    public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    int sz = str.length();
	    for (int i = 0; i < sz; i++) {
	        if (Character.isDigit(str.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRietflipperGui = new JFrame();
		frmRietflipperGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmRietflipperGui.setTitle("Rietflipper GUI");
		frmRietflipperGui.setBounds(100, 100, 721, 504);
		frmRietflipperGui.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 705, 465);
		frmRietflipperGui.getContentPane().add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Items", null, panel_1, null);
		panel_1.setLayout(null);
		list.setBounds(10, 32, 177, 166);
		panel_1.add(list);
		
		
		list_1.setBounds(370, 32, 187, 156);
		panel_1.add(list_1);
		
		JButton button = new JButton(">");
		button.setBounds(232, 42, 89, 23);
		panel_1.add(button);
		button.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));
		button.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < list.getSelectedItems().length; i++) {
					list_1.add(list.getSelectedItems()[i]);
				}
	        	
	        	// java awt list is buggy as shit so this to make sure everything gets added correctly
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        }

	    });
		
		
		
		JButton button_1 = new JButton("<");
		button_1.setBounds(232, 77, 89, 23);
		panel_1.add(button_1);
		button_1.setFont(new Font("Arial", Font.BOLD, 19));
		button_1.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < list_1.getSelectedItems().length; i++) {
					list.add(list_1.getSelectedItems()[i]);
				}
	        	
	        	// java awt list is buggy as shit so this to make sure everything gets added correctly
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        }

	    });
		
		
		
		
		JLabel label = new JLabel("Minimum item price:");
		label.setBounds(10, 215, 130, 14);
		panel_1.add(label);
		
		textField = new JTextField();
		textField.setBounds(138, 212, 86, 20);
		panel_1.add(textField);
		textField.setText("0");
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(138, 254, 86, 20);
		panel_1.add(textField_1);
		textField_1.setText("0");
		textField_1.setColumns(10);
		
		JLabel lblMinimumPrice = new JLabel("Maximum item price:");
		lblMinimumPrice.setBounds(10, 257, 133, 14);
		panel_1.add(lblMinimumPrice);
		
		JButton btnScrape = new JButton("Scrape!");
		btnScrape.setBounds(10, 320, 218, 70);
		panel_1.add(btnScrape);
		btnScrape.addActionListener(e -> {
	        	scrape();
	    });
		
		JLabel lblScrapedItems = new JLabel("Scraped items");
		lblScrapedItems.setBounds(31, 11, 100, 14);
		panel_1.add(lblScrapedItems);
		
		
		chckbxNewCheckBox.setBounds(10, 290, 155, 23);
		panel_1.add(chckbxNewCheckBox);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(558, 211, 89, 23);
		panel_1.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	list_1.add(textField_2.getText());
	        }

	    });
		
		
		textField_2 = new JTextField();
		textField_2.setBounds(433, 212, 109, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblAddItemBy = new JLabel("Add item by name:");
		lblAddItemBy.setBounds(316, 215, 128, 14);
		panel_1.add(lblAddItemBy);
		
		JLabel lblForExampleRune = new JLabel("For example: Rune kiteshield");
		lblForExampleRune.setBounds(355, 236, 187, 14);
		panel_1.add(lblForExampleRune);
		
		JLabel lblFlippingItems = new JLabel("Flipping items");
		lblFlippingItems.setBounds(409, 11, 100, 14);
		panel_1.add(lblFlippingItems);
		
		JButton btnExportItemList = new JButton("Export item list");
		btnExportItemList.setBounds(370, 290, 139, 23);
		panel_1.add(btnExportItemList);
		
		btnExportItemList.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	saveList.clear();
		        for (int i = 0; i < getList_1().getItems().length; i++) {
		        	saveList.add(getList_1().getItems()[i]);
				}
		        try {
					save();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		
		
		
		JButton btnImportItemList = new JButton("Import item list");
		btnImportItemList.setBounds(534, 290, 133, 23);
		panel_1.add(btnImportItemList);
		btnImportItemList.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
					read();
				} catch (FileNotFoundException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		

		
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(578, 32, 89, 23);
		panel_1.add(btnClear);
		btnClear.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	list_1.removeAll();
	        }

	    });
		JButton btnStartScript = new JButton("Start script");
		btnStartScript.setBounds(287, 403, 130, 23);
		panel_1.add(btnStartScript);
		btnStartScript.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < getList_1().getItems().length; i++) {
	        		riArr.add(new rietItem(getList_1().getItems()[i]));
	        		MethodProvider.log("Added item: " + getList_1().getItems()[i]);
	        	}
	        	frmRietflipperGui.dispose();
	        	isRunning = true;
	        	
	        }

	    });
		
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Settings", null, panel, null);
		panel.setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("10");
		textPane.setBounds(183, 35, 35, 20);
		panel.add(textPane);
		
		JCheckBox chckbxSellItemsIf = new JCheckBox("sell items if not sold after");
		chckbxSellItemsIf.setBounds(6, 35, 171, 23);
		panel.add(chckbxSellItemsIf);
		
		JLabel lblMinutes = new JLabel("minutes");
		lblMinutes.setBounds(228, 41, 46, 14);
		panel.add(lblMinutes);
		
		//frmRietflipperGui.pack();
		frmRietflipperGui.setVisible(true);
		
		
	}
	
	public void scrape() {
		if(isNumeric(textField.getText()) && isNumeric(textField_1.getText())) {
			list.removeAll();
			int minimumPrice = Integer.parseInt(textField.getText());
			int maximumPrice = Integer.parseInt(textField_1.getText());
			boolean ismember = chckbxNewCheckBox.isSelected();
			
	    	java.util.List<OSBuddyItem> item = OSBuddy.getItemAll(p -> p.getOverallAverage() > minimumPrice &&  p.getOverallAverage() < maximumPrice && p.isMember() == ismember);
	    	MethodProvider.log("Length: " + item.size());
	    	
	    	
	    	for (int i = 0; i < item.size(); i++) {
				OSBuddyItem item1 = (OSBuddyItem) item.get(i);
				MethodProvider.log("" + item1.getId() + " - " + item1.getName());
				list.add(item1.getName());
			}

		}else {
			JOptionPane.showMessageDialog(null, "We could not parse the numbers you have provided!", "Rietflipper info", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public List getList() {
		return this.list;
	}
	
	public List getList_1() {
		return this.list_1;
	}

	
	
	public void saveQuick(String fileName) throws FileNotFoundException {
		try {
			FileOutputStream fout= new FileOutputStream (fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveList);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings({ "unchecked" })
	public void readQuick(String fileName) throws FileNotFoundException {
		try {
			FileInputStream fin= new FileInputStream (fileName);
			ObjectInputStream ois = new ObjectInputStream(fin);
			ArrayList<String> importedArray = (ArrayList<String>)ois.readObject();
			for (int i = 0; i < importedArray.size(); i++) {
				list_1.add(importedArray.get(i));
			}
			fin.close();
	        
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	
	
	public void save() throws FileNotFoundException {
		FileOutputStream f = null;
        @SuppressWarnings("unused")
		DataOutputStream h = null;
        FileDialog d = new FileDialog(new JFrame(), "Save", FileDialog.SAVE);
        d.setVisible(true);
        String dir;
        dir = d.getDirectory();
        File oneFile = new File(dir + d.getFile() + ".riet");
        try {
            oneFile.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            f = new FileOutputStream(oneFile);
            @SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(saveList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                
                f.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	
	public void read() throws FileNotFoundException, ClassNotFoundException {
		try {
			
			FileDialog fd = new FileDialog(frmRietflipperGui, "Choose a file", FileDialog.LOAD);
			fd.setVisible(true);
			@SuppressWarnings("unused")
			String filename = fd.getFile();
			FileInputStream fin= new FileInputStream (fd.getDirectory() + fd.getFile());
			ObjectInputStream ois = new ObjectInputStream(fin);
			@SuppressWarnings("unchecked")
			ArrayList<String> importedArray = (ArrayList<String>)ois.readObject();
			for (int i = 0; i < importedArray.size(); i++) {
				list_1.add(importedArray.get(i));
			}
			fin.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}