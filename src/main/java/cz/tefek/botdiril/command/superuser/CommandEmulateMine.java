package cz.tefek.botdiril.command.superuser;

import java.util.EnumMap;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.ItemPickaxe;
import cz.tefek.botdiril.userdata.mechanic.mine.EnumMineMultiplier;
import cz.tefek.botdiril.userdata.mechanic.mine.MineAPI;
import cz.tefek.botdiril.userdata.mechanic.mine.MineInput;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "emulatemine", aliases = {
        "emumine" }, category = CommandCategory.SUPERUSER, description = "Emulates the mine command.", powerLevel = EnumPowerLevel.SUPERUSER_OWNER)
public class CommandEmulateMine
{
    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item, @CmdPar("iterations") int iterationCount)
    {
        CommandAssert.numberMoreThanZeroL(iterationCount, "Iteration count must be greater than 0!");

        if (!(item instanceof ItemPickaxe))
        {
            throw new CommandException("That's not a valid pickaxe.");
        }

        var pick = (ItemPickaxe) item;

        var resultStr = new StringBuilder("***Mine emulation:\n***");

        var repairKitCount = Long.MAX_VALUE;

        var totalLoot = new ItemDrops();
        var totalModifiers = new EnumMap<EnumMineMultiplier, Double>(EnumMineMultiplier.class);
        var totalXP = 0l;
        var pickaxeBreaks = 0;
        var refreshes = 0;

        var userLevel = co.ui.getLevel();

        var mineInput = new MineInput(pick, repairKitCount, userLevel);
        mineInput.setBlessedMiningSurge(Curser.isBlessed(co, EnumBlessing.MINE_SURGE));
        mineInput.setBlessedUnbreakablePickaxe(Curser.isBlessed(co, EnumBlessing.UNBREAKABLE_PICKAXE));
        mineInput.setCursedDoubleBreak(Curser.isCursed(co, EnumCurse.DOUBLE_PICKAXE_BREAK_CHANCE));
        mineInput.setPreferenceRepairKitEnabled(!UserPreferences.isBitEnabled(co.po, EnumUserPreference.REPAIR_KIT_DISABLED));

        for (int i = 0; i < iterationCount; i++)
        {
            System.out.println(i);

            var mineResult = MineAPI.mine(co, totalLoot, mineInput);

            var lostItems = mineResult.getLostItems();

            var modifiers = mineResult.getMultipliers();

            modifiers.forEach((key, val) -> totalModifiers.put(key, totalModifiers.getOrDefault(key, 0.0) + val));

            totalXP += mineResult.getXP();

            if (lostItems.hasItemDropped(pick))
            {
                pickaxeBreaks++;
            }

            if (mineResult.isInstantlyRefreshed())
            {
                refreshes++;
            }
        }

        resultStr.append(String.format("You are mining **%s times** with a **%s**", BotdirilFmt.format(iterationCount), pick.inlineDescription()));

        if (UserPreferences.isBitEnabled(co.po, EnumUserPreference.REPAIR_KIT_DISABLED))
        {
            resultStr.append(" and *without a repair kit*");
        }

        resultStr.append(".\n");
        resultStr.append("**You broke your ");
        resultStr.append(pick.inlineDescription());
        resultStr.append(" ");
        resultStr.append(pickaxeBreaks);
        resultStr.append(" times while mining, for an average break rate of ");
        resultStr.append(String.format("%.2f%%", pickaxeBreaks * 100.0 / iterationCount));
        resultStr.append(".**\n");

        var lootStr = totalLoot.stream().map(ip -> "**" + BotdirilFmt.format(ip.getAmount()) + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", "));

        resultStr.append("You found ");
        resultStr.append(lootStr);
        resultStr.append(" and **");
        resultStr.append(BotdirilFmt.format(totalXP));
        resultStr.append(" XP** in total.\n");

        MR.send(co.textChannel, resultStr.toString());

        var resultStr2 = new StringBuilder();

        var avgStr = totalLoot.stream().map(ip -> "**" + BotdirilFmt.format(ip.getAmount() / (double) iterationCount) + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", "));

        resultStr2.append("You found ");
        resultStr2.append(avgStr);
        resultStr2.append(" and **");
        resultStr2.append(BotdirilFmt.format(totalXP / (double) iterationCount));
        resultStr2.append(" XP** on average.\n");

        resultStr2.append("**");
        resultStr2.append(BotdirilFmt.format(refreshes));
        resultStr2.append(" blessed refreshes occurred.**\n");

        co.po.incrementLong(EnumStat.TIMES_MINED.getName());

        resultStr2.append("[Average modifiers: ");

        totalModifiers.forEach((key, value) -> resultStr2.append(String.format("%s - %.2fx, ", key.getLocalizedName(), value / iterationCount)));

        resultStr2.append(String.format("total average multiplier - %.2fx ]\n", totalModifiers.values().stream().reduce(1.0, (acc, other) -> acc * (other / iterationCount))));

        MR.send(co.textChannel, resultStr2.toString());

    }
}
