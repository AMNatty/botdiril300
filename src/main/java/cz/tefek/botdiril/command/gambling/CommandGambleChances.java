package cz.tefek.botdiril.command.gambling;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.random.GambleEngine;

@Command(value = "odds", aliases = {}, category = CommandCategory.GAMBLING, description = "Your gambling odds.")
public class CommandGambleChances
{
    @CmdInvoke
    public static void print(CallObj co)
    {
        GambleEngine.printChances(co);
    }
}
