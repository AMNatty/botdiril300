package cz.tefek.botdiril.command.administrative;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.exceptions.HierarchyException;

import java.time.Instant;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.serverdata.ServerPreferences;
import cz.tefek.botdiril.userdata.timers.Timers;

@Command(value = "rename", aliases = {
        "changenickname" }, category = CommandCategory.ADMINISTRATIVE, description = "Change your nickname. Thirty day cooldown.")
public class CommandRename
{
    @CmdInvoke
    public static void rename(CallObj co, @CmdPar("new name") String nick)
    {
        CommandAssert.stringNotTooLong(nick, 32, "Your nickname **cannot** be longer than **32 characters**.");

        final var oldNick = co.callerMember.getNickname();

        try
        {
            CommandAssert.assertTimer(co.ui, Timers.nicknamechange, "You need to wait $ to rename yourself again.");

            co.guild.getController().setNickname(co.callerMember, nick).queue(c ->
            {
                MR.send(co.textChannel, "Your nickname has been changed. You'll be able to rename yourself again in **30 days**.");

                var lc = co.guild.getTextChannelById(ServerPreferences.getConfigByGuild(co.guild.getIdLong()).getLoggingChannel());

                if (lc != null)
                {
                    var eb = new EmbedBuilder();
                    eb.setTitle("Botdiril");
                    eb.setColor(0x008080);
                    eb.setDescription(co.callerMember.getAsMention() + " changed his nickname.");
                    if (oldNick != null)
                        eb.addField("Old Nickname", oldNick, false);
                    eb.addField("New Nickname", co.callerMember.getAsMention(), false);
                    eb.addField("Cooldown", "30 days", false);
                    eb.setFooter("Message ID: " + co.message.getIdLong(), null);
                    eb.setTimestamp(Instant.now());

                    MR.send(lc, eb.build());
                }
            });
        }
        catch (HierarchyException e)
        {
            MR.send(co.textChannel, "I can't change your nickname when your role is above me.");
            co.ui.resetTimer(Timers.nicknamechange);
        }
    }
}
