package cz.tefek.botdiril.command.inventory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.pools.CardPools;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(value = "draw", category = CommandCategory.ITEMS, aliases = {}, levelLock = 5, description = "Draws some cards.")
public class CommandDraw
{
    @CmdInvoke
    public static void draw(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.draw, "You need to wait $ before drawing cards again.");

        var lc = new ArrayList<Card>();

        var cp = (Card) CardPools.rareOrBetter.draw().draw();

        lc.add(cp);

        for (int i = 0; i < XPRewards.getLevel(co.ui.getLevel()).getDrawPotency() - 1; i++)
        {
            lc.add((Card) CardPools.commonToLimited.draw().draw());
        }

        var msg = String.format("You drew %s.", lc.stream()
                .map(IIdentifiable::inlineDescription)
                .collect(Collectors.joining(", ")));

        lc.stream().forEach(co.ui::addCard);

        MR.send(co.textChannel, msg);
    }
}
