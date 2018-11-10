package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class ItemCrateVoid extends ItemCrate
{
    public ItemCrateVoid()
    {
        super("voidcrate", Icons.CRATE_VOID, "Void Crate");
        this.setDescription("This crate in an quantum superposition of being empty or full at one time. The only way to check is to look inside.");

        ShopEntries.addCoinBuy(this, 800_000_000);
        ShopEntries.addTokenBuy(this, 12_000_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, "This crate is coming soon...");
        co.ui.addItem(this, amount);
    }
}
