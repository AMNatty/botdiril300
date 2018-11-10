package cz.tefek.botdiril.command.currency;

import java.util.function.Function;

import java.math.BigDecimal;
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

@Command(value = "payoutmegakeks", aliases = { "payoutmega",
        "bigpayout" }, category = CommandCategory.CURRENCY, description = "Pay out your " + Icons.MEGAKEK + " for some " + Icons.KEK, levelLock = 15)
public class CommandPayoutMegaKeks
{
    private static final Function<Double, Long> conversion = (pow) -> Math.round(Math.pow((Math.pow(((pow + 125) / 1000), 3) * 1000), 3.45));

    @CmdInvoke
    public static void payout(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.payout, "You need to wait **$** before paying out again.");

        var has = co.ui.getMegaKeks();

        CommandAssert.assertNotEquals(has, BigInteger.ZERO, "You can't pay out zero " + Icons.MEGAKEK + ".");

        var dma = new BigDecimal(has);

        var numStr = BigNumbers.stringifyBoth(has);

        var pow = dma.precision() - dma.scale() - 1;

        var gets = conversion.apply((double) pow);

        co.ui.setMegaKeks(BigInteger.ZERO);
        co.ui.addKeks(gets);

        var xp = 10 + pow * pow / 10;

        XPAdder.addXP(co, xp);

        MR.send(co.textChannel, String.format("Paid out **%s** %s for **%d** %s. **[+%d XP]**", numStr, Icons.MEGAKEK, gets, Icons.KEK, xp));
    }
}
