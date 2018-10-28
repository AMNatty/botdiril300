package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.core.entities.Role;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.serverdata.RolePreferences;

@Command(value = "unbind", aliases = "unbindpower", category = CommandCategory.ADMINISTRATIVE, description = "Unbind a power from a role.", powerLevel = EnumPowerLevel.SUPERUSER)
public class CommandUnbindPower
{
    @CmdInvoke
    public static void bind(CallObj co, @CmdPar("role") Role role, @CmdPar("power") EnumPowerLevel powerLevel)
    {
        var mp = EnumPowerLevel.getManageablePowers(co.callerMember, co.textChannel);

        CommandAssert.assertTrue(mp.contains(powerLevel), "You can't manage that power.");
        CommandAssert.assertTrue(co.callerMember.canInteract(role), "You can't manage that role!");

        var res = RolePreferences.add(role, powerLevel);

        switch (res)
        {
            case RolePreferences.REMOVED:
                MR.send(co.textChannel, MessageFormat.format("Removed **{0}** from **{1}**.", powerLevel.toString(), role.getName()));
                break;
            case RolePreferences.NOT_PRESENT:
                MR.send(co.textChannel, MessageFormat.format("**{0}** is not bound to **{1}**...", powerLevel.toString(), role.getName()));
                break;
            default:
                throw new CommandException(MessageFormat.format("Unexpected behaviour detected in the command, please report this to a developer.. Response code: {0}", res));
        }
    }
}
