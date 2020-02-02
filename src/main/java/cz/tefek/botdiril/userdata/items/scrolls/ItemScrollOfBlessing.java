package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.Arrays;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.tempstat.Curser;

public class ItemScrollOfBlessing extends Item implements IOpenable
{
    public ItemScrollOfBlessing()
    {
        super("scrollofblessing", Icons.SCROLL_RARE, "Scroll of Blessing");
        this.setDescription("You feel blessed.");
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.rainbowGem, 2), new ItemPair(Items.diamond), new ItemPair(Items.timewarpCrystal)), 1, this));
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
