package cz.tefek.botdiril.userdata.items.crate;

import java.util.ArrayList;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.stat.EnumStat;

public class ItemCrateGlitchy extends ItemCrate
{
    public static int CONTENTS = 8;
    public static int DISPLAY_LIMIT = 12;

    public ItemCrateGlitchy()
    {
        super("glitchycrate", Icons.CRATE_GLITCHY, "Glitchy Crate");
        this.setDescription("47 6f 6f 64 20 6a 6f 62 20 79 6f 75 20 63 61 6e 20 72 65 61 64 20 74 68 69 73 20 74 65 78 74 2e 20 4e 6f 77 20 6f 70 65 6e 20 74 68 65 20 63 72 61 74 65 2e");

        ShopEntries.addCoinBuy(this, 1_000_000_000);
        ShopEntries.addTokenBuy(this, 15_000_000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        var candidates = new ArrayList<>(Item.items());
        candidates.removeAll(Items.leagueItems);

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            ip.addItem(candidates.get(Botdiril.RANDOM.nextInt(candidates.size())));
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

        var dc = ip.distintCount();

        if (dc > DISPLAY_LIMIT)
            sb.append(String.format("\nand %d more different items...", dc - DISPLAY_LIMIT));

        sb.append(String.format("\n**Total %d items.**", ip.totalCount()));

        co.po.addLong(EnumStat.CRATES_OPENED.getName(), amount);

        MR.send(co.textChannel, sb.toString());
    }
}
