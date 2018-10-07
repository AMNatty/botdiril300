package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateUncommon extends ItemCrate
{

    public ItemCrateUncommon()
    {
        super("uncommoncrate", Icons.CRATE_UNCOMMON, "Uncommon Crate");
        this.setDescription("A rare version of Common Crate. Uncommon crates are, well... *uncommon*.");

        ShopEntries.addCoinBuy(this, 115200);
        ShopEntries.addCoinSell(this, 57600);
        ShopEntries.addTokenBuy(this, 800);
    }

    @Override
    public void open(CallObj co, long amount)
    {

    }

}
