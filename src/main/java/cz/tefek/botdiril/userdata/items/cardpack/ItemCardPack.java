package cz.tefek.botdiril.userdata.items.cardpack;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Item;

public abstract class ItemCardPack extends Item implements IOpenable
{
    public ItemCardPack(String name, String icon, String localizedName)
    {
        super(name, icon, localizedName);
    }

    @Override
    public abstract void open(CallObj co, long amount);

    @Override
    public String getFootnote(CallObj co)
    {
        return "Open using `" + co.sc.getPrefix() + "open " + this.getName() + "`. Is guaranteed to contain at least five cards.";
    }
}
