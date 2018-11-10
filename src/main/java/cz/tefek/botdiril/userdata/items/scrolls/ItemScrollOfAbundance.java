package cz.tefek.botdiril.userdata.items.scrolls;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;

public class ItemScrollOfAbundance extends Item implements IOpenable
{
    public ItemScrollOfAbundance()
    {
        super("scrollofabundance", Icons.SCROLL_RARE, "Scroll of Abundance");
        this.setDescription(MessageFormat.format("Use to instantly double your {0}.", Icons.COIN));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var newCoins = co.ui.getCoins() * Math.round(Math.pow(2, amount));
        co.ui.setCoins(newCoins);
        MR.send(co.textChannel, MessageFormat.format("You now have **{0}** {1}.", newCoins, Icons.COIN));
    }

}
