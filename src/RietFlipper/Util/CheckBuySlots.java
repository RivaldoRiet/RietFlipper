package RietFlipper.Util;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.Player;

import RietFlipper.RietFlipper;

public class CheckBuySlots extends Node{

	public CheckBuySlots(RietFlipper c) {
        super(c);
    }

    @Override
    public boolean validate() {
        return !c.geItemIsCollectable() && c.buyHasExpired() && getInventory().contains("Coins") && getGrandExchange().isOpen();
    }
    
    @Override
    public int execute() {
    	 Inventory i = getInventory();
         GrandExchange g = getGrandExchange();
         c.log("" + this.getClass().getName() + "debug: ");
         c.checkBuySlots();
         
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
