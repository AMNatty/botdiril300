package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CratePools;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;

public class ItemCrateHyper extends ItemCrate
{
    public static int CONTENTS = 8;

    public static int DISPLAY_LIMIT = 12;

    private static final double BLESS_CHANCE = 0.3;

    public ItemCrateHyper()
    {
        super("hypercrate", Icons.CRATE_HYPER, "Hyper Crate");
        this.setDescription("This crate has been so overpowered that even when the universe reached heat death, this crate was overflowing with energy.");

        ShopEntries.addCoinBuy(this, 1_200_000_000);
        ShopEntries.addTokenBuy(this, 20_000_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            ip.addItem((Item) CratePools.hyperCrate.draw().draw(), 1);
        }

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

        for (int j = 0; j < amount; j++)
        {
            if (Botdiril.RDG.nextUniform(0, 1) < BLESS_CHANCE)
            {
                Curser.bless(co);
            }
        }

        var dc = ip.distintCount();

        if (dc > DISPLAY_LIMIT)
            sb.append(String.format("\nand %d more different items...", dc - DISPLAY_LIMIT));

        sb.append(String.format("\n**Total %d items.**", ip.totalCount()));

        co.po.addLong(EnumStat.CRATES_OPENED.getName(), amount);

        MR.send(co.textChannel, sb.toString());
    }
}
