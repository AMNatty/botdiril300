package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateUltimate extends ItemCrate
{
    public ItemCrateUltimate()
    {
        super("ultimatecrate", Icons.CRATE_ULTIMATE, "Ultimate Crate");
        this.setDescription("The ultimate evolution of the crate opening experience.");

        ShopEntries.addCoinBuy(this, 5_000_000);
        ShopEntries.addCoinSell(this, 2_400_000);
        ShopEntries.addTokenBuy(this, 35_000);
    }

    @Override
    public void open(CallObj co, int amount)
    {

    }
}
