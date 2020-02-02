package cz.tefek.botdiril.userdata.items.pickaxe;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.item.ItemPickaxe;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.item.ShopEntries;

public class Pickaxes
{

    public static ItemPickaxe pickaxeI;
    public static ItemPickaxe pickaxeII;
    public static ItemPickaxe pickaxeIII;
    public static ItemPickaxe pickaxeIV;
    public static ItemPickaxe pickaxeV;

    public static ItemPickaxe pickaxeVI;
    public static ItemPickaxe pickaxeVII;
    public static ItemPickaxe pickaxeVIII;
    public static ItemPickaxe pickaxeIX;
    public static ItemPickaxe pickaxeX;

    public static ItemPickaxe pickaxeXI;
    public static ItemPickaxe pickaxeXII;
    public static ItemPickaxe pickaxeXIII;
    public static ItemPickaxe pickaxeXIV;
    public static ItemPickaxe pickaxeXV;

    public static ItemPickaxe pickaxeXVI;
    public static ItemPickaxe pickaxeXVII;
    public static ItemPickaxe pickaxeXVIII;
    public static ItemPickaxe pickaxeXIX;
    public static ItemPickaxe pickaxeXX;

    public static void load()
    {
        final String[] minorNames = { "Clumsy ", "Lesser ", "", "Improved ", "Master " };
        final String[] majorNames = { "Basic ", "Good ", "Epic ", "Legendary ", "Arcane " };

        var gson = new Gson();
        List<PickaxeMetadata> pickaxeData = null;

        try (var fr = new FileReader("assets/miningData/pickaxes.json"))
        {
            pickaxeData = Arrays.asList(gson.fromJson(fr, PickaxeMetadata[].class));

            for (var pickaxe : pickaxeData)
            {
                var numeral = pickaxe.getRomanNumeral();
                var pickaxeField = Pickaxes.class.getField("pickaxe" + numeral);

                var id = "pickaxe" + numeral.toLowerCase();
                var name = "Pickaxe " + numeral;

                String icon = null;
                int pickaxeTier = pickaxe.getTier();

                switch (pickaxeTier)
                {
                    case 0:
                        icon = Icons.PICKAXE_I;
                        break;
                    case 1:
                        icon = Icons.PICKAXE_II;
                        break;
                    case 2:
                        icon = Icons.PICKAXE_III;
                        break;
                    case 3:
                        icon = Icons.PICKAXE_IV;
                        break;
                    case 4:
                        icon = Icons.PICKAXE_V;
                        break;
                    default:
                        BotMain.logger.fatal("Pickaxe item data not found or malformed! Aborting: Missing pickaxe icon!");
                        System.exit(13);
                        break;
                }

                var multiplier = pickaxe.getMultiplier();
                var budget = pickaxe.getBudget();
                var breakChance = pickaxe.getBreakChance();

                var item = new ItemPickaxe(id, icon, name, multiplier, budget, breakChance);

                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                otherSymbols.setDecimalSeparator('.');
                otherSymbols.setGroupingSeparator('\'');
                DecimalFormat df = new DecimalFormat("#.##", otherSymbols);

                var sb = new StringBuilder();
                sb.append(minorNames[pickaxe.getMiniTier()]);
                sb.append(majorNames[pickaxe.getTier()]);
                sb.append(" Pickaxe\n");

                sb.append("**Mining value:**\n");
                sb.append(String.format("%s\n", df.format(budget)));

                sb.append("**Break chance:**\n");
                sb.append(String.format("%s%%\n", df.format(breakChance * 100)));

                sb.append("**Rare drop multiplier:**\n");
                sb.append(String.format("%s\n", df.format(multiplier)));

                item.setDescription(sb.toString());

                pickaxeField.set(null, item);
            }
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("Pickaxe item data not found or malformed! Aborting.", e);
            System.exit(13);
        }

        ShopEntries.addCoinBuy(pickaxeI, 500);
        CraftingEntries.add(new Recipe(List.of(new ItemPair(Items.coal, 80)), 1, pickaxeI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeI, 2), new ItemPair(Items.iron, 40)), 1, pickaxeII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeII, 3), new ItemPair(Items.copper, 40)), 1, pickaxeIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeIII, 4), new ItemPair(Items.uranium, 10)), 1, pickaxeIV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(Items.coal, 8000), new ItemPair(Items.iron, 4000), new ItemPair(Items.copper, 150), new ItemPair(Items.uranium, 15), new ItemPair(Items.gold, 5), new ItemPair(Items.redGem, 1), new ItemPair(Items.greenGem, 1)), 1, pickaxeV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeV, 3), new ItemPair(Items.copper, 80)), 1, pickaxeVI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVI, 3), new ItemPair(Items.uranium, 100)), 1, pickaxeVII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVII, 3), new ItemPair(Items.gold, 500)), 1, pickaxeVIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVIII, 3), new ItemPair(Items.platinum, 400)), 1, pickaxeIX));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(Items.coal, 256_000), new ItemPair(Items.iron, 192_000), new ItemPair(Items.copper, 16_384), new ItemPair(Items.uranium, 3_072), new ItemPair(Items.gold, 1_024), new ItemPair(Items.redGem, 2), new ItemPair(Items.greenGem, 2), new ItemPair(Items.blueGem, 1), new ItemPair(Items.purpleGem, 1)), 1, pickaxeX));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeX, 3), new ItemPair(Items.gold, 120)), 1, pickaxeXI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXI, 3), new ItemPair(Items.platinum, 250)), 1, pickaxeXII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXII, 3), new ItemPair(Items.kekium, 150)), 1, pickaxeXIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXIII, 3), new ItemPair(Items.emerald, 300)), 1, pickaxeXIV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(Items.coal, 65_536_000), new ItemPair(Items.iron, 48_912_000), new ItemPair(Items.copper, 5_242_880), new ItemPair(Items.uranium, 655_360), new ItemPair(Items.gold, 320_768), new ItemPair(Items.platinum, 64_000), new ItemPair(Items.redGem, 4), new ItemPair(Items.greenGem, 4), new ItemPair(Items.blueGem, 2), new ItemPair(Items.purpleGem, 2), new ItemPair(Items.rainbowGem, 1), new ItemPair(Items.blackGem, 1)), 1, pickaxeXV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXV, 3), new ItemPair(Items.kekium, 1_600)), 1, pickaxeXVI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVI, 4), new ItemPair(Items.emerald, 150)), 1, pickaxeXVII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVII, 5), new ItemPair(Items.diamond, 200)), 1, pickaxeXVIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVIII, 6), new ItemPair(Items.goldenOil, 100)), 1, pickaxeXIX));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXIX, 1), new ItemPair(Items.timewarpCrystal, 1000)), 1, pickaxeXX));

    }

}
