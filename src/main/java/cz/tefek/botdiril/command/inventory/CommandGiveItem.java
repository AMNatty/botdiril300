package cz.tefek.botdiril.command.inventory;

import net.dv8tion.jda.core.entities.Member;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "give", aliases = { "giveitem",
        "givecard" }, category = CommandCategory.ITEMS, description = "Give someone an item or a card.", levelLock = 5)
public class CommandGiveItem
{
    @CmdInvoke
    public static void giveOne(CallObj co, @CmdPar("user") Member recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        if (item instanceof Item)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Item) item), "You don't have that item to give it to someone.");
        }
        else if (item instanceof Card)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Card) item), "You don't have that item to give it to someone.");
        }

        give(co, recipient, item, 1);
    }

    @CmdInvoke
    public static void give(CallObj co, @CmdPar("user") Member recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't give zero items.");

        if (item instanceof Item)
        {
            co.ui.addItem((Item) item, -amount);
            new UserInventory(recipient.getUser().getIdLong()).addItem((Item) item, amount);
        }
        else if (item instanceof Card)
        {
            co.ui.addCard((Card) item, -amount);
            new UserInventory(recipient.getUser().getIdLong()).addCard((Card) item, amount);
        }

        if (recipient.getUser().getIdLong() == co.caller.getIdLong())
            MR.send(co.textChannel, String.format("You gave yourself **%d** %s(s)? ~~If there was a point in giving yourself your own stuff or something...~~", amount, item.inlineDescription()));
        else
            MR.send(co.textChannel, String.format("You gave **%s** **%d** %s(s).", recipient.getEffectiveName(), amount, item.inlineDescription()));
    }
}