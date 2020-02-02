package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.Arrays;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.xp.XPAdder;

public class ItemScrollOfIntelligence extends Item implements IOpenable
{
    private static final long XP = 100_000;

    public ItemScrollOfIntelligence()
    {
        super("scrollofintelligence", Icons.SCROLL_RARE, "Scroll of Intelligence");
        this.setDescription(MessageFormat.format("Use to instantly gain **{0} XP**.", XP));
        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.scrollOfLesserIntelligence, 5), new ItemPair(Items.greenGem, 10), new ItemPair(Items.trash, 32)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, MessageFormat.format("You read the **{0}**... **[+{1} XP]**", this.inlineDescription(), amount * XP));
        XPAdder.addXP(co, XP * amount);

        co.po.close();
    }

}
