package cz.tefek.botdiril.command.interactive;

import java.util.stream.Collectors;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ItemPickaxe;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;

@Command(value = "mine", aliases = {}, category = CommandCategory.INTERACTIVE, description = "Mine to get some sweet stuff.")
public class CommandMine
{
    private static final Item[] minerals = { Items.coal, Items.iron, Items.copper, Items.gold, Items.platinum,
            Items.uranium, Items.kekium, Items.emerald, Items.diamond };

    private static void generateMinerals(ItemDrops drops, double budget, int iteration)
    {
        if (budget <= 0 || iteration > 20)
        {
            return;
        }

        var sum = 0L;

        for (var mineral : minerals)
        {
            double sellVal = ShopEntries.getSellValue(mineral);
            var roll = Botdiril.RDG.nextExponential(budget / sellVal / minerals.length);
            var count = Math.round(roll);

            if (count == 0)
            {
                continue;
            }

            sum += count * sellVal;
            drops.addItem(mineral, count);
        }

        generateMinerals(drops, budget - sum, iteration + 1);
    }

    private static void generateMinerals(ItemDrops drops, long pickaxeBudget)
    {
        generateMinerals(drops, pickaxeBudget * Botdiril.RDG.nextUniform(0.8, 1.25), 0);
    }

    @CmdInvoke
    public static void mine(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.mine, "You still need to wait **$** to mine.");

        var roll = Botdiril.RDG.nextExponential(Math.pow((co.ui.getLevel() + 600) / 150.0, 2));

        var loot = new ItemDrops();
        loot.addItem(Items.keks, Math.round(roll + 10));
        loot.addItem(Items.coins, Math.round(roll * 10));

        if (tryChance(0.30, 0))
        {
            if (tryChance(0.9, 0))
            {
                loot.addItem(Items.keys, 1);
            }
            else
            {
                if (tryChance(0.95, 0))
                {
                    loot.addItem(Items.oil, 1);
                }
                else
                {
                    loot.addItem(Items.goldenOil, 1);
                }
            }
        }

        var xp = Math.round((roll + 1000) / 8);

        XPAdder.addXP(co, xp);

        loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));

        var lootStr = loot.stream().map(ip -> "**" + ip.getAmount() + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", ")) + " and **" + xp + " XP**";

        co.po.incrementLong(EnumStat.TIMES_MINED.getName());

        MR.send(co.textChannel, "You are mining without a pickaxe.\nYou found " + lootStr);
    }

    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item)
    {
        CommandAssert.assertTimer(co.ui, Timers.mine, "You still need to wait **$** to mine.");

        if (!(item instanceof ItemPickaxe))
        {
            co.ui.resetTimer(Timers.mine);
            throw new CommandException("That's not a valid pickaxe.");
        }

        if (co.ui.howManyOf(item) <= 0)
        {
            co.ui.resetTimer(Timers.mine);
            throw new CommandException("You don't have that pickaxe.");
        }

        var pick = (ItemPickaxe) item;

        var mult = pick.getMultiplier();
        var pickaxeBudget = pick.getPickaxeValue();

        String formatStr;
        var repairKitCount = co.ui.howManyOf(Items.repairKit);

        if (repairKitCount > 0 || mult < 2.5)
        {
            formatStr = "You are mining with a **%s**.\nYou found %s.";
        }
        else
        {
            formatStr = "You are mining with a **%s** and *without a repair kit* for additional rewards.\nYou found %s.";
            mult += 1;
            pickaxeBudget *= 3;
        }

        var chanceToBreak = Curser.isCursed(co, EnumCurse.DOUBLE_PICKAXE_BREAK_CHANCE) ? pick.getChanceToBreak() * 2
                : pick.getChanceToBreak();

        if (tryChance(chanceToBreak) && !Curser.isBlessed(co, EnumBlessing.UNBREAKABLE_PICKAXE))
        {
            if (repairKitCount > 0)
            {
                co.po.incrementLong(EnumStat.REPAIR_KITS_USED.getName());
                co.ui.addItem(Items.repairKit, -1);
                formatStr += "\n**A " + Items.repairKit.inlineDescription() + " saved you from breaking your " + pick.inlineDescription() + " while mining.**";
            }
            else
            {

                formatStr += "\n**You broke the " + pick.inlineDescription() + " while mining.**";
                co.ui.addItem(item, -1);
                co.po.incrementLong(EnumStat.PICKAXES_BROKEN.getName());
            }
        }

        if (Curser.isBlessed(co, EnumBlessing.MINE_SURGE) && tryChance(0.4))
        {
            formatStr += "\n*You mine with such precision that you feel like mining again instantly.*";
            co.ui.resetTimer(Timers.mine);
        }

        var loot = new ItemDrops();

        generateMinerals(loot, pickaxeBudget);

        if (tryChance(0.02, 0))
        {
            loot.addItem(Items.timewarpCrystal, 1);
        }

        if (tryChance(0.03, mult / 2))
        {
            loot.addItem(Items.keys, 1);
        }

        if (tryChance(0.01, mult))
        {
            loot.addItem(Items.redGem, 1);
        }

        if (tryChance(0.015, 0))
        {
            if (tryChance(0.9, 0))
            {
                loot.addItem(Items.oil, 1);
            }
            else
            {
                loot.addItem(Items.goldenOil, 1);
            }
        }

        if (tryChance(0.01, mult))
        {
            loot.addItem(Items.greenGem, 1);
        }

        if (tryChance(0.001, mult))
        {
            loot.addItem(Items.blueGem, 1);
        }

        if (tryChance(0.001, mult))
        {
            loot.addItem(Items.purpleGem, 1);
        }

        if (tryChance(0.0002, mult))
        {
            loot.addItem(Items.rainbowGem, 1);
        }

        if (tryChance(0.0002, mult))
        {
            loot.addItem(Items.blackGem, 1);
        }

        if (tryChance(0.00003, mult))
        {
            loot.addItem(Items.scrollOfIntelligenceII, 1);
        }

        if (tryChance(0.00001, mult))
        {
            loot.addItem(Items.gemdiril, 1);
        }

        var xp = Math.round(Math.pow(14, 1.8 + mult / 2.25));

        XPAdder.addXP(co, xp);

        loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));

        var lootStr = loot.stream().map(ip -> "**" + ip.getAmount() + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", ")) + " and **" + xp + " XP**";

        co.po.incrementLong(EnumStat.TIMES_MINED.getName());

        MR.send(co.textChannel, String.format(formatStr, item.inlineDescription(), Curser.isCursed(co, EnumCurse.CANT_SEE_MINED_STUFF)
                ? "*<something?>*"
                : lootStr));
    }

    private static boolean tryChance(double chance)
    {
        return Botdiril.RDG.nextUniform(0, 1) > 1 - chance;
    }

    private static boolean tryChance(double chance, double pickaxe)
    {
        return tryChance(1 - Math.pow(1 - chance, 1 + pickaxe));
    }
}
