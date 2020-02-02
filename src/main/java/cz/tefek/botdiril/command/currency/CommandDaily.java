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
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilFmt;

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

        if (Curser.isCursed(co, EnumCurse.CANT_TAKE_DAILY))
        {
            MR.send(co.textChannel, "***You prepare to check out your daily loot but some magical spell is preventing you from doing so.***");
            co.ui.resetTimer(Timers.daily);
            return;
        }

        var str = String.format("**Here are your daily items:**\n%s xp\n%s %s\n%s %s\n%s %s\n%s %s", BotdirilFmt.format(xp), BotdirilFmt.format(coins), Icons.COIN, BotdirilFmt.format(keks), Icons.KEK, BigNumbers.stringifyBoth(megakeks), Icons.MEGAKEK, BotdirilFmt.format(keys), Icons.KEY);

        co.po.incrementLong(EnumStat.TIMES_DAILY.getName());

        MR.send(co.textChannel, str);
    }
}
