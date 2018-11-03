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

@Command(value = "exchange", aliases = {
        "buyfortokens" }, category = CommandCategory.CURRENCY, description = "Exchange " + Icons.TOKEN + " for items or cards.", levelLock = 8)
public class CommandExchange
{
    @CmdInvoke
    public static void buy(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
            throw new CommandException("That item / card cannot be bought, sorry.");

        if (ShopEntries.getTokenPrice(item) > co.ui.getKekTokens())
            throw new CommandException("You can't afford that item / card, sorry.");

        buy(co, item, 1);
    }

    @CmdInvoke
    public static void buy(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD_BUY_TOKENS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't buy zero items.");

        var price = amount * ShopEntries.getTokenPrice(item);

        if (item instanceof Item)
        {
            co.ui.addItem((Item) item, amount);
        }
        else if (item instanceof Card)
        {
            co.ui.addCard((Card) item, amount);
        }

        co.ui.addKekTokens(-price);

        MR.send(co.textChannel, String.format("You bought **%d** %s for **%d** %s.", amount, item.getIcon(), price, Icons.TOKEN));
    }
}
