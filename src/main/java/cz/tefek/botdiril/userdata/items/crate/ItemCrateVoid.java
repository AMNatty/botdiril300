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

public class ItemCrateVoid extends ItemCrate
{
    public static int CONTENTS = 11;

    public static int DISPLAY_LIMIT = 12;

    private static final double EMPTY_CHANCE = 0.45;

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
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int j = 0; j < amount; j++)
        {
            if (Botdiril.RDG.nextUniform(0, 1) < EMPTY_CHANCE)
            {
                continue;
            }

            for (int i = 0; i < CONTENTS; i++)
            {
                ip.addItem((Item) CratePools.infernalCrate.draw().draw(), 1);
            }
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
