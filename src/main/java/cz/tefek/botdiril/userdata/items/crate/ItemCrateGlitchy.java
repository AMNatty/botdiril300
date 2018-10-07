package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateGlitchy extends ItemCrate
{
    public ItemCrateGlitchy()
    {
        super("glitchycrate", Icons.CRATE_GLITCHY, "Glitchy Crate");
        this.setDescription("47 6f 6f 64 20 6a 6f 62 20 79 6f 75 20 63 61 6e 20 72 65 61 64 20 74 68 69 73 20 74 65 78 74 2e 20 4e 6f 77 20 6f 70 65 6e 20 74 68 65 20 63 72 61 74 65 2e");

        ShopEntries.addCoinBuy(this, 500_000_000);
        ShopEntries.addTokenBuy(this, 3_200_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {

    }
}
