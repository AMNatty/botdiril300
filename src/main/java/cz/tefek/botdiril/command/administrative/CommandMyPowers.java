package cz.tefek.botdiril.command.administrative;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.time.Instant;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "powers", aliases = {
        "permissions" }, category = CommandCategory.ADMINISTRATIVE, description = "Displays the powers a user wields.")
public class CommandMyPowers
{
    @CmdInvoke
    public static void print(CallObj co)
    {
        print(co, co.callerMember);
    }

    @CmdInvoke
    public static void print(CallObj co, @CmdPar("user") Member user)
    {
        if (user.getUser().isBot())
        {
            MR.send(co.textChannel, "This command can't be used on bots.");
            return;
        }

        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setTitle("Power Listing");
        eb.setDescription(user.getAsMention() + "'s powers:");
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());
        eb.setFooter("User ID: " + user.getUser().getIdLong(), null);
        eb.setTimestamp(Instant.now());

        EnumPowerLevel.getCumulativePowers(user, co.textChannel).forEach(c ->
        {
            eb.addField(c.toString(), c.getDescription(), false);
        });

        MR.send(co.textChannel, eb.build());
    }
}
