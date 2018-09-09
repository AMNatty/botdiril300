package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateBasic extends ItemCrate
{
    public ItemCrateBasic()
    {
        super("crate", Icons.CRATE_BASIC, "Crate");
        this.setDescription("Just an ordinary crate, nothing to see here. Maybe a few keks.");
        ShopEntries.addCoinBuy(this, 15000);
        ShopEntries.addTokenBuy(this, 100);
        ShopEntries.addCoinSell(this, 5000);
    }

    @Override
    public void open(CallObj co, int amount)
    {

    }
}
