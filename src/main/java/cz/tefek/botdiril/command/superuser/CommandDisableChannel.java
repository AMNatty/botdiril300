package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.serverdata.ChannelPreferences;
import cz.tefek.botdiril.serverdata.ServerPreferences;

@Command(value = "toggledisable", aliases = {
        "disablechannel" }, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER, description = "Disable commands for non-elevated users in a text channel.")
public class CommandDisableChannel
{
    @CmdInvoke
    public static void toggleDisable(CallObj co)
    {
        toggleDisable(co, co.textChannel);
    }

    @CmdInvoke
    public static void toggleDisable(CallObj co, @CmdPar("channel") TextChannel channel)
    {
        var channelId = co.textChannel.getIdLong();
        var on = !ChannelPreferences.checkBit(channelId, ChannelPreferences.BIT_DISABLED);
        var lc = co.guild.getTextChannelById(ServerPreferences.getConfigByGuild(co.guild.getIdLong()).getLoggingChannel());

        if (on)
        {
            ChannelPreferences.setBit(channelId, ChannelPreferences.BIT_DISABLED);
            MR.send(co.textChannel, "Channel " + channel.getAsMention() + " is now disabled for non-elevated users.");
        }
        else
        {
            ChannelPreferences.clearBit(channelId, ChannelPreferences.BIT_DISABLED);
            MR.send(co.textChannel, "Channel " + channel.getAsMention() + " is now enabled for non-elevated users.");
        }

        if (lc != null)
        {
            var eb = new EmbedBuilder();
            eb.setTitle("Botdiril SuperUser");
            eb.setColor(0x008080);

            if (on)
            {
                eb.setDescription("Channel interaction has been disabled for non-elevated users.");
            }
            else
            {
                eb.setDescription("Channel interaction has been enabled for non-elevated users.");
            }

            eb.addField("User", co.caller.getAsMention(), false);
            eb.addField("Channel", co.textChannel.getAsMention(), false);
            eb.setFooter("Message ID: " + co.message.getIdLong(), null);
            eb.setTimestamp(Instant.now());

            MR.send(lc, eb.build());
        }
    }
}
