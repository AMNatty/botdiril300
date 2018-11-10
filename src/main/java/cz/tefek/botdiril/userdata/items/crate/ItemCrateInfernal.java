package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateInfernal extends ItemCrate
{
    public ItemCrateInfernal()
    {
        super("infernalcrate", Icons.CRATE_INFERNAL, "Infernal Crate");
        this.setDescription("This crate, forged in the depths of hell, will have the items that everyone would like to have. It might curse you, so be careful.");

        ShopEntries.addCoinBuy(this, 1_000_000_000);
        ShopEntries.addTokenBuy(this, 15_000_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, "This crate is coming soon...");
        co.ui.addItem(this, amount);
    }
}
