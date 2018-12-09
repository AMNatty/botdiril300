package cz.tefek.botdiril.userdata.items.scrolls;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
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
        var xpNeeded = XPRewards.getXPNeededForLvlUp(co.ui.getLevel(), co.ui.getXP());

        XPAdder.addXP(co, xpNeeded);

        co.po.close();
    }

}
