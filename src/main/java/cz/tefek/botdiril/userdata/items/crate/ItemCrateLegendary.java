package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateLegendary extends ItemCrate
{
    public ItemCrateLegendary()
    {
        super("legendarycrate", Icons.CRATE_LEGENDARY, "Legendary Crate");
        this.setDescription("Contrary to popular beliefs, this crate is actually worth opening.");

        ShopEntries.addCoinBuy(this, 1_300_000);
        ShopEntries.addCoinSell(this, 260_000);
        ShopEntries.addTokenBuy(this, 50_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, "This crate is coming soon...");
        co.ui.addItem(this, amount);
    }
}
