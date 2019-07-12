package RietFlipper.Util;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.Player;

import RietFlipper.RietFlipper;
import static org.dreambot.api.methods.MethodProvider.*;

public class OpenGe extends Node{

	public static Area geArea = new Area(3160, 3482, 3172, 3495);
    public static Tile geTile = new Tile(3163, 3485);
    
	public OpenGe(RietFlipper c) {
        super(c);
    }

    @Override
    public boolean validate() {
        return !getInventory().contains("Coins") || !getGrandExchange().isOpen();
    }

    @Override
    public int execute() {
    	 Inventory i = getInventory();
         GrandExchange g = getGrandExchange();
         c.log("" + this.getClass().getName() + "debug: ");
         if (!geArea.contains(getLocalPlayer())) {
             c.status = "Walking to the ge";
             getWalking().walkExact(c.geTile);
         } else {
             if (!i.contains("Coins")) {
                 if (getBank().isOpen()) {
                     c.status = "Checking bank for coins";
                     if (getBank().contains("Coins")) {
                         getBank().withdrawAll("Coins");
                         sleepUntil(() -> i.contains("Coins"), Calculations.random(3500, 5500));
                     }
                 } else {
                     if (getBank().open(BankLocation.GRAND_EXCHANGE)) {
                         c.status = "Opening bank";
                         sleepUntil(() -> getBank().isOpen(), Calculations.random(5000, 8500));
                     }
                 }
             } else {
                 if (getBank().isOpen()) {
                     c.status = "Closing bank";
                     getBank().close();
                 } else {
                     if (!g.isOpen()) {
                         c.status = "Opening GE";
                         g.open();
                         sleepUntil(() -> g.isOpen(), Calculations.random(5000, 8500));
                     }
                 }
             }
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
