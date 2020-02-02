package cz.tefek.botdiril.command.interactive;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.pools.CratePools;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "farm", category = CommandCategory.INTERACTIVE, aliases = {}, description = "Farm to get some gold, experience and items.")
public class CommandFarm
{
    @CmdInvoke
    public static void farm(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.farm, "You need to wait **$** to **farm** again.");

        var cannon = Botdiril.RDG.nextUniform(0, 1) < 0.25;

        var coins = Botdiril.RDG.nextLong(100 + co.ui.getLevel() * 200, 300 + co.ui.getLevel() * 350);

        var keks = Botdiril.RDG.nextLong(500 + co.ui.getLevel() * 400, 800 + co.ui.getLevel() * 700);

        var xp = Botdiril.RDG.nextLong(200 + co.ui.getLevel() * 20, 400 + co.ui.getLevel() * 35);

        var drops = CratePools.leagueRewards.draw();

        co.ui.addItem(drops);
        co.ui.addKeks(keks);
        co.ui.addCoins(coins);
        XPAdder.addXP(co, xp);

        final var cannonString = "You farmed and didn't actually miss the cannon minion so you got more loot: **%s %s**, **%s %s** and **%s**. **[%s XP]**";
        final var basicString = "You farmed and got **%s %s**, **%s %s** and **%s**. **[%s XP]**";

        var op = String.format(cannon ? cannonString
                : basicString, BotdirilFmt.format(coins), EnumCurrency.COINS.getIcon(), BotdirilFmt.format(keks), EnumCurrency.KEKS.getIcon(), drops.inlineDescription(), BotdirilFmt.format(xp));

        co.po.incrementLong(EnumStat.TIMES_FARMED.getName());

        MR.send(co.textChannel, op);
    }
}
