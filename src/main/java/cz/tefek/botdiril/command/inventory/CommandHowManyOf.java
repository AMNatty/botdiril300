package cz.tefek.botdiril.command.inventory;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "howmanyof", aliases = { "countcards", "countitems", "cc",
        "cc" }, category = CommandCategory.ITEMS, description = "Tells the count of an item or card.")
public class CommandHowManyOf
{
    @CmdInvoke
    public static void count(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable thing)
    {
        long count;

        if (thing instanceof Card)
        {
            count = co.ui.howManyOf((Card) thing);
        }
        else if (thing instanceof Item)
        {
            count = co.ui.howManyOf((Item) thing);
        }
        else
        {
            MR.send(co.textChannel, "**An error has happened while processing this command:** Invalid instance of IIdentifiable\n**Please contact the developer.**");
            return;
        }

        if (count <= 0)
            MR.send(co.textChannel, String.format("You have **no %s**.", thing.inlineDescription()));
        else
            MR.send(co.textChannel, String.format("You have **%d %s**.", count, thing.inlineDescription()));
    }
}
