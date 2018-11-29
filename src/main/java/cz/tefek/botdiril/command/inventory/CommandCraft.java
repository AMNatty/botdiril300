package cz.tefek.botdiril.command.inventory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;
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
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;

@Command(value = "craft", aliases = {}, category = CommandCategory.ITEMS, description = "Craft stuff.", levelLock = 2)
public class CommandCraft
{
    @CmdInvoke
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        craft(co, item, 1);
    }

    @CmdInvoke
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount") long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't craft zero items / cards.");

        CommandAssert.numberNotAboveL(amount, Integer.MAX_VALUE, "That's way too many.");

        var recipe = CraftingEntries.search(item);

        CommandAssert.assertTrue(recipe != null, "That item cannot be crafted.");

        BotMain.sql.lock();

        try
        {
            var components = recipe.getComponents();

            var missing = new ArrayList<ItemPair>();

            for (var itemPair : components)
            {
                var it = itemPair.getItem();
                var itAmt = itemPair.getAmount() * amount;
                var has = co.ui.howManyOf(it);

                if (has < itAmt)
                {
                    missing.add(new ItemPair(it, itAmt - has));
                }
            }

            if (!missing.isEmpty())
            {
                var missingStr = missing.stream().map(ip -> String.format("**%d %s**", ip.getAmount(), ip.getItem().inlineDescription())).collect(Collectors.joining(", "));
                throw new CommandException(String.format("You are missing %s for this crafting.", missingStr));
            }

            if (Curser.isBlessed(co, EnumBlessing.MINE_SURGE))
            {
                for (int i = 0; i < amount; i++)
                {
                    if (Botdiril.RDG.nextUniform(0, 1) < 0.2)
                    {
                        components.forEach(c -> co.ui.addItem(c.getItem(), -c.getAmount()));
                    }
                }
            }
            else
            {
                components.forEach(c -> co.ui.addItem(c.getItem(), -c.getAmount() * amount));
            }

            var ingr = components.stream().map(ip -> String.format("**%d** **%s**", ip.getAmount() * amount, ip.getItem().inlineDescription())).collect(Collectors.joining(", "));

            if (Curser.isBlessed(co, EnumBlessing.MINE_SURGE) && Botdiril.RDG.nextUniform(0, 1) < 0.2)
            {
                MR.send(co.textChannel, String.format("*You failed horribly while crafting and lost %s.*", ingr));
                return;
            }

            if (item instanceof Item)
            {
                co.ui.addItem((Item) item, amount * recipe.getAmount());
            }
            else if (item instanceof Card)
            {
                co.ui.addCard((Card) item, amount * recipe.getAmount());
            }

            co.po.addLong(EnumStat.ITEMS_CRAFTED.getName(), amount * recipe.getAmount());

            MR.send(co.textChannel, String.format("You crafted **%d** **%s** from %s.", amount * recipe.getAmount(), item.inlineDescription(), ingr));
        }
        finally
        {
            BotMain.sql.unlock();
        }
    }
}
