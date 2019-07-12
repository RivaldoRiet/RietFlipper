package RietFlipper.Util;



import static org.dreambot.api.methods.MethodProvider.sleep;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.Player;

import RietFlipper.RietFlipper;
import RietFlipper.rietItem;

public class BuyItem extends Node {

    public BuyItem(RietFlipper c) {
        super(c);
    }

    @Override
    public boolean validate() {
        return c.itemWithoutBuyPrice() != null && !c.sellHasExpired() && !c.buyHasExpired() && getInventory().contains("Coins") && getGrandExchange().isOpen();
    }

    @Override
    public int execute() {
    	c.log("" + this.getClass().getName() + " - debug: ");
        if(c.itemWithoutBuyPrice() != null) {
        	rietItem item = c.itemWithoutBuyPrice();
        	c.log("" + this.getClass().getName() + " - debug: " + c.itemWithoutBuyPrice().name);
        	c.getBuyPrice(item);
        	c.setMargin(item);
        }
        return 0;
    }
    
    public Inventory getInventory() {
   	 return c.getInventory();
    }
    
    public GrandExchange getGrandExchange() {
   	return c.getGrandExchange();
    }
    
    public Walking getWalking() {
   	 return c.getWalking();
    }
    
    public Bank getBank() {
   	 return c.getBank();
    }
    
    public Player getLocalPlayer() {
		return c.getLocalPlayer();
    }
    
}
