package cz.tefek.botdiril.userdata.items;

import java.util.HashMap;
import java.util.Map;

import cz.tefek.botdiril.userdata.IIdentifiable;

public class ShopEntries
{
    private static Map<Integer, Long> buysCoins = new HashMap<>();
    private static Map<Integer, Long> sellsCoins = new HashMap<>();
    private static Map<Integer, Long> buysTokens = new HashMap<>();

    private static Map<Integer, Long> yieldsDust = new HashMap<>();

    // ADDING

    public static void addCoinSell(IIdentifiable item, long amount)
    {
        sellsCoins.put(item.getID(), amount);
    }

    public static void addCoinBuy(IIdentifiable item, long amount)
    {
        buysCoins.put(item.getID(), amount);
    }

    public static void addTokenBuy(IIdentifiable item, long amount)
    {
        buysTokens.put(item.getID(), amount);
    }

    public static void addDisenchant(IIdentifiable item, long amount)
    {
        yieldsDust.put(item.getID(), amount);
    }

    // CHECKING

    public static boolean canBeBought(IIdentifiable item)
    {
        return buysCoins.containsKey(item.getID());
    }

    public static boolean canBeSold(IIdentifiable item)
    {
        return sellsCoins.containsKey(item.getID());
    }

    public static boolean canBeBoughtForTokens(IIdentifiable item)
    {
        return buysTokens.containsKey(item.getID());
    }

    public static boolean canBeDisenchanted(IIdentifiable item)
    {
        return yieldsDust.containsKey(item.getID());
    }

    // REMOVING

    public static Long getCoinPrice(IIdentifiable item)
    {
        return buysCoins.get(item.getID());
    }

    public static Long getSellValue(IIdentifiable item)
    {
        return sellsCoins.get(item.getID());
    }

    public static Long getTokenPrice(IIdentifiable item)
    {
        return buysTokens.get(item.getID());
    }

    public static Long getDustForDisenchanting(IIdentifiable item)
    {
        return yieldsDust.get(item.getID());
    }
}
