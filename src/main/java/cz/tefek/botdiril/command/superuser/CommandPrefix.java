package cz.tefek.botdiril.command.superuser;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.PermissionLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "prefix", aliases = {}, category = CommandCategory.SUPERUSER, description = "Sets the prefix for this server.", powerLevel = PermissionLevel.SUPERUSER_OVERRIDE)
public class CommandPrefix
{
    @CmdInvoke
    public static void setPrefix(CallObj co, @CmdPar("prefix") String prefix)
    {
        CommandAssert.stringNotTooLong(prefix, 8, "The prefix is too long.");

        co.sc.setPrefix(prefix);

        co.guild.getController().setNickname(co.guild.getMember(co.bot), "[" + prefix + "] " + Botdiril.BRANDING).queue();

        MR.send(co.textChannel, "Prefix set to: " + prefix);
    }
}