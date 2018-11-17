package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CratePools;

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
        var fm = String.format("You open %d %s and get the following items:", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
            ip.addItem((Item) CratePools.epicCrate.draw().draw(), 1);

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
