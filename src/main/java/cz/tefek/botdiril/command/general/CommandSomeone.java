package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.core.EmbedBuilder;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "someone", category = CommandCategory.GENERAL, aliases = {
        "randomuser" }, description = "Gets a random user present in this channel.")
public class CommandSomeone
{
    @CmdInvoke
    public static void choose(CallObj co)
    {
        var memberList = co.textChannel.getMembers();
        var member = memberList.get(Botdiril.RANDOM.nextInt(memberList.size()));

        var eb = new EmbedBuilder();
        eb.setTitle(co.callerMember.getEffectiveName() + ", here is the user you rolled:");
        eb.setDescription(member.getAsMention());
        eb.setColor(0x008080);
        eb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        MR.send(co.textChannel, eb.build());
    }
}
