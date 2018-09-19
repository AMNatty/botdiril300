package cz.tefek.botdiril.command.gambling;

import java.util.Random;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;

@Command(value = "diceroll", category = CommandCategory.GAMBLING, aliases = {}, description = "Rolls a six-sided die. You can specify a number to gamble keks.")
public class CommandDiceRoll
{
    @CmdInvoke
    public static void roll(CallObj co)
    {
        MR.send(co.textChannel, ":game_die: You rolled a **" + (new Random().nextInt(6) + 1 + "**!"));
    }

    @CmdInvoke
    public static void roll(CallObj co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks, @CmdPar("bet on side") int number)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");
        CommandAssert.numberInBoundsInclusiveL(number, 1, 6, "You can't bet on a side that does not exit... Use a number in the range 1..6");

        var rolled = new Random().nextInt(6) + 1;

        if (rolled == number)
        {
            var reward = keks * 5;
            co.ui.addKeks(reward);
            MR.send(co.textChannel, String.format(":game_die: You rolled a **%d**! Here are your %d %s.", rolled, reward, Icons.KEK));
        } else
        {
            co.ui.addKeks(-keks);
            MR.send(co.textChannel, String.format(":game_die: You rolled a **%d**! You lost your %d %s.", rolled, keks, Icons.KEK));
        }
    }
}
