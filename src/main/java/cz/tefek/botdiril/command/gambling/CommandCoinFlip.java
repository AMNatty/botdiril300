package cz.tefek.botdiril.command.gambling;

import java.util.Random;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(value = "coinflip", category = CommandCategory.GAMBLING, aliases = {}, description = "Coin flip. You can specify a number to gamble keks.")
public class CommandCoinFlip
{
    @CmdInvoke
    public static void roll(CallObj co)
    {
        MR.send(co.textChannel, new Random().nextBoolean() ? "**Heads.**" : "**Tails.**");
    }

    @CmdInvoke
    public static void roll(CallObj co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks, @CmdPar("bet on side") EnumCoinSides side)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");

        if (co.ui.useTimer(Timers.gambleXP) == -1)
        {
            var lvl = co.ui.getLevel();
            XPAdder.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * Botdiril.RDG.nextUniform(0.00001, 0.0001)));
        }

        var rolled = new Random().nextBoolean() ? EnumCoinSides.HEADS : EnumCoinSides.TAILS;

        if (rolled == side)
        {
            co.ui.addKeks(keks);
            MR.send(co.textChannel, String.format("**%s!** Here are your **%d %s**.", rolled == EnumCoinSides.HEADS
                    ? ":large_orange_diamond: Heads"
                    : ":large_blue_diamond: Tails", keks, Icons.KEK));
        }
        else
        {
            co.ui.addKeks(-keks);
            MR.send(co.textChannel, String.format("**%s!** You **lost** your **%d %s**.", rolled == EnumCoinSides.HEADS
                    ? ":large_orange_diamond: Heads"
                    : ":large_blue_diamond: Tails", keks, Icons.KEK));
        }
    }
}
