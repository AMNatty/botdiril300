package cz.tefek.botdiril.command.inventory;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ShopEntries;

@Command(value = "disenchant", aliases = {
        "dust" }, category = CommandCategory.ITEMS, description = "Disenchant items or cards into " + Icons.DUST + ".", levelLock = 6)
public class CommandDisenchant
{
    @CmdInvoke
    public static void dust(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        dust(co, item, 1);
    }

    @CmdInvoke
    public static void dust(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't disenchant zero items / cards.");

        if (!ShopEntries.canBeDisenchanted(item))
            throw new CommandException("That item cannot be sold.");

        if (item instanceof Item)
        {
            CommandAssert.numberNotAboveL(amount, co.ui.howManyOf((Item) item), "You don't have that many items of that type.");
            co.ui.addItem((Item) item, -amount);
        }
        else if (item instanceof Card)
        {
            CommandAssert.numberNotAboveL(amount, co.ui.howManyOf((Card) item), "You don't have that many items of that type.");
            co.ui.addCard((Card) item, -amount);
        }

        var value = amount * ShopEntries.getDustForDisenchanting(item);
        co.ui.addDust(value);

        MR.send(co.textChannel, String.format("You disenchanted **%d** %s for **%d** %s.", amount, item.inlineDescription(), value, Icons.DUST));
    }
}
