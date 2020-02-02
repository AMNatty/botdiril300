package cz.tefek.botdiril.command.general;

import java.util.Arrays;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;

@Command(value = "preferences", aliases = { "options",
        "prefs" }, category = CommandCategory.GENERAL, description = "Show your user preferences.")
public class CommandPreferences
{
    @CmdInvoke
    public static void list(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setTitle("Preferences");
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());
        eb.setDescription(String.format("Your Botdiril preferences.\nYou can update an option with `%sprefupdate <option ID> on/off`.", co.sc.getPrefix()));

        Arrays.stream(EnumUserPreference.values()).forEach(pref ->
        {
            var indicator = UserPreferences.isBitEnabled(co.po, pref) ? "on" : "off";
            eb.addField(pref.getLocalizedName(), String.format("**ID:**`%s`\n**Status:** *%s*", pref.getID(), indicator), true);
        });

        MR.send(co.textChannel, eb.build());

    }
}
