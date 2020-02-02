package cz.tefek.botdiril.command.gambling;

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

@Command(value = "diceroll", category = CommandCategory.GAMBLING, aliases = {}, description = "Rolls a six-sided die. You can specify a number to gamble keks.")
public class CommandDiceRoll
{
    @CmdInvoke
    public static void roll(CallObj co)
    {
        MR.send(co.textChannel, String.format(":game_die: You rolled a **%d**!", Botdiril.RDG.nextInt(1, 6)));
    }

    @CmdInvoke
    public static void roll(CallObj co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks, @CmdPar("bet on side") int number)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");
        CommandAssert.numberInBoundsInclusiveL(number, 1, 6, "You can't bet on a side that does not exit... Use a number in the range 1..6");

        if (co.ui.useTimer(Timers.gambleXP) == -1)
        {
            var lvl = co.ui.getLevel();
            XPAdder.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * Botdiril.RDG.nextUniform(0.00001, 0.0001)));
        }

        var rolled = Botdiril.RDG.nextInt(1, 6);

        if (rolled == number)
        {
            var reward = keks * 5;
            co.ui.addKeks(reward);
            MR.send(co.textChannel, String.format(":game_die: You rolled a **%d**! Here are your **%d %s**.", rolled, reward, Icons.KEK));
        }
        else
        {
            co.ui.addKeks(-keks);
            MR.send(co.textChannel, String.format(":game_die: You rolled a **%d**! You **lost** your **%d %s.**", rolled, keks, Icons.KEK));
        }
    }
}
