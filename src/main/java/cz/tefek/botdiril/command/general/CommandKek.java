package cz.tefek.botdiril.command.general;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.EnumSpecialCommandProperty;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;

@Command(category = CommandCategory.GENERAL, description = Icons.KEK, value = "kek", special = {
        EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS })
public class CommandKek
{
    @CmdInvoke
    public static void name(CallObj co)
    {
        MR.send(co.textChannel, Icons.SKINS.get(Botdiril.RANDOM.nextInt(Icons.SKINS.size())));
    }
}
