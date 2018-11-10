package cz.tefek.botdiril.command.currency;

import java.util.Random;

import java.math.BigInteger;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.BigNumbers;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(value = "daily", category = CommandCategory.CURRENCY, aliases = {}, description = "Get yourself some free daily stuff.")
public class CommandDaily
{
    @CmdInvoke
    public static void daily(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.daily, "You need to wait **$** to use **daily** again.");

        var lvl = co.ui.getLevel();

        var lvldata = XPRewards.getLevel(lvl);

        var xp = Math.round(new Random().nextDouble() * (lvldata.getDailyMax() - lvldata.getDailyMin()) + lvldata.getDailyMin());
        XPAdder.addXP(co, xp);

        var coins = 5000 + lvl * 200;
        co.ui.addCoins(coins);

        var keks = 2000 + lvl * 100;
        co.ui.addKeks(keks);

        var megakeks = BigInteger.TWO.pow(10 + (int) Math.round(Math.pow(lvl, 0.6)));
        co.ui.addMegaKeks(megakeks);

        var keys = lvl > 100 ? 5 : 3;
        co.ui.addKeys(keys);

        var str = String.format("**Here are your daily items:**\n%d xp\n%d %s\n%d %s\n%s %s\n%d %s", xp, coins, Icons.COIN, keks, Icons.KEK, BigNumbers.stringifyBoth(megakeks), Icons.MEGAKEK, keys, Icons.KEY);

        MR.send(co.textChannel, str);
    }
}
