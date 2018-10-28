package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.core.EmbedBuilder;

import java.text.MessageFormat;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;

@Command(value = "xpleaderboards", aliases = { "toplevel", "toplevels",
        "xprankings" }, category = CommandCategory.CURRENCY, description = "Shows the top 10 highest level users.")
public class CommandTopLevel
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Global level leaderboards");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.jda.getSelfUser().getEffectiveAvatarUrl());

        BotMain.sql.exec("SELECT us_userid, us_level, us_xp FROM " + UserInventory.TABLE_USER + " ORDER BY us_level DESC, us_xp DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var us = co.jda.getUserById(rs.getLong("us_userid"));
                var userName = us == null ? "[Unknown user]" : us.getAsMention();
                var usn = String.format("**%d.** %s", i, userName);
                var lvlInfo = String.format("**Level %d**, %d XP", rs.getLong("us_level"), rs.getLong("us_xp"));
                var row = String.format("%s with %s", usn, lvlInfo);

                eb.addField("", row, false);

                i++;
            }

            return 0;
        });

        MR.send(co.textChannel, eb.build());
    }
}
