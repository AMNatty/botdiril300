package cz.tefek.botdiril.command.currency;

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
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;

@Command(value = "sell", aliases = {}, category = CommandCategory.CURRENCY, description = "Sell items for " + Icons.COIN + ".", levelLock = 2)
public class CommandSell
{
    @CmdInvoke
    public static void sell(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        sell(co, item, 1);
    }

    @CmdInvoke
    public static void sell(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't sell zero items / cards.");

        if (!ShopEntries.canBeSold(item))
            throw new CommandException("That item / card cannot be sold.");

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

        var value = amount * ShopEntries.getSellValue(item);

        if (Curser.isCursed(co, EnumCurse.HALVED_SELL_VALUE))
        {
            value /= 2;
        }

        if (Curser.isBlessed(co, EnumBlessing.BETTER_SELL_PRICES))
        {
            value = value * 17 / 10;
        }

        co.ui.addCoins(value);

        MR.send(co.textChannel, String.format("You sold **%d** %s for **%d** %s.", amount, item.inlineDescription(), value, Icons.COIN));
    }
}
