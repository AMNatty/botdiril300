package cz.tefek.botdiril.userdata.items.scrolls;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.xp.XPAdder;
import cz.tefek.botdiril.userdata.xp.XPRewards;

public class ItemScrollOfIntelligence2 extends Item implements IOpenable
{
    public ItemScrollOfIntelligence2()
    {
        super("scrollofintelligenceii", Icons.SCROLL_UNIQUE, "Scroll of Intelligence II");
        this.setDescription("Use to instantly **level up**.");
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var xpNeeded = 0L;

        for (int i = 0; i < amount; i++)
        {
            xpNeeded += XPRewards.getXPNeededForLvlUp(co.ui.getLevel() + i, i == 0 ? co.ui.getXP() : 0);
        }

        XPAdder.addXP(co, xpNeeded);

        co.po.close();
    }

}
