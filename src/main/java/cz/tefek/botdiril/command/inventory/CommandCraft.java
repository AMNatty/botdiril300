package cz.tefek.botdiril.command.inventory;

import java.util.stream.Collectors;

import cz.tefek.botdiril.BotMain;
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
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "craft", aliases = {}, category = CommandCategory.ITEMS, description = "Craft stuff.", levelLock = 2)
public class CommandCraft
{
    @CmdInvoke
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        craft(co, item, 1);
    }

    @CmdInvoke
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't craft zero items / cards.");

        var recipe = CraftingEntries.search(item);

        CommandAssert.assertTrue(recipe != null, "That item cannot be crafted.");

        BotMain.sql.lock();

        try
        {
            var components = recipe.getComponents();

            for (var itemPair : components)
            {
                var it = itemPair.getItem();
                var itAmt = itemPair.getAmount();
                var has = co.ui.howManyOf(it);

                if (has < itAmt * amount)
                {
                    throw new CommandException(String.format("You need **%d** more **%s** for this crafting.", itAmt - has, it.inlineDescription()));
                }
            }

            components.forEach(c -> co.ui.addItem(c.getItem(), -c.getAmount() * amount));

            if (item instanceof Item)
            {
                co.ui.addItem((Item) item, amount);
            }
            else if (item instanceof Card)
            {
                co.ui.addCard((Card) item, amount);
            }

            var ingr = components.stream().map(ip -> String.format("**%d** **%s**", ip.getAmount() * amount, ip.getItem().inlineDescription())).collect(Collectors.joining(", "));

            MR.send(co.textChannel, String.format("You crafted **%d** **%s** from %s.", amount, item.inlineDescription(), ingr));
        }
        finally
        {
            BotMain.sql.unlock();
        }
    }
}
