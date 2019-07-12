package RietFlipper.Util;

import static org.dreambot.api.methods.MethodProvider.sleepUntil;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.Player;

import RietFlipper.RietFlipper;

public class CheckSellSlots extends Node{

	public CheckSellSlots(RietFlipper c) {
        super(c);
    }

    @Override
    public boolean validate() {
        return c.itemWithoutBuyPrice() == null && !c.geItemIsCollectable() && c.sellHasExpired() && !c.buyHasExpired() && getInventory().contains("Coins") && getGrandExchange().isOpen();
    }

    @Override
    public int execute() {
    	 Inventory i = getInventory();
         GrandExchange g = getGrandExchange();
         c.log("" + this.getClass().getName() + "debug: ");
         c.checkSellSlots();
         
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
