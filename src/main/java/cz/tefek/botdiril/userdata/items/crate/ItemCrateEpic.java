package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateEpic extends ItemCrate
{
    public static int CONTENTS = 7;
    public static int DISPLAY_LIMIT = 12;

    public ItemCrateEpic()
    {
        super("epiccrate", Icons.CRATE_EPIC, "Epic Crate");
        this.setDescription("No. Just because it has epic in the name, it doesn't mean it contains Fortnite skins.");

        ShopEntries.addCoinBuy(this, 360_000);
        ShopEntries.addCoinSell(this, 72_000);
        ShopEntries.addTokenBuy(this, 4500);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, "This crate is coming soon...");
        co.ui.addItem(this, amount);
    }
}
