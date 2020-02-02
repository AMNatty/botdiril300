package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.properties.NValueNotPresentException;

@Command(value = "aliases", aliases = {
        "listaliases" }, category = CommandCategory.GENERAL, description = "List your aliases.")
public class CommandListAliases
{
    @CmdInvoke
    public static void list(CallObj co)
    {
        var bound = co.po.getByteOrDefault("alias_used", (byte) 0b00000000);

        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());

        if (bound == 0)
        {
            eb.setTitle(String.format("You have no aliases", Integer.bitCount(bound & 0x000000FF)));
            eb.setDescription(String.format("Tip: Set up an alias using `%salias <what gets replaced> <what to replace it with>`.", co.sc.getPrefix()));
        }
        else
        {
            eb.setTitle(String.format("You have %d aliases", Integer.bitCount(bound & 0x000000FF)));
            eb.setDescription("Currently set aliases:");
            for (int i = 0; i < Byte.SIZE; i++)
            {
                if ((1 << i & bound) > 0)
                {
                    try
                    {
                        eb.addField(String.format("Alias %d", i), String.format("`%s` → `%s`", co.po.getString("alias_in" + i), co.po.getString("alias_out" + i)), false);
                    }
                    catch (NValueNotPresentException e)
                    {
                        BotMain.logger.error("Non-existent alias accessed.", e);
                    }
                }
            }

            eb.setFooter(String.format("Tip: Type `%sremovealias <alias number>` to delete an alias.", co.sc.getPrefix()));
        }

        MR.send(co.textChannel, eb.build());

    }
}
