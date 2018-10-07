package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CratePools;

public class ItemCrateBasic extends ItemCrate
{
    public static int CONTENTS = 5;
    public static int DISPLAY_LIMIT = 12;

    public ItemCrateBasic()
    {
        super("crate", Icons.CRATE_BASIC, "Crate");
        this.setDescription("Just an ordinary crate, nothing to see here. Maybe a few keks.");
        ShopEntries.addCoinBuy(this, 15000);
        ShopEntries.addTokenBuy(this, 100);
        ShopEntries.addCoinSell(this, 5000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("You open %d %s and get the following items:", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
            ip.addItem((Item) CratePools.basicCrate.draw().draw(), 1);

        var i = 0;

        for (var itemPair : ip)
        {
            var item = itemPair.getItem();
            var amt = itemPair.getAmount();

            co.ui.addItem(item, amt);

            if (i <= DISPLAY_LIMIT)
                sb.append(String.format("\n%dx %s", amt, item.inlineDescription()));

            i++;
        }

        var dc = ip.distintCount();

        if (dc > DISPLAY_LIMIT)
            sb.append(String.format("\nand %d more different items...", dc - DISPLAY_LIMIT));

        sb.append(String.format("\nTotal %d items.", ip.totalCount()));

        MR.send(co.textChannel, sb.toString());
    }
}
