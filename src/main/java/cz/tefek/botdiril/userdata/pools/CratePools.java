package cz.tefek.botdiril.userdata.pools;

import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.Items;

public class CratePools
{
    public static final LootPool<Item> terribleRewards = new LootPool<>();
    public static final LootPool<Item> badRewards = new LootPool<>();
    public static final LootPool<Item> leagueRewards = new LootPool<>();
    public static final LootPool<Item> normalRewards = new LootPool<>();
    public static final LootPool<Item> goodRewards = new LootPool<>();
    public static final LootPool<Item> greatRewards = new LootPool<>();
    public static final LootPool<Item> amazingRewards = new LootPool<>();

    public static final PoolDrawer basicCrate = new PoolDrawer().add(10, terribleRewards).add(50, badRewards).add(60, leagueRewards).add(8, normalRewards).add(1, goodRewards);

    public static final PoolDrawer uncommonCrate = new PoolDrawer().add(5, terribleRewards).add(40, badRewards).add(30, leagueRewards).add(16, normalRewards).add(2, goodRewards).add(1, greatRewards);

    static
    {
        terribleRewards.add(Items.trash);

        badRewards.add(Items.pickaxeI);
        badRewards.add(Items.cardPackBasic);

        normalRewards.add(Items.cardPackNormal);

        goodRewards.add(Items.pickaxeII);
        goodRewards.add(Items.cardPackNormal);
        goodRewards.add(Items.redGem);
        goodRewards.add(Items.greenGem);
        goodRewards.add(Items.toolBox);

        greatRewards.add(Items.pickaxeIII);
        greatRewards.add(Items.cardPackGood);
        greatRewards.add(Items.purpleGem);
        greatRewards.add(Items.blueGem);

        amazingRewards.add(Items.rainbowGem);
        amazingRewards.add(Items.blackGem);
        amazingRewards.add(Items.gemdiril);
        amazingRewards.add(Items.cardPackVoid);

        leagueRewards.addAll(Items.leagueItems);
    }
}
