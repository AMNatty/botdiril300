package cz.tefek.botdiril.command.currency;

import java.util.Random;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;

@Command(value = "farm", category = CommandCategory.CURRENCY, aliases = {}, description = "Farm to get some gold, experience and items.")
public class CommandFarm
{
    @CmdInvoke
    public static void farm(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.farm, "You need to wait $ to farm again.");

        var random = new Random();

        var cannon = random.nextDouble() > 0.75;

        var coins = Botdiril.RDG.nextLong(100 + co.ui.getLevel() * 200, 300 + co.ui.getLevel() * 350);

        var keks = Botdiril.RDG.nextLong(500 + co.ui.getLevel() * 400, 800 + co.ui.getLevel() * 700);

        var xp = Botdiril.RDG.nextLong(200 + co.ui.getLevel() * 20, 400 + co.ui.getLevel() * 35);

        var drops = Items.leagueItems.get(random.nextInt(Items.leagueItems.size()));

        co.ui.addItem(drops);
        co.ui.addKeks(keks);
        co.ui.addCoins(coins);
        XPAdder.addXP(co, xp);

        final var cannonString = "You farmed and didn't actually miss the cannon minion so you got more loot: **%d %s**, **%d %s**, **%d xp** and **%s**.";
        final var basicString = "You farmed and got **%d %s**, **%d %s**, **%d xp** and **%s**.";

        var op = String.format(cannon ? cannonString
                : basicString, coins, EnumCurrency.COINS.getIcon(), keks, EnumCurrency.KEKS.getIcon(), xp, drops.inlineDescription());

        co.po.incrementLong(EnumStat.TIMES_FARMED.getName());

        MR.send(co.textChannel, op);
    }
}
