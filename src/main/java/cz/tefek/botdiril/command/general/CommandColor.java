package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.core.EmbedBuilder;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "color", category = CommandCategory.GENERAL, aliases = { "col" }, description = "Gets a random color.")
public class CommandColor
{
    @CmdInvoke
    public static void choose(CallObj co)
    {
        var col = Botdiril.RDG.nextInt(0x000000, 0xffffff);

        var eb = new EmbedBuilder();
        eb.setTitle("Here's your random color.");
        eb.setColor(col);
        var hexInt = Integer.toHexString(col);
        var hex = "000000".substring(hexInt.length()) + hexInt;
        eb.setDescription("#" + hex);
        MR.send(co.textChannel, eb.build());
    }
}
