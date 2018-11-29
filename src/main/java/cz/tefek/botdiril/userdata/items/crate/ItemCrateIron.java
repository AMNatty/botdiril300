package cz.tefek.botdiril.userdata.items.crate;

import java.util.Arrays;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.pools.CratePools;
import cz.tefek.botdiril.userdata.stat.EnumStat;

public class ItemCrateIron extends ItemCrate
{
    public static int CONTENTS = 8;
    public static int DISPLAY_LIMIT = 12;

    public ItemCrateIron()
    {
        super("ironcrate", Icons.CRATE_IRON, "Iron Crate");
        this.setDescription("Despite the name, I don't think the crate is trash tier.");

        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.iron, 1_000), new ItemPair(Items.crateBasic)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
            ip.addItem((Item) CratePools.uncommonCrate.draw().draw(), 1);

        var keks = Botdiril.RDG.nextLong(40_000 * amount, 60_000 * amount);

        sb.append(String.format("\n%d %s", keks, Icons.KEK));

        co.ui.addKeks(keks);

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

        sb.append(String.format("\n**Total %d items.**", ip.totalCount()));

        co.po.addLong(EnumStat.CRATES_OPENED.getName(), amount);

        MR.send(co.textChannel, sb.toString());
    }
}
