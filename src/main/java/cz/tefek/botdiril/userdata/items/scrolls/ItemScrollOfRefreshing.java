package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.Arrays;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.timers.Timers;

public class ItemScrollOfRefreshing extends Item implements IOpenable
{
    public ItemScrollOfRefreshing()
    {
        super("scrollofrefreshing", Icons.SCROLL, "Scroll of Refreshing");
        this.setDescription("Instantly resfresh the cooldown of your **daily** loot.");
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.timewarpCrystal, 1), new ItemPair(Items.gold, 1000), new ItemPair(Items.trash, 24)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        co.ui.resetTimer(Timers.daily);
        MR.send(co.textChannel, "You refreshed your **daily** command cooldown!");

        co.po.close();
    }

}
