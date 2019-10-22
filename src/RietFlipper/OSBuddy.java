package RietFlipper;

import io.protocol.json.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class OSBuddy {

    private static final String TRADEABLE_ITEMS = "https://rsbuddy.com/exchange/summary.json";
    private static final String path = "../summary.json";
    private static List<OSBuddyItem> OSBuddyItemList;
    private static URLConnection con;
	private static InputStream is;
	private static InputStreamReader isr;
	private static BufferedReader br;
	
    public static void init() {
        OSBuddyItemList = new LinkedList<>();
        HTTPResponse itemRequest = new HTTPRequest(TRADEABLE_ITEMS).read();
    
        JsonObject items = itemRequest.getJsonResponse().asObject();
        for (String id : items.names()) {
            JsonObject item = items.get(id).asObject();
            int itemId          = item.get("id").asInt();
            String itemName     = item.get("name").asString();
            int buyAverage      = item.get("buy_average").asInt();
            int buyQuantity     = item.get("buy_quantity").asInt();
            int sellAverage     = item.get("sell_average").asInt();
            int sellQuantity    = item.get("sell_quantity").asInt();
            int overallAverage  = item.get("overall_average").asInt();
            int overallQuantity = item.get("overall_quantity").asInt();
            boolean isMember = item.get("members").asBoolean();
            OSBuddyItemList.add(new OSBuddyItem(itemId,itemName,buyAverage,buyQuantity,sellAverage,sellQuantity,overallAverage,overallQuantity,isMember));

        }
    }


    public static int getItemId(String itemName) {
        return getItem(p -> p.getName().equals(itemName)).map(OSBuddyItem::getId).orElse(-1);
    }

    public static String getItemName(int itemId) {
        return getItem(p -> p.getId() == itemId).map(OSBuddyItem::getName).orElse(null);
    }

    public static Optional<OSBuddyItem> getItem(Predicate<OSBuddyItem> predicate) {
        return OSBuddyItemList.stream().filter(predicate).findFirst();
    }
    
    public static List<OSBuddyItem> getItemAll(Predicate<OSBuddyItem> predicate) {
        return OSBuddyItemList.stream().filter(predicate).collect(Collectors.toList());
    }

    
}
