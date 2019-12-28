package cz.tefek.botdiril.command.debug;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;

@Command(aliases = {
        "latency" }, category = CommandCategory.GENERAL, description = "Tells the Discord latency.", value = "ping")
public class CommandPing
{
    @CmdInvoke
    public static void ping(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Botdiril Debug Commands", null, co.bot.getEffectiveAvatarUrl());
        eb.setTitle("Ping.");
        eb.setColor(0x008080);
        eb.setDescription(co.jda.getGatewayPing() + " ms");

        MR.send(co.textChannel, eb.build());
    }
}
