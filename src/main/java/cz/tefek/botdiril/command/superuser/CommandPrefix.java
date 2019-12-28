package cz.tefek.botdiril.command.superuser;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "prefix", aliases = {}, category = CommandCategory.SUPERUSER, description = "Sets the prefix for this server.", powerLevel = EnumPowerLevel.SUPERUSER)
public class CommandPrefix
{
    @CmdInvoke
    public static void setPrefix(CallObj co, @CmdPar("prefix") String prefix)
    {
        CommandAssert.stringNotTooLong(prefix, 8, "The prefix is too long.");

        if (prefix.contains("@"))
        {
            throw new CommandException("The prefix can't contain @.");
        }

        co.sc.setPrefix(prefix);

        co.guild.getMember(co.bot).modifyNickname("[" + prefix + "] " + Botdiril.BRANDING).queue(success ->
        {
            MR.send(co.textChannel, "Prefix set to: " + prefix);
        });
    }
}
