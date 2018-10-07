package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.IOpenable;
import cz.tefek.botdiril.userdata.items.Item;

public abstract class ItemCrate extends Item implements IOpenable
{
    public ItemCrate(String name, String icon, String localizedName)
    {
        super(name, icon, localizedName);
    }

    @Override
    public String getFootnote(CallObj co)
    {
        return "Open using `" + co.sc.getPrefix() + "open " + this.getName() + "`. Keep in mind you need a key to do so.";
    }
}
