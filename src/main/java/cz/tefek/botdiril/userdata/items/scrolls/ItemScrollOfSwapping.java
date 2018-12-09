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

public class ItemScrollOfSwapping extends Item implements IOpenable
{
    public ItemScrollOfSwapping()
    {
        super("scrollofswapping", Icons.SCROLL, "Scroll of Swapping");
        this.setDescription("Reverse your gems!");
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.toolBox, 3), new ItemPair(Items.blackGem, 1)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        co.ui.addItem(this, amount - 1);

        // red -> green
        // green -> red
        var red = co.ui.howManyOf(Items.redGem);
        var green = co.ui.howManyOf(Items.greenGem);
        co.ui.setItem(Items.redGem, green);
        co.ui.setItem(Items.greenGem, red);

        // blue -> purple
        // purple -> blue
        var blue = co.ui.howManyOf(Items.blueGem);
        var purple = co.ui.howManyOf(Items.purpleGem);
        co.ui.setItem(Items.purpleGem, blue);
        co.ui.setItem(Items.blueGem, purple);

        // black -> rainbow
        // rainbow -> black        
        var black = co.ui.howManyOf(Items.blackGem);
        var rainbow = co.ui.howManyOf(Items.rainbowGem);
        co.ui.setItem(Items.rainbowGem, black);
        co.ui.setItem(Items.blackGem, rainbow);

        MR.send(co.textChannel, "**You reversed your gems!**");

        co.po.close();
    }
}
