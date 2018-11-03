package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.core.entities.Member;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;

@Command(value = "resetcooldowns", aliases = {
        "resetcds" }, category = CommandCategory.SUPERUSER, description = "Reset timers on everything for a user.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandResetCooldowns
{
    @CmdInvoke
    public static void resetCooldowns(CallObj co)
    {
        resetCooldowns(co, co.callerMember);
    }

    @CmdInvoke
    public static void resetCooldowns(CallObj co, @CmdPar("user") Member user)
    {
        BotMain.sql.exec("DELETE FROM " + UserInventory.TABLE_TIMERS + " WHERE fk_us_id=?", stat ->
        {
            var res = stat.executeUpdate();

            if (res == 0)
            {
                MR.send(co.textChannel, "No timers were reset.");
                return 0;
            }

            MR.send(co.textChannel, String.format("Reset **%d** timer(s) of **%s**.", res, user.getEffectiveName()));
            return res;

        }, new UserInventory(user.getUser().getIdLong()).getFID());
    }
}
