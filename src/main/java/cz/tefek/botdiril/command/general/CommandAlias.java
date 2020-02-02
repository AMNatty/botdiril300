package cz.tefek.botdiril.command.general;

import java.util.HashMap;
import java.util.Map;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

@Command(value = "alias", aliases = {}, category = CommandCategory.GENERAL, description = "Set an alias for some text.")
public class CommandAlias
{
    public static Map<String, String> allAliases(PropertyObject po)
    {
        var map = new HashMap<String, String>();
        var bound = po.getByteOrDefault("alias_used", (byte) 0b00000000);

        for (int i = 0; i < Byte.SIZE; i++)
        {
            if ((1 << i & bound) > 0)
            {
                try
                {
                    map.put(po.getString("alias_in" + i), po.getString("alias_out" + i));
                }
                catch (Exception e)
                {
                    BotMain.logger.error("Non-existent alias accessed.", e);
                }
            }
        }

        return map;
    }

    @CmdInvoke
    public static void bind(CallObj co, @CmdPar("alias name") String source, @CmdPar("alias replacement") String replacement)
    {
        source = source.trim();

        if (source.isEmpty())
        {
            throw new CommandException("Alias cannot be empty!");
        }

        CommandAssert.stringNotTooLong(source, 48, "Alias source shouldn't be longer than 48 characters!");

        if (source.length() < 5)
        {
            throw new CommandException("Alias source shouldn't be shorter than 5 characters!");
        }

        CommandAssert.stringNotTooLong(replacement, 64, "Alias replacement shouldn't be longer than 64 characters!");

        var slot = findEmptySlot(co);

        if (slot == -1)
        {
            MR.send(co.textChannel, "You have no empty alias slots. Try freeing some with `removealias`.");
        }
        else
        {
            setSlot(co.po, slot, source, replacement);
            MR.send(co.textChannel, "Alias bound to slot " + slot + ".");
        }
    }

    static int findEmptySlot(CallObj co)
    {
        var bound = co.po.getByteOrDefault("alias_used", (byte) 0b00000000);

        for (int i = 0; i < Byte.SIZE; i++)
        {
            if ((1 << i & bound) == 0)
            {
                return i;
            }
        }

        return -1;
    }

    static void setSlot(PropertyObject po, int slot, String in, String out)
    {
        var bound = po.getByteOrDefault("alias_used", (byte) 0b00000000);
        po.setByte("alias_used", (byte) (bound | 1 << slot));
        po.setString("alias_in" + slot, in);
        po.setString("alias_out" + slot, out);
    }
}
