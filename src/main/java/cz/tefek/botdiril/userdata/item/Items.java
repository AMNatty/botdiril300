package cz.tefek.botdiril.userdata.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.items.cardpack.ItemCardPackBasic;
import cz.tefek.botdiril.userdata.items.cardpack.ItemCardPackGood;
import cz.tefek.botdiril.userdata.items.cardpack.ItemCardPackNormal;
import cz.tefek.botdiril.userdata.items.cardpack.ItemCardPackVoid;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateBasic;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateEpic;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateGlitchy;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateGolden;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateHyper;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateInfernal;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateIron;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateLeague;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateLegendary;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateUltimate;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateUncommon;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateVoid;
import cz.tefek.botdiril.userdata.items.pickaxe.Pickaxes;
import cz.tefek.botdiril.userdata.items.scrolls.ItemLesserScrollOfIntelligence;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfAbundance;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfBlessing;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfIntelligence;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfIntelligence2;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfRefreshing;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfSwapping;

public class Items
{
    public static List<Item> leagueItems;

    public static ItemCurrency xp;
    public static ItemCurrency coins;
    public static ItemCurrency keks;
    public static ItemCurrency tokens;
    public static ItemCurrency megakeks;
    public static ItemCurrency dust;
    public static ItemCurrency keys;

    public static ItemCardPackBasic cardPackBasic;
    public static ItemCardPackNormal cardPackNormal;
    public static ItemCardPackGood cardPackGood;
    public static ItemCardPackVoid cardPackVoid;

    public static Item crateBasic;
    public static Item crateUncommon;
    public static Item crateIron;
    public static Item crateGolden;
    public static Item crateEpic;
    public static Item crateLegendary;
    public static Item crateUltimate;
    public static Item crateHyper;
    public static Item crateGlitchy;
    public static Item crateInfernal;
    public static Item crateVoid;
    public static Item crateLeague;

    public static Item redGem;
    public static Item greenGem;

    public static Item blueGem;
    public static Item purpleGem;

    public static Item rainbowGem;
    public static Item blackGem;

    public static Item timewarpCrystal;

    public static Item gemdiril;

    public static Item toolBox;
    public static Item trash;

    public static Item scrollOfLesserIntelligence;
    public static Item scrollOfIntelligence;
    public static Item scrollOfRefreshing;
    public static Item scrollOfIntelligenceII;
    public static Item scrollOfAbundance;
    public static Item scrollOfCombining;
    public static Item scrollOfSwapping;
    public static Item scrollOfBlessing;

    public static Item coal;
    public static Item iron;
    public static Item copper;
    public static Item gold;
    public static Item platinum;
    public static Item uranium;
    public static Item kekium;
    public static Item emerald;
    public static Item diamond;

    public static Item repairKit;

    public static Item max;
    public static Item oil;
    public static Item goldenOil;

    public static void load()
    {
        xp = new ItemCurrency(EnumCurrency.XP);
        coins = new ItemCurrency(EnumCurrency.COINS);
        keks = new ItemCurrency(EnumCurrency.KEKS);
        tokens = new ItemCurrency(EnumCurrency.TOKENS);
        megakeks = new ItemCurrency(EnumCurrency.MEGAKEKS);
        dust = new ItemCurrency(EnumCurrency.DUST);
        keys = new ItemCurrency(EnumCurrency.KEYS);

        cardPackBasic = new ItemCardPackBasic();
        cardPackNormal = new ItemCardPackNormal();
        cardPackGood = new ItemCardPackGood();
        cardPackVoid = new ItemCardPackVoid();

        redGem = new Item("infernalgem", Icons.GEM_RED, "Infernal Gem").setDescription("Unleash the fury upon your foes.");
        greenGem = new Item("peacegem", Icons.GEM_GREEN, "Peace Gem").setDescription("Avoid conflicts.");

        blueGem = new Item("equlibriumgem", Icons.GEM_BLUE, "Equlibrium Gem").setDescription("Remove any differences.");
        purpleGem = new Item("imbalancegem", Icons.GEM_PURPLE, "Imbalance Gem").setDescription("The source of imbalance permeating the universe.");

        rainbowGem = new Item("ordergem", Icons.GEM_RAINBOW, "Order Gem").setDescription("Natural enemy of chaos.");
        blackGem = new Item("chaosgem", Icons.GEM_BLACK, "Chaos Gem").setDescription("The source of all chaos.");

        timewarpCrystal = new Item("timewarpcrystal", Icons.GEM_TIMEWARP, "Timewarp Crystal").setDescription("Manipulate the spacetime!");

        gemdiril = new Item("gemdiril", Icons.GEM_GEMDIRIL, "Gemdiril").setDescription("A very rare gem of an unknown value.");

        trash = new Item("trash", Icons.ITEM_TRASH, "Trash").setDescription("It's just trash, or is it?");
        ShopEntries.addDisenchant(trash, 1000);

        oil = new Item("oil", Icons.OTHER_OIL, "Oil").setDescription("This better not start a nuclear war...");
        ShopEntries.addCoinSell(oil, 800);

        goldenOil = new Item("goldenoil", Icons.OTHER_GOLDENOIL, "Golden Oil").setDescription("Passively grants +1% bonus sell value, but also grants 0.5% chance for all golden oil barrels to explode, leaving you with no barrels and making you lose 15% from that trade.");

        toolBox = new Item("toolbox", Icons.ITEM_SUSPICIOUS_METAL_BOX, "Tool Box").setDescription("I wonder what it's for.");
        ShopEntries.addCoinSell(toolBox, 4_000);

        coal = new Item("coal", Icons.MINE_COAL, "Coal");
        coal.setDescription("A very common resource, used as a fuel.");
        ShopEntries.addCoinSell(coal, 5);

        iron = new Item("iron", Icons.MINE_IRON, "Iron");
        iron.setDescription("One of the most common minerals, used pretty much everywhere.");
        ShopEntries.addCoinSell(iron, 10);

        copper = new Item("copper", Icons.MINE_COPPER, "Copper");
        copper.setDescription("A soft metal, usually used in electric devices and alloys (such as bronze, brass, etc.).");
        ShopEntries.addCoinSell(copper, 60);

        gold = new Item("gold", Icons.MINE_GOLD, "Gold");
        gold.setDescription("A rare metal with good properties, very popular in jewelry.");
        ShopEntries.addCoinSell(gold, 1_000);

        platinum = new Item("platinum", Icons.MINE_PLATINUM, "Platinum");
        platinum.setDescription("A very rare metal with very good properties.");
        ShopEntries.addCoinSell(platinum, 4_000);

        uranium = new Item("uranium", Icons.MINE_URANIUM, "Uranium");
        uranium.setDescription("An extremely dense metal used in nuclear physics.");
        ShopEntries.addCoinSell(uranium, 500);

        kekium = new Item("kekium", Icons.MINE_KEKIUM, "Kekium");
        kekium.setDescription("An exceptionally rare metal found in the deepest of mines.");
        ShopEntries.addCoinSell(kekium, 100_000);
        ShopEntries.addTokenBuy(kekium, 80_000);

        emerald = new Item("emerald", Icons.MINE_EMERALD, "Emerald");
        emerald.setDescription("An extremely rare gemstone. Emerald is a variant of beryl.");
        ShopEntries.addCoinSell(emerald, 1_000_000);

        diamond = new Item("diamond", Icons.MINE_DIAMOND, "Diamond");
        diamond.setDescription("A crystallic form of carbon, diamonds are the hardest known mineral known to humans.");
        ShopEntries.addCoinSell(diamond, 10_000_000);

        crateBasic = new ItemCrateBasic();
        crateIron = new ItemCrateIron();
        crateUncommon = new ItemCrateUncommon();
        crateEpic = new ItemCrateEpic();
        crateLegendary = new ItemCrateLegendary();
        crateGolden = new ItemCrateGolden();
        crateUltimate = new ItemCrateUltimate();

        crateGlitchy = new ItemCrateGlitchy();
        crateInfernal = new ItemCrateInfernal();
        crateVoid = new ItemCrateVoid();
        crateHyper = new ItemCrateHyper();

        crateLeague = new ItemCrateLeague();

        repairKit = new Item("repairkit", Icons.ITEM_REPAIR_KIT, "Repair Kit").setDescription("It's handy to have one of these at your disposal when handling fragile tools. Will be **automatically** used and consumed to avoid breaking a tool.");
        CraftingEntries.add(new Recipe(List.of(new ItemPair(toolBox, 1), new ItemPair(oil, 1)), 1, repairKit));

        scrollOfLesserIntelligence = new ItemLesserScrollOfIntelligence();
        scrollOfIntelligence = new ItemScrollOfIntelligence();
        scrollOfRefreshing = new ItemScrollOfRefreshing();
        scrollOfAbundance = new ItemScrollOfAbundance();
        scrollOfIntelligenceII = new ItemScrollOfIntelligence2();
        scrollOfCombining = new Item("scrollofcombining", Icons.SCROLL, "Scroll of Combining").setDescription("Crafting ingredient for some magical recipes.");
        scrollOfSwapping = new ItemScrollOfSwapping();
        scrollOfBlessing = new ItemScrollOfBlessing();

        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(purpleGem, 1)), 3, scrollOfCombining));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(redGem, 2048), new ItemPair(greenGem, 2048), new ItemPair(blueGem, 1024), new ItemPair(purpleGem, 1024), new ItemPair(rainbowGem, 512), new ItemPair(blackGem, 512), new ItemPair(kekium, 12345678)), 1, gemdiril));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(trash, 20), new ItemPair(redGem, 8), new ItemPair(copper, 30)), 1, toolBox));

        max = new Item("Max", Icons.OTHER_MAX, "Max the Doggo").setDescription("The goodest boy on Earth.");

        Pickaxes.load();

        try (var br = new FileReader("assets/itemdata-g.json"))
        {
            var jarr = new JSONArray(new JSONTokener(br));

            leagueItems = new ArrayList<>(jarr.length());

            jarr.forEach(jo ->
            {
                var jobj = (JSONObject) jo;

                var loli = new Item(jobj.getString("id"), Icons.ITEM_LOL, jobj.getString("name"));
                loli.setDescription(jobj.getString("shortdesc"));
                ShopEntries.addDisenchant(loli, jobj.getInt("sell") * 10);

                // var recipe = new CraftingEntries.Recipe(Arrays.asList(new ItemPair[] {
                //        new ItemPair(Items.dust, jobj.getInt("buy") * 10) }), 1, loli);

                // CraftingEntries.add(recipe);

                leagueItems.add(loli);
            });
        }
        catch (IOException e)
        {
            BotMain.logger.fatal("League of Legends item data not found or malformed! Aborting.", e);
            System.exit(8);
        }
    }
}
