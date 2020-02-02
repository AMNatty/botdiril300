package cz.tefek.botdiril.command.gambling;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.internal.GlobalProperties;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(value = "jackpot", aliases = {}, category = CommandCategory.GAMBLING, description = "Shows the jackpot.")
public class CommandJackpot
{
    @CmdInvoke
    public static void tellJackpot(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Jackpot Stats");
        eb.setDescription("Jackpot stats are global and same for every server.");
        eb.setColor(0x008080);
        eb.addField("Jackpot", String.format("%d %s", GlobalProperties.get(GlobalProperties.JACKPOT), Icons.KEK), false);
        eb.addField("How much will stay after paying out the jackpot", String.format("%d %s", GlobalProperties.get(GlobalProperties.JACKPOT_RESET), Icons.KEK), false);
        eb.addField("How much YOU can pay out (scales with level and your " + Icons.KEK + "s).", String.format("%d %s", XPRewards.maxJackpot(co.ui.getLevel(), co.ui.getKeks()), Icons.KEK), false);

        MR.send(co.textChannel, eb.build());
    }
}
