package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.Arrays;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;

public class ItemScrollOfRefreshing extends Item implements IOpenable
{
    public ItemScrollOfRefreshing()
    {
        super("scrollofrefreshing", Icons.SCROLL, "Scroll of Refreshing");
        this.setDescription("Instantly resfresh the cooldown of your **daily** loot.");
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.greenGem, 2), new ItemPair(Items.redGem, 1), new ItemPair(Items.trash, 8)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        co.ui.resetTimer(Timers.daily);
        MR.send(co.textChannel, "You refreshed your **daily** command cooldown!");
    }

}
