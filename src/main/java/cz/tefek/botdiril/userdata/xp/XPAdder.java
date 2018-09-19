package cz.tefek.botdiril.userdata.xp;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.ItemPair;

public class XPAdder
{
    @SuppressWarnings("deprecation")
    public static void addXP(CallObj co, long xp)
    {
        co.ui.addXP(xp);

        var caxp = co.ui.getXP();
        var lvl = co.ui.getLevel();
        var i = lvl;
        var xpSum = XPRewards.getXPAtLevel(i);

        var rewards = new ArrayList<ItemPair>();

        while (xpSum < caxp)
        {
            RewardParser.parse(XPRewards.getRewardsForLvl(i)).forEach(ip ->
            {
                var pair = rewards.stream().filter(rev -> rev.getItem().equals(ip.getItem())).findAny();

                if (pair.isPresent())
                    pair.get().addAmount(ip.getAmount());
                else
                    rewards.add(ip);
            });

            if (++i == XPRewards.getMaxLevel())
            {
                i = XPRewards.getMaxLevel();
                break;
            }

            xpSum += XPRewards.getXPAtLevel(i);
        }

        for (ItemPair itemPair : rewards)
        {
            co.ui.addItem(itemPair.getItem(), itemPair.getAmount());
        }

        if (i == XPRewards.getMaxLevel())
        {
            co.ui.setXP(0);
        } else
        {
            co.ui.setXP(caxp - (xpSum - XPRewards.getXPAtLevel(i)));
        }

        if (i > lvl)
        {
            co.ui.setLevel(i);

            var rw = rewards.stream().map(ip -> ip.getAmount() + "x " + ip.getItem().inlineDescription()).collect(Collectors.joining("\n"));

            MR.send(co.textChannel, String.format("***You advanced to level %d!***\n**Rewards:**\n%s", i, rw));
        }
    }
}
