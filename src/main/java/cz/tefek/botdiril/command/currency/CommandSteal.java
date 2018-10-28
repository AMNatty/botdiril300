package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.core.entities.User;

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
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.RewardParser;
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

        CommandAssert.assertTimer(co.ui, Timers.steal, "You need to wait $ before using `" + co.sc.getPrefix() + "steal` again.");

        CommandAssert.numberNotBelowL(co.ui.getCoins(), 1000, "You need at least 1000 " + Icons.COIN + " to rob someone.");

        if (co.ui.useTimer(Timers.gambleXP) == -1)
        {
            var lvl = co.ui.getLevel();
            XPAdder.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * Botdiril.RDG.nextUniform(0.00001, 0.0001)));
        }

        var other = new UserInventory(user.getIdLong());

        BotMain.sql.lock();

        var maxSteal = RewardParser.parse(XPRewards.getRewardsForLvl(co.ui.getLevel())).stream().filter(ip -> ip.getItem().equals(Items.coins)).mapToLong(ItemPair::getAmount).findFirst().orElse(0);

        var mod = Botdiril.RANDOM.nextDouble() * 0.8 - 0.4;

        var stole = Math.min(Math.round(other.getCoins() * mod), maxSteal);

        if (stole <= 0)
        {
            MR.send(co.textChannel, "You didn't manage to steal anything, better luck next time... ;)");
            return;
        }

        co.ui.addCoins(stole);
        other.addCoins(-stole);
        MR.send(co.textChannel, String.format("It worked out! You stole %d %s.", stole, Icons.COIN));

        BotMain.sql.unlock();
    }
}
