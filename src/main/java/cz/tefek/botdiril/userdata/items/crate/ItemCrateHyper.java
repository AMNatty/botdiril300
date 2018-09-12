package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateHyper extends ItemCrate
{
    public ItemCrateHyper()
    {
        super("hypercrate", Icons.CRATE_HYPER, "Hyper Crate");
        this.setDescription("This crate has been so overpowered that even when the universe reached heat death, this crate was overflowing with energy.");

        ShopEntries.addCoinBuy(this, 600_000_000);
        ShopEntries.addTokenBuy(this, 4_000_000);
    }

    @Override
    public void open(CallObj co, int amount)
    {

    }
}