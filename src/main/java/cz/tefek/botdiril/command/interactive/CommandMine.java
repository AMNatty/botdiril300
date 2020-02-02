package cz.tefek.botdiril.command.interactive;

import java.util.stream.Collectors;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.ItemPickaxe;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.mechanic.mine.MineAPI;
import cz.tefek.botdiril.userdata.mechanic.mine.MineInput;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "mine", aliases = {}, category = CommandCategory.INTERACTIVE, description = "Mine to get some sweet stuff.")
public class CommandMine
{
    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item)
    {
        CommandAssert.assertTimer(co.ui, Timers.mine, "You still need to wait **$** to **mine**.");

        BotMain.sql.lock();

        try
        {
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

            var resultStr = new StringBuilder();

            var repairKitCount = co.ui.howManyOf(Items.repairKit);

            var userLevel = co.ui.getLevel();

            var loot = new ItemDrops();

            var mineInput = new MineInput(pick, repairKitCount, userLevel);
            mineInput.setBlessedMiningSurge(Curser.isBlessed(co, EnumBlessing.MINE_SURGE));
            mineInput.setBlessedUnbreakablePickaxe(Curser.isBlessed(co, EnumBlessing.UNBREAKABLE_PICKAXE));
            mineInput.setCursedDoubleBreak(Curser.isCursed(co, EnumCurse.DOUBLE_PICKAXE_BREAK_CHANCE));
            mineInput.setPreferenceRepairKitEnabled(!UserPreferences.isBitEnabled(co.po, EnumUserPreference.REPAIR_KIT_DISABLED));

            var mineResult = MineAPI.mine(co, loot, mineInput);

            var lostItems = mineResult.getLostItems();
            var modifiers = mineResult.getMultipliers();

            if (mineResult.isInstantlyRefreshed())
            {
                resultStr.append("*You mine with such precision that you feel like mining again instantly.*\n");
                co.ui.resetTimer(Timers.mine);
            }

            resultStr.append(String.format("You are mining with a **%s**", pick.inlineDescription()));

            if (mineResult.isMiningWithoutRepairKit())
            {
                resultStr.append(" and *without a repair kit*");
            }

            resultStr.append(".\n");

            if (lostItems.hasItemDropped(Items.repairKit))
            {
                co.po.incrementLong(EnumStat.REPAIR_KITS_USED.getName());
                resultStr.append("**Your ");
                resultStr.append(pick.inlineDescription());
                resultStr.append(" broke while mining, but you managed to fix using a ");
                resultStr.append(Items.repairKit.inlineDescription());
                resultStr.append("**\n");
            }
            else if (lostItems.hasItemDropped(pick))
            {
                co.po.incrementLong(EnumStat.PICKAXES_BROKEN.getName());
                resultStr.append("**You broke the ");
                resultStr.append(pick.inlineDescription());
                resultStr.append(" while mining.**\n");
            }

            var xp = mineResult.getXP();
            XPAdder.addXP(co, xp);

            var lootStr = loot.stream().map(ip -> "**" + BotdirilFmt.format(ip.getAmount()) + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", "));

            resultStr.append("You found ");

            if (Curser.isCursed(co, EnumCurse.CANT_SEE_MINED_STUFF))
            {
                resultStr.append("*something*");
            }
            else
            {
                resultStr.append(lootStr);
            }

            resultStr.append(" and **");
            resultStr.append(BotdirilFmt.format(xp));
            resultStr.append(" XP**.\n");

            loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));
            lostItems.stream().forEach(ip -> co.ui.addItem(ip.getItem(), -ip.getAmount()));

            co.po.incrementLong(EnumStat.TIMES_MINED.getName());

            resultStr.append("[Modifiers: ");

            modifiers.forEach((key, value) -> resultStr.append(String.format("%s - %.2fx, ", key.getLocalizedName(), value)));

            resultStr.append(String.format("total multiplier - %.2fx ]\n", modifiers.values().stream().reduce(1.0, (acc, other) -> acc * other)));

            MR.send(co.textChannel, resultStr.toString());
        }
        finally
        {
            BotMain.sql.unlock();
        }

    }
}
