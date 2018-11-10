package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.text.MessageFormat;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(aliases = {
        "rob" }, category = CommandCategory.CURRENCY, description = "Hehe. Time to rob someone.", value = "steal", levelLock = 10)
public class CommandSteal
{
    @CmdInvoke
    public static void print(CallObj co, @CmdPar("who to rob") User user)
    {
        CommandAssert.assertNotEquals(co.caller.getIdLong(), user.getIdLong(), "You can't rob yourself. Or can you? :thinking:");

        CommandAssert.assertTimer(co.ui, Timers.steal, "You need to wait **$** before trying to **steal** again.");

        CommandAssert.numberNotBelowL(co.ui.getCoins(), 1000, "You need at least **1000** " + Icons.COIN + " to rob someone.");

        if (co.ui.useTimer(Timers.gambleXP) == -1)
        {
            var lvl = co.ui.getLevel();
            XPAdder.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * Botdiril.RDG.nextUniform(0.00001, 0.0001)));
        }

        var other = new UserInventory(user.getIdLong());

        BotMain.sql.lock();

        var lvl = co.ui.getLevel();
        var lvlOther = other.getLevel();

        var lvlMod = Math.pow(10, (lvlOther - lvl) / 100);

        var maxSteal = Math.round(Math.pow(lvl, 1.35) * 100);

        var mod = Botdiril.RANDOM.nextDouble() * 0.8 - 0.4;

        var eb = new EmbedBuilder();
        eb.setTitle("Steal");
        eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
        eb.setColor(0x008080);

        if (co.ui.howManyOf(Items.toolBox) > 0)
        {
            eb.appendDescription(MessageFormat.format("You used a **{0}**...\n", Items.toolBox.inlineDescription()));
            co.ui.addItem(Items.toolBox, -1);
            mod = Math.abs(mod);
        }

        var stole = Math.min(Math.round(other.getCoins() * mod), Math.round(maxSteal * lvlMod));

        if (stole <= 0)
        {
            eb.appendDescription("You didn't manage to steal anything, better luck next time...\n;)");
        }
        else
        {
            co.ui.addCoins(stole);
            other.addCoins(-stole);
            eb.appendDescription("It worked out!");
            eb.addField("Stolen coins", String.format("**%d %s**.", stole, Icons.COIN), false);
        }

        MR.send(co.textChannel, eb.build());

        BotMain.sql.unlock();
    }
}
