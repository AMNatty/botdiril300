package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;

@Command(value = "preferenceupdate", aliases = { "optionupdate", "optupdate", "prefupdate", "prefsupdate",
        "preferenceset", "optset", "prefset", "setpreference", "setopt", "setoption", "setpref", "updatepreference",
        "updateopt", "updatepref",
        "updateoption" }, category = CommandCategory.GENERAL, description = "Update your user preferences.")
public class CommandPreferenceUpdate
{
    @CmdInvoke
    public static void update(CallObj co, @CmdPar("option ID") EnumUserPreference option, @CmdPar("on/off") boolean enable)
    {
        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());

        if (enable)
        {
            UserPreferences.setBit(co.po, option);
        }
        else
        {
            UserPreferences.clearBit(co.po, option);
        }

        eb.setTitle("Preference updated");
        var indicator = enable ? "on" : "off";

        eb.setDescription(String.format("Succesfully set `%s` to `%s`.", option.getLocalizedName(), indicator));

        MR.send(co.textChannel, eb.build());

    }
}
