package cz.tefek.botdiril.command.general;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "removealias", aliases = {}, category = CommandCategory.GENERAL, description = "Remove an alias you previously set.")
public class CommandRemoveAlias
{
    @CmdInvoke
    public static void unbind(CallObj co, @CmdPar("alias number") int number)
    {
        CommandAssert.numberInBoundsInclusiveL(number, 0, Byte.SIZE - 1, "Alias number must be non-negative and less than " + Byte.SIZE + "!");

        var bound = co.po.getByteOrDefault("alias_used", (byte) 0b00000000);

        var bit = (byte) (1 << number);

        if ((bound & bit) != 0)
        {
            co.po.setByte("alias_used", (byte) (bound & ~bit));
            MR.send(co.textChannel, String.format("Alias with the number %d was removed.", number));
        }
        else
        {
            MR.send(co.textChannel, String.format("Alias with the number %d does not exist and could not be removed.", number));
        }
    }
}
