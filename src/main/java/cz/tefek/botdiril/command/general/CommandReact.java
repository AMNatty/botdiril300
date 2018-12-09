package cz.tefek.botdiril.command.general;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;

@Command(value = "react", description = "Makes the bot react with an emote.", category = CommandCategory.GENERAL)
public class CommandReact
{
    @CmdInvoke
    public static void react(CallObj co, @CmdPar("emote") String emote)
    {

    }
}
