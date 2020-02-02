package cz.tefek.botdiril.command.interactive;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.text.MessageFormat;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(aliases = {
        "rob" }, category = CommandCategory.INTERACTIVE, description = "Hehe. Time to rob someone.", value = "steal", levelLock = 10)
public class CommandSteal
{
    @CmdInvoke
    public static void steal(CallObj co, @CmdPar("who to rob") User user)
    {
        CommandAssert.assertTimer(co.ui, Timers.steal, "You need to wait **$** before trying to **steal** again.");

        try
        {
            CommandAssert.assertNotEquals(co.caller.getIdLong(), user.getIdLong(), "You can't rob yourself. Or can you? :thinking:");

            CommandAssert.numberNotBelowL(co.ui.getCoins(), 1000, "You need at least **1'000** " + Icons.COIN + " to rob someone.");
        }
        catch (CommandException e)
        {
            co.ui.resetTimer(Timers.steal);

            throw e;
        }

        if (co.ui.useTimer(Timers.gambleXP) == -1)
        {
            var lvl = co.ui.getLevel();
            XPAdder.addXP(co, lvl * 20);
        }

        var other = new UserInventory(user.getIdLong());

        BotMain.sql.lock();

        var lvl = co.ui.getLevel();
        var lvlOther = other.getLevel();

        var lvlMod = Math.pow(10, (lvlOther - lvl) / 100);

        var maxSteal = Math.round(Math.pow(lvl, 1.8) * 100);

        var mod = Botdiril.RANDOM.nextDouble() * 0.8 - 0.4;

        try (var po = new PropertyObject(user.getIdLong()))
        {
            if (Curser.isCursed(po, EnumCurse.EASIER_TO_ROB))
            {
                mod = Botdiril.RANDOM.nextDouble() * 0.6 - 0.2;
            }

            if (Curser.isBlessed(po, EnumBlessing.STEAL_IMMUNE))
            {
                co.ui.resetTimer(Timers.steal);

                var eb = new EmbedBuilder();
                eb.setTitle("Steal");
                eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
                eb.setColor(0x008080);
                eb.setDescription("That person is immune. For some reason.");
                MR.send(co.textChannel, eb.build());

                return;
            }
        }

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

        if (Curser.isBlessed(co, EnumBlessing.PICKPOCKET) && Botdiril.RDG.nextUniform(0, 1) > 0.75)
        {
            maxSteal *= 100;
        }

        var stole = Math.min(Math.round(other.getCoins() * mod), Math.round(maxSteal * lvlMod));

        if (stole <= 0)
        {
            eb.appendDescription("You didn't manage to steal anything, better luck next time...\n;)");
        }
        else
        {
            if (co.po.getLongOrDefault(EnumStat.BIGGEST_STEAL.getName(), 0) < stole)
            {
                co.po.setLong(EnumStat.BIGGEST_STEAL.getName(), stole);
            }

            co.ui.addCoins(stole);
            other.addCoins(-stole);
            eb.appendDescription("It worked out!");
            eb.addField("Stolen coins", String.format("**%s %s**.", BotdirilFmt.format(stole), Icons.COIN), false);

            try (var otherProps = new PropertyObject(user.getIdLong()))
            {
                co.po.incrementLong(EnumStat.TIMES_ROBBED.getName());
            }
        }

        MR.send(co.textChannel, eb.build());

        BotMain.sql.unlock();
    }
}
