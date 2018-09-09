package cz.tefek.botdiril.userdata.items;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.userdata.EnumCurrency;
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

    public static Item cardPackBasic;
    public static Item cardPackNormal;
    public static Item cardPackGood;
    public static Item cardPackVoid;

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

    public static Item toolBox;

    public static void load()
    {
        xp = new ItemCurrency(EnumCurrency.XP);
        coins = new ItemCurrency(EnumCurrency.COINS);
        keks = new ItemCurrency(EnumCurrency.KEKS);
        tokens = new ItemCurrency(EnumCurrency.TOKENS);
        megakeks = new ItemCurrency(EnumCurrency.MEGAKEKS);
        dust = new ItemCurrency(EnumCurrency.DUST);
        keys = new ItemCurrency(EnumCurrency.KEYS);

        cardPackBasic = new Item("basiccardpack", Icons.CARDPACK_BASIC, "Basic Card Pack").setDescription("Contains all the essential cards for your collections.");
        cardPackNormal = new Item("cardpack", Icons.CARDPACK_NORMAL, "Normal Card Pack").setDescription("Contains a variety of cards ranging from common to mythical/limited cards.");
        cardPackGood = new Item("goodcardpack", Icons.CARDPACK_GOOD, "Good Card Pack").setDescription("For the true collectors, drops legacy/rare card or better.");
        cardPackVoid = new Item("voidcardpack", Icons.CARDPACK_VOID, "Void Card Pack").setDescription("For a very dangerous place Void is, it contains some *awesome* loot. Open this pack at your own risk. It may turn into a Pandora's box.");

        pickaxeI = new Item("pickaxei", Icons.PICKAXE_I, "Pickaxe I").setDescription("Basic Pickaxe.\nUsed for mining.");
        ShopEntries.addCoinBuy(pickaxeI, 2_000);
        ShopEntries.addCoinSell(pickaxeI, 800);

        pickaxeII = new Item("pickaxeii", Icons.PICKAXE_II, "Pickaxe II").setDescription("Good Pickaxe.\nExpect much better loot.");
        ShopEntries.addCoinBuy(pickaxeII, 80_000);
        ShopEntries.addCoinSell(pickaxeII, 35_000);

        pickaxeIII = new Item("pickaxeiii", Icons.PICKAXE_III, "Pickaxe III").setDescription("Hyper Pickaxe.\nNormally unobtainable, this pickaxe almost swings itself.");
        ShopEntries.addCoinSell(pickaxeIII, 250_000);

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

        try (var br = new FileReader("assets/itemdata-g.json"))
        {
            var jarr = new JSONArray(new JSONTokener(br));

            leagueItems = new ArrayList<>(jarr.length());

            jarr.forEach(jo -> {
                var jobj = (JSONObject) jo;

                var loli = new Item(jobj.getString("id"), Icons.ITEM_LOL, jobj.getString("name"));
                loli.setDescription(jobj.getString("shortdesc"));
                ShopEntries.addDisenchant(loli, jobj.getInt("sell") * 10);

                var recipe = new CraftingEntries.Recipe(Arrays.asList(new ItemPair[] { new ItemPair(Items.dust, jobj.getInt("buy") * 10) }), 1, loli);

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
