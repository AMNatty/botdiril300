package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateEpic extends ItemCrate
{
    public ItemCrateEpic()
    {
        super("epiccrate", Icons.CRATE_EPIC, "Epic Crate");
        this.setDescription("No. Just because it has epic in the name, it doesn't mean it contains Fortnite skins.");

        ShopEntries.addCoinBuy(this, 360_000);
        ShopEntries.addCoinSell(this, 180_000);
        ShopEntries.addTokenBuy(this, 2250);
    }

    @Override
    public void open(CallObj co, int amount)
    {

    }
}
