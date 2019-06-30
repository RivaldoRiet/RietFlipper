package RietFlipper;

import java.awt.Graphics;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.WidgetChild;

@ScriptManifest(category = Category.MONEYMAKING, name = "new RietFlipper grand exchange flipper v1.0", author = "Riet/Rivaldo/Oh okay", version = 0)
public class RietFlipper extends AbstractScript {
	Area geArea = new Area(3160, 3482, 3172, 3495);
	Tile geTile = new Tile(3163, 3485);
	Timer t = new Timer();
	rietItem ri = new rietItem("Rune kiteshield");
	rietItem[] riArr = new rietItem[]{new rietItem("Ruby"), new rietItem("Diamond"), new rietItem("Sapphire")};
	@Override
	public void onPaint(Graphics graphics) {
        graphics.drawString("Time ran: " + t.formatTime(), 8, 335);
        for (int i = 0; i < riArr.length; i++) {
			graphics.drawString(riArr[i].name + " - Margin: " + riArr[i].margin + " - boughtprice: " + riArr[i].boughtprice + " - soldprice: " + riArr[i].soldprice, 8, 320 - 15 * i);
		}
        
		super.onPaint(graphics);
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
		}else {
			if(!i.contains("Coins")) {
				if (getBank().isOpen()) {
					if(getBank().contains("Coins")) {
						getBank().withdrawAll("Coins");
						sleepUntil(() -> i.contains("Coins"), Calculations.random(3500, 5500));
					}
				} else {
					if (getBank().open(BankLocation.GRAND_EXCHANGE)) {
						sleepUntil(() -> getBank().isOpen(), Calculations.random(5000, 8500));
					}
				}
			}else {
				if(getBank().isOpen()) {
					getBank().close();
				}else {
					if(!g.isOpen()) {
						g.open();
						sleepUntil(() -> g.isOpen(), Calculations.random(5000, 8500));
					}else {
						if(g.isGeneralOpen()) {
							for (int j = 0; j < riArr.length; j++) {
								/*
								while(riArr[j].boughtprice == 0) {
									getBuyPrice(riArr[j]);
									sleep(Calculations.random(100, 250));
								}
								
								while(riArr[j].soldprice == 0) {
									getSellPrice(riArr[j]);
									sleep(Calculations.random(100, 250));
								}
								riArr[j].margin = riArr[j].boughtprice - riArr[j].soldprice;
								*/
								
								if(riArr[j].boughtprice > 0) {
									if(riArr[j].soldprice == 0) {
										log("debug x");
										getSellPrice(riArr[j]);
									}else {
										log("debug y");
										riArr[j].margin = riArr[j].boughtprice - riArr[j].soldprice;
										//log("Margin: " + ri.margin + " - boughtprice: " + ri.boughtprice + " - soldprice: " + ri.soldprice);
									}
								}else {
									log("debug z");
									getBuyPrice(riArr[j]);
								}
								
								
								
								
								
							}
						}
					}
				}
			}
		}
		return 300;
	}
	
	public void getBuyPrice(rietItem item) {
		GrandExchange g = getGrandExchange();
		if(g.isGeneralOpen()) {
			item.slot = g.getFirstOpenSlot();
			if(g.openBuyScreen(item.slot)) {
				sleepUntil(() -> g.isBuyOpen(), Calculations.random(5000, 8500));
				g.searchItem(item.name);
				sleep(Calculations.random(100, 250));
				g.addBuyItem(item.name);
				sleepUntil(() -> g.getCurrentPrice() > 0, Calculations.random(5000, 8500));
				sleep(Calculations.random(100, 250));
				g.buyItem(item.name, 1, g.getCurrentPrice() * 2);
				sleepUntil(() -> g.slotContainsItem(item.slot), Calculations.random(5000, 8500));
				sleep(Calculations.random(100, 250));
				if(g.slotContainsItem(item.slot)) {
					g.openSlotInterface(item.slot);
					sleep(Calculations.random(2000, 2500));
					WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
					WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
					WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
					String buyText = x.getText();
					buyText = buyText.split("price of <col=ffb83f>")[1];
					buyText = buyText.split("<")[0];
					buyText = buyText.replace(",", "");
					int boughtPrice = Integer.parseInt(buyText);
					item.boughtprice = boughtPrice;
					sleep(Calculations.random(100, 250));
					col1.interact();
					sleep(Calculations.random(100, 250));
					col2.interact();
					sleep(Calculations.random(100, 250));
					sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
				}
			}
		}
	}
	
	
	
	public void getSellPrice(rietItem item) {
		GrandExchange g = getGrandExchange();
		log("debug 1");
			log("debug 2");
			if(g.getFirstOpenSlot() != -1) {
				log("debug 3");
				
				if(g.addSellItem(item.name)) {
				if(g.setPrice(1)) {
				if(g.confirm()) {
				log("debug 4");
				if(g.openSlotInterface(item.slot)) {
					log("debug 5");
					//sleepUntil(() -> g.isSellOpen(), Calculations.random(5000, 8500));
					WidgetChild x = getWidgets().getWidget(465).getChild(22).getChild(1);
					WidgetChild col1 = getWidgets().getWidget(465).getChild(23).getChild(0);
					WidgetChild col2 = getWidgets().getWidget(465).getChild(23).getChild(1);
					sleepUntil(() -> getWidgets().getWidget(465).getChild(23).getChild(0).isVisible(), Calculations.random(5000, 8500));
					String soldText = x.getText();
					soldText = soldText.split("price of <col=ffb83f>")[1];
					soldText = soldText.split("<")[0];
					soldText = soldText.replace(",", "");
					int soldprice = Integer.parseInt(soldText);
					item.soldprice = soldprice;
					
					col1.interact();
					col2.interact();
					sleepUntil(() -> g.isGeneralOpen(), Calculations.random(5000, 8500));
					log("debug 6");
					}
				}
				}
				}
			}
	}
}
