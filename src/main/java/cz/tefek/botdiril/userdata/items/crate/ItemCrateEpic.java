package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CratePools;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.util.BotdirilFmt;

public class ItemCrateEpic extends ItemCrate
{
    public static int CONTENTS = 8;
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
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            ip.addItem((Item) CratePools.epicCrate.draw().draw(), 1);
        }

        var i = 0;

        for (var itemPair : ip)
        {
            var item = itemPair.getItem();
            var amt = itemPair.getAmount();

            co.ui.addItem(item, amt);

            if (i <= DISPLAY_LIMIT)
            {
                sb.append(String.format("\n%sx %s", BotdirilFmt.format(amt), item.inlineDescription()));
            }

            i++;
        }

        var dc = ip.distintCount();

        if (dc > DISPLAY_LIMIT)
        {
            sb.append(String.format("\nand %d more different items...", dc - DISPLAY_LIMIT));
        }

        sb.append(String.format("\n**Total %s items.**", BotdirilFmt.format(ip.totalCount())));

        co.po.addLong(EnumStat.CRATES_OPENED.getName(), amount);

        MR.send(co.textChannel, sb.toString());
    }
}
