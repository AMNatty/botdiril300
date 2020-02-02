package cz.tefek.botdiril.userdata.mechanic.mine;

import java.util.EnumMap;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.item.ShopEntries;

public class MineAPI
{
    public static final double KITLESS_BONUS_THRESHOLD = 2;
    public static final double CHANCE_INSTAMINE = 0.4;
    public static final double KITLESS_MULTIPLIER = 1.5;

    private static final Item[] minerals = { Items.coal, Items.iron, Items.copper, Items.gold, Items.platinum,
            Items.uranium, Items.kekium, Items.emerald, Items.diamond };

    private static final Item[] evilGems = { Items.redGem, Items.purpleGem, Items.blackGem };

    private static final Item[] goodGems = { Items.greenGem, Items.blueGem, Items.rainbowGem };

    private static boolean tryChance(double chance)
    {
        return Botdiril.RDG.nextUniform(0, 1) < chance;
    }

    private static void generateMinerals(ItemDrops drops, double pickaxeBudget)
    {
        var budget = pickaxeBudget;

        for (var mineral : minerals)
        {
            double sellVal = ShopEntries.getSellValue(mineral);
            var budgetSellValRatio = budget / sellVal;
            var start = System.currentTimeMillis();
            var roll = budgetSellValRatio > 5 ? Botdiril.RDG.nextExponential(budgetSellValRatio)
                    : Botdiril.RDG.nextPoisson(budgetSellValRatio);
            var end = System.currentTimeMillis();
            System.out.println(end - start);
            var count = (long) Math.floor(roll);

            if (count == 0)
            {
                continue;
            }

            drops.addItem(mineral, count);
        }
    }

    private static double getMeanGemChance(double pickaxeBudget, int gemTier)
    {
        var generalOccurenceCount = Math.max(Math.log(Math.max(Math.log(pickaxeBudget / 100.0) - (gemTier * 4), 1)), 0);
        var saturatedMax = Math.pow(0.5, gemTier + 1) * 5;
        var growthModifier = 1 - gemTier / 8;

        return Math.pow(generalOccurenceCount, growthModifier) * saturatedMax;
    }

    private static void generateGems(ItemDrops drops, double pickaxeBudget)
    {
        for (int gemTier = 0; gemTier < goodGems.length; gemTier++)
        {
            var mean = getMeanGemChance(pickaxeBudget, gemTier);

            if (mean == 0)
            {
                continue;
            }

            drops.addItem(goodGems[gemTier], Botdiril.RDG.nextPoisson(mean));
        }

        for (int gemTier = 0; gemTier < evilGems.length; gemTier++)
        {
            var mean = getMeanGemChance(pickaxeBudget, gemTier);

            if (mean == 0)
            {
                continue;
            }

            drops.addItem(evilGems[gemTier], Botdiril.RDG.nextPoisson(mean));
        }
    }

    public static MineResult mine(CallObj co, ItemDrops loot, MineInput inputData)
    {
        var pickaxe = inputData.getPickaxe();

        var chanceToBreak = pickaxe.getChanceToBreak();
        var rareDropMultiplier = pickaxe.getRareDropMultiplier();
        var pickaxeBudget = pickaxe.getPickaxeValue();

        var multiplierMap = new EnumMap<EnumMineMultiplier, Double>(EnumMineMultiplier.class);

        if (inputData.isCursedDoubleBreak())
        {
            chanceToBreak *= 2;
        }

        var useRepairKit = inputData.isPreferenceRepairKitEnabled();

        useRepairKit &= inputData.getRepairKitsAvailable() > 0;
        useRepairKit &= !inputData.isBlessedUnbreakablePickaxe();

        var multiplier = 1.0;

        if (!useRepairKit && rareDropMultiplier >= KITLESS_BONUS_THRESHOLD)
        {
            rareDropMultiplier += 1;
            multiplier *= KITLESS_MULTIPLIER;
            multiplierMap.put(EnumMineMultiplier.MLT_KITLESS, KITLESS_MULTIPLIER);
        }

        var randomModifier = Botdiril.RDG.nextGaussian(1, 0.1);
        multiplier *= randomModifier;

        var levelModifier = 1 + Math.log(1 + inputData.getUserLevel()) / 3.0;
        multiplier *= levelModifier;

        generateMinerals(loot, pickaxeBudget * multiplier);
        generateGems(loot, pickaxeBudget * randomModifier);

        loot.addItem(Items.timewarpCrystal, Botdiril.RDG.nextPoisson(0.02 + 0.01 * rareDropMultiplier));
        loot.addItem(Items.keys, Botdiril.RDG.nextPoisson(0.03));
        loot.addItem(Items.oil, Botdiril.RDG.nextPoisson(0.027));
        loot.addItem(Items.goldenOil, Botdiril.RDG.nextPoisson(0.003));
        loot.addItem(Items.scrollOfLesserIntelligence, Botdiril.RDG.nextPoisson(0.05 * rareDropMultiplier));
        loot.addItem(Items.scrollOfIntelligence, Botdiril.RDG.nextPoisson(0.005 * rareDropMultiplier));
        loot.addItem(Items.scrollOfIntelligenceII, Botdiril.RDG.nextPoisson(0.00003));
        loot.addItem(Items.trash, Botdiril.RDG.nextPoisson(0.04 * rareDropMultiplier));
        loot.addItem(Items.max, Botdiril.RDG.nextPoisson(0.00001));

        var xp = Math.round(Math.pow(Botdiril.RDG.nextGaussian(15, 2), 1.8 + rareDropMultiplier / 2.25));

        var lostItems = new ItemDrops();

        if (tryChance(chanceToBreak) && !inputData.isBlessedUnbreakablePickaxe())
        {
            if (useRepairKit)
            {
                lostItems.addItem(Items.repairKit);
            }
            else
            {
                lostItems.addItem(pickaxe);
            }
        }

        boolean instantlyRefreshed = false;

        if (inputData.isBlessedMiningSurge() && tryChance(CHANCE_INSTAMINE))
        {
            instantlyRefreshed = true;
        }

        multiplierMap.put(EnumMineMultiplier.MLT_EXPERIENCE, levelModifier);
        multiplierMap.put(EnumMineMultiplier.MLT_RANDOM, randomModifier);

        return new MineResult(xp, lostItems, !useRepairKit, multiplierMap, instantlyRefreshed);
    }
}
