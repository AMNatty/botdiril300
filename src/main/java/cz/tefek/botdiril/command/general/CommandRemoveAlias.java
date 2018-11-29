package cz.tefek.botdiril.command.general;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "removealias", aliases = {}, category = CommandCategory.GENERAL, description = "Remove an alias you previously set.")
public class CommandRemoveAlias
{
    @CmdInvoke
    public static void unbind(CallObj co)
    {
        MR.send(co.textChannel, "You actually want this? This command is coming soon.");
    }

    @CmdInvoke
    public static void unbind(CallObj co, @CmdPar("alias number") int number)
    {
        MR.send(co.textChannel, "You actually want this? This command is coming soon.");
    }
}
