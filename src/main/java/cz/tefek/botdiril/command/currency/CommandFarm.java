package cz.tefek.botdiril.command.currency;

import java.util.Random;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;

@Command(value = "farm", category = CommandCategory.CURRENCY, aliases = {}, description = "Farm to get some gold, experience and items.")
public class CommandFarm
{
    @CmdInvoke
    public static void farm(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.farm, "You need to wait $ to farm again.");

        var random = new Random();

        var cannon = random.nextDouble() > 0.75;

        var coins = cannon ? 500 : 300;

        var xp = 200;

        var drops = Items.leagueItems.get(random.nextInt(Items.leagueItems.size()));

        co.ui.addItem(drops);
        co.ui.addCoins(coins);
        XPAdder.addXP(co, xp);

        final var cannonString = "You farmed and didn't actually miss the cannon minion so you got more loot: %d %s, %d xp and %s.";
        final var basicString = "You farmed and got %d %s, %d xp and %s.";

        var op = String
                .format(cannon ? cannonString : basicString, coins, EnumCurrency.COINS
                        .getIcon(), xp, drops.inlineDescription());

        MR.send(co.textChannel, op);
    }
}
