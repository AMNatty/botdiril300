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

public class ItemCrateInfernal extends ItemCrate
{
    public static int CONTENTS = 8;

    public static int DISPLAY_LIMIT = 12;

    private static final double CURSE_CHANCE = 0.3;

    public ItemCrateInfernal()
    {
        super("infernalcrate", Icons.CRATE_INFERNAL, "Infernal Crate");
        this.setDescription("This crate, forged in the depths of hell, will have the items that everyone would like to have. It might curse you, so be careful.");

        ShopEntries.addCoinBuy(this, 1_000_000_000);
        ShopEntries.addTokenBuy(this, 15_000_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            ip.addItem((Item) CratePools.infernalCrate.draw().draw(), 1);
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
            if (Botdiril.RDG.nextUniform(0, 1) < CURSE_CHANCE)
            {
                Curser.curse(co);
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
