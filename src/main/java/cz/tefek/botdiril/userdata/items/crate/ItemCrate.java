package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Item;

public abstract class ItemCrate extends Item
{
    public ItemCrate(String name, String icon, String localizedName)
    {
        super(name, icon, localizedName);
    }

    public abstract void open(CallObj co, int amount);

    @Override
    public String getFootnote(CallObj co)
    {
        return "Open using `" + co.sc.getPrefix() + "open " + this.getName() + "`. Keep in mind you need a key to do so.";
    }
}
