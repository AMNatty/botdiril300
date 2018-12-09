package cz.tefek.botdiril.command.general;

import java.util.Arrays;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.stat.EnumStat;

@Command(value = "stats", description = "Show your stats.", category = CommandCategory.GENERAL)
public class CommandStats
{
    @CmdInvoke
    public static void show(CallObj co, @CmdPar("user") User user)
    {
        var eb = new EmbedBuilder();

        eb.setTitle("Stats");
        eb.setColor(0x008080);
        eb.setDescription(user.getAsMention() + "'s stats.");

        Arrays.stream(EnumStat.values()).forEach(es ->
        {
            eb.addField(es.getLocalizedName(), "" + co.po.getLongOrDefault(es.name(), 0L), false);
        });

        MR.send(co.textChannel, eb.build());
    }
}
