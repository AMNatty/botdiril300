package cz.tefek.botdiril.command.general;

import java.util.Arrays;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.MiniTime;

@Command(value = "curses", aliases = {
        "blessings" }, category = CommandCategory.GENERAL, description = "Check user's timers.")
public class CommandCurses
{
    @CmdInvoke
    public static void check(CallObj co, @CmdPar("user") User user)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
        eb.setTitle("Curse/blessing timers");
        eb.setDescription(user.getAsMention() + "'s active blessings/curses.");

        try (var po = new PropertyObject(user.getIdLong()))
        {

            Arrays.stream(EnumCurse.values()).forEach(t ->
            {
                var remaining = po.getLongOrDefault(t.getName(), 0) - System.currentTimeMillis();

                if (remaining < 0)
                {
                    return;
                }

                eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
            });

            Arrays.stream(EnumBlessing.values()).forEach(t ->
            {
                var remaining = po.getLongOrDefault(t.getName(), 0) - System.currentTimeMillis();

                if (remaining < 0)
                {
                    return;
                }

                eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
            });
        }

        eb.setColor(0x008080);
        eb.setThumbnail(user.getEffectiveAvatarUrl());

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void checkSelf(CallObj co)
    {
        check(co, co.caller);
    }
}
