package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.core.entities.Member;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "additem", category = CommandCategory.SUPERUSER, aliases = {}, description = "Adds items to the target's inventory (for science of course).", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandAddItem
{
    @CmdInvoke
    public static void addItem(CallObj co, @CmdPar("item") Item item)
    {
        co.ui.addItem(item, 1);

        MR.send(co.textChannel, String.format("Added %d %s!", 1, item.inlineDescription()));
    }

    @CmdInvoke
    public static void addItem(CallObj co, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        co.ui.addItem(item, howmany);

        MR.send(co.textChannel, String.format("Added %d %s!", howmany, item.inlineDescription()));
    }

    @CmdInvoke
    public static void addItem(CallObj co, @CmdPar("user") Member user, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        new UserInventory(user.getUser().getIdLong()).addItem(item, howmany);

        MR.send(co.textChannel, String.format("Added %d %s to %s's inventory!", howmany, item.inlineDescription(), user.getAsMention()));
    }
}
