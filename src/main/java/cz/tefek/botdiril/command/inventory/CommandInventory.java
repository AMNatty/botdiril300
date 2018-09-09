package cz.tefek.botdiril.command.inventory;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import net.dv8tion.jda.core.entities.Member;

@Command(value = "inventory", aliases = { "inv",
                                          "i" }, category = CommandCategory.ITEMS, description = "Shows your/someone's inventory.")
public class CommandInventory
{
    @CmdInvoke
    public static void show(CallObj co)
    {
        show(co, co.callerMember);
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("target") Member user)
    {
        co.ui.getUserDataObj();
    }
}
