package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateGolden extends ItemCrate
{
    public ItemCrateGolden()
    {
        super("goldencrate", Icons.CRATE_GOLDEN, "Golden Crate");
        this.setDescription("Ever wondered where all the keks you gamble away go? They are in these crates.");

        ShopEntries.addCoinBuy(this, 50000);
        ShopEntries.addCoinSell(this, 20000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, "This crate is coming soon...");
        co.ui.addItem(this, amount);
    }
}
