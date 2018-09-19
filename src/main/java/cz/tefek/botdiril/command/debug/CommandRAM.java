package cz.tefek.botdiril.command.debug;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "heap",
        "ram" }, category = CommandCategory.SUPERUSER, description = "Shows some memory information.", value = "memory")
public class CommandRAM
{
    @CmdInvoke
    public static void print(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Botdiril Debug Commands", null, co.bot.getEffectiveAvatarUrl());
        eb.setTitle("Memory information.");
        eb.setColor(0x008080);

        var rt = Runtime.getRuntime();

        eb.addField("Maximum memory", rt.maxMemory() / 1000000 + " MB", false);
        eb.addField("Used memory", rt.totalMemory() / 1000000 + " MB", false);
        eb.addField("Free memory", rt.freeMemory() / 1000000 + " MB", false);

        MR.send(co.textChannel, eb.build());
    }
}
