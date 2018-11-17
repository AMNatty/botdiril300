package cz.tefek.botdiril.userdata.items;

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
import cz.tefek.botdiril.userdata.items.CraftingEntries.Recipe;
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
import cz.tefek.botdiril.userdata.items.crate.ItemCrateLegendary;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateUltimate;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateUncommon;
import cz.tefek.botdiril.userdata.items.crate.ItemCrateVoid;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfAbundance;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfIntelligence;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfIntelligence2;
import cz.tefek.botdiril.userdata.items.scrolls.ItemScrollOfRefreshing;

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
    public static Item crateGolden;
    public static Item crateEpic;
    public static Item crateLegendary;
    public static Item crateUltimate;
    public static Item crateHyper;
    public static Item crateGlitchy;
    public static Item crateInfernal;
    public static Item crateVoid;

    public static Item pickaxeI;
    public static Item pickaxeII;
    public static Item pickaxeIII;
    public static Item pickaxeIV;
    public static Item pickaxeV;

    public static Item redGem;
    public static Item greenGem;

    public static Item blueGem;
    public static Item purpleGem;

    public static Item rainbowGem;
    public static Item blackGem;

    public static Item gemdiril;

    public static Item toolBox;
    public static Item trash;

    public static Item scrollOfIntelligence;
    public static Item scrollOfRefreshing;
    public static Item scrollOfIntelligenceII;
    public static Item scrollOfAbundance;
    public static Item scrollOfCombining;

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

        gemdiril = new Item("gemdiril", Icons.GEM_GEMDIRIL, "Gemdiril").setDescription("A very rare gem of an unknown value.");
        trash = new Item("trash", Icons.ITEM_TRASH, "Trash").setDescription("It's just trash, or is it?");

        ShopEntries.addDisenchant(trash, 1000);

        pickaxeI = new ItemPickaxe("pickaxei", Icons.PICKAXE_I, "Pickaxe I", 10).setDescription("Basic Pickaxe.\nUsed for mining.");
        ShopEntries.addCoinBuy(pickaxeI, 2_000);
        ShopEntries.addCoinSell(pickaxeI, 800);

        pickaxeII = new ItemPickaxe("pickaxeii", Icons.PICKAXE_II, "Pickaxe II", 450).setDescription("Good Pickaxe.\nExpect much better loot.");
        ShopEntries.addCoinBuy(pickaxeII, 80_000);
        ShopEntries.addCoinSell(pickaxeII, 35_000);

        pickaxeIII = new ItemPickaxe("pickaxeiii", Icons.PICKAXE_III, "Pickaxe III", 35_000).setDescription("Hyper Pickaxe.\nNormally unobtainable, this pickaxe almost swings itself.");
        ShopEntries.addCoinSell(pickaxeIII, 250_000);

        pickaxeIV = new ItemPickaxe("pickaxeiv", Icons.PICKAXE_IV, "Pickaxe IV", 800_000).setDescription("Ascended Pickaxe\nPlease don't touch anything you don't want to disintegrate with this.");

        pickaxeV = new ItemPickaxe("pickaxev", Icons.PICKAXE_V, "Pickaxe V", 9_600_000).setDescription("Omega Pickaxe\nWho said bedrock was unbreakable?");

        toolBox = new Item("toolbox", Icons.ITEM_SUSPICIOUS_METAL_BOX, "Tool Box").setDescription("I wonder what it's for.");
        ShopEntries.addCoinSell(toolBox, 4_000);

        crateGolden = new ItemCrateGolden();

        crateBasic = new ItemCrateBasic();
        crateUncommon = new ItemCrateUncommon();
        crateEpic = new ItemCrateEpic();
        crateLegendary = new ItemCrateLegendary();
        crateUltimate = new ItemCrateUltimate();

        crateGlitchy = new ItemCrateGlitchy();
        crateInfernal = new ItemCrateInfernal();
        crateVoid = new ItemCrateVoid();
        crateHyper = new ItemCrateHyper();

        scrollOfIntelligence = new ItemScrollOfIntelligence();
        scrollOfRefreshing = new ItemScrollOfRefreshing();
        scrollOfAbundance = new ItemScrollOfAbundance();
        scrollOfIntelligenceII = new ItemScrollOfIntelligence2();
        scrollOfCombining = new Item("scrollofcombining", Icons.SCROLL, "Scroll of Combining").setDescription("Crafting ingredient for some magical recipes.");

        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(blueGem, 1)), 15, scrollOfCombining));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(redGem, 32), new ItemPair(greenGem, 32), new ItemPair(blueGem, 8), new ItemPair(purpleGem, 8), new ItemPair(rainbowGem, 1), new ItemPair(blackGem, 1)), 1, gemdiril));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(dust, 30_000)), 1, pickaxeI));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(pickaxeI, 64), new ItemPair(dust, 100_000)), 1, pickaxeII));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(pickaxeII, 120), new ItemPair(scrollOfCombining, 1), new ItemPair(dust, 500_000)), 1, pickaxeIII));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(trash, 10), new ItemPair(redGem, 1)), 1, toolBox));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(pickaxeIII, 24), new ItemPair(toolBox, 1), new ItemPair(scrollOfCombining, 16), new ItemPair(dust, 40_000_000)), 1, pickaxeIV));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(blueGem, 2), new ItemPair(purpleGem, 4), new ItemPair(rainbowGem, 2), new ItemPair(blackGem, 2), new ItemPair(dust, 800_000_000)), 1, pickaxeV));

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

                var recipe = new CraftingEntries.Recipe(Arrays.asList(new ItemPair[] {
                        new ItemPair(Items.dust, jobj.getInt("buy") * 10) }), 1, loli);

                CraftingEntries.add(recipe);

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
