package cz.tefek.botdiril.userdata.items.crate;

import java.util.Arrays;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.pools.CratePools;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.util.BotdirilFmt;

public class ItemCrateGolden extends ItemCrate
{
    public static int CONTENTS = 10;
    public static int DISPLAY_LIMIT = 12;

    public ItemCrateGolden()
    {
        super("goldencrate", Icons.CRATE_GOLDEN, "Golden Crate");
        this.setDescription("Ever wondered where all the keks you gamble away go? They are in these crates.");

        CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.gold, 1_000), new ItemPair(Items.crateLegendary)), 1, this));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            ip.addItem((Item) CratePools.legendaryCrate.draw().draw(), 1);
        }

        var keks = Botdiril.RDG.nextLong(2_000_000 * amount, 5_000_000 * amount);

        sb.append(String.format("\n%s %s", BotdirilFmt.format(keks), Icons.KEK));

        co.ui.addKeks(keks);

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
