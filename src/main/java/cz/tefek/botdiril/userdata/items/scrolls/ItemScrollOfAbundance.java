package cz.tefek.botdiril.userdata.items.scrolls;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;

public class ItemScrollOfAbundance extends Item implements IOpenable
{
    private static long LIMIT = 5_000_000;

    public ItemScrollOfAbundance()
    {
        super("scrollofabundance", Icons.SCROLL_RARE, "Scroll of Abundance");
        this.setDescription(MessageFormat.format("Use to instantly double your {0}, 5 million at most.", Icons.COIN));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var diff = Math.min(co.ui.getCoins() * Math.round(Math.pow(2, amount - 1)), LIMIT * amount);
        co.ui.addCoins(diff);
        MR.send(co.textChannel, MessageFormat.format("You now have **{0}** more {1}.", diff, Icons.COIN));

        co.po.close();
    }

}
