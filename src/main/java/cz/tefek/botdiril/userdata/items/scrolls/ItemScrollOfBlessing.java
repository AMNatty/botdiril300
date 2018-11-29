package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.Arrays;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.tempstat.Curser;

public class ItemScrollOfBlessing extends Item implements IOpenable
{
    public ItemScrollOfBlessing()
    {
        super("scrollofblessing", Icons.SCROLL_RARE, "Scroll of Blessing");
        this.setDescription("You feel blessed.");
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.rainbowGem, 2), new ItemPair(Items.diamond)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        for (int i = 0; i < amount; i++)
        {
            Curser.bless(co);
        }

        co.po.close();
    }
}
