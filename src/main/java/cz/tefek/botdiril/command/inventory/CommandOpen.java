package cz.tefek.botdiril.command.inventory;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "open", aliases = {}, category = CommandCategory.ITEMS, description = "Open a card pack/crate/something else.")
public class CommandOpen
{
    @CmdInvoke
    public static void open(CallObj co, @CmdPar("what to open") Item item)
    {
        open(co, item, 1);
    }

    @CmdInvoke
    public static void open(CallObj co, @CmdPar("what to open") Item item, @CmdPar("how many to open") long amount)
    {
        CommandAssert.assertTrue(item instanceof IOpenable, "This item cannot be opened.");

        var openable = (IOpenable) item;

        CommandAssert.numberInBoundsInclusiveL(amount, 1, 32, "You can open 32 crates at most and one at least (obviously).");

        new Thread(() ->
        {
            try
            {
                BotMain.sql.lock();

                CommandAssert.numberNotBelowL(co.ui.howManyOf(item), amount, "You don't have " + amount + " " + item.inlineDescription() + "s...");

                if (openable.requiresKey())
                    CommandAssert.numberNotBelowL(co.ui.getKeys(), amount, "You don't have enough keys for this.");

                openable.open(co, amount);
                co.ui.addItem(item, -amount);

                if (openable.requiresKey())
                    co.ui.addKeys(-amount);
            }
            catch (CommandException e)
            {
                MR.send(co.textChannel, e.getMessage());
            }
            finally
            {
                BotMain.sql.unlock();
            }
        }).start();
    }
}
