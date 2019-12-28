package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.Member;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.items.Item;

@Command(value = "resetitem", category = CommandCategory.SUPERUSER, aliases = {}, description = "Resets the amount of an item to 0.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandResetAmount
{
    @CmdInvoke
    public static void resetItem(CallObj co, @CmdPar("item") Item item)
    {
        co.ui.setItem(item, 0);

        MR.send(co.textChannel, String.format("Reset **%s** to **0**!", item.inlineDescription()));
    }

    @CmdInvoke
    public static void resetItem(CallObj co, @CmdPar("user") Member user, @CmdPar("item") Item item)
    {
        new UserInventory(user.getUser().getIdLong()).setItem(item, 0);

        MR.send(co.textChannel, String.format("Reset **%s's** **%s** to **0**!", user.getEffectiveName(), item.inlineDescription()));
    }
}
