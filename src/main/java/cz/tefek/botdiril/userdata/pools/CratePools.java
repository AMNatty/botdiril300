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
    public static final LootPool<Item> hyperRewards = new LootPool<>();
    public static final LootPool<Item> ultraRewards = new LootPool<>();
    public static final LootPool<Item> godlyRewards = new LootPool<>();

    public static final PoolDrawer basicCrate = new PoolDrawer().add(10, terribleRewards).add(50, badRewards).add(60, leagueRewards).add(8, normalRewards).add(1, goodRewards);

    public static final PoolDrawer uncommonCrate = new PoolDrawer().add(5, terribleRewards).add(60, badRewards).add(30, leagueRewards).add(36, normalRewards).add(4, goodRewards).add(1, greatRewards);

    public static final PoolDrawer epicCrate = new PoolDrawer().add(5, terribleRewards).add(30, badRewards).add(36, normalRewards).add(4, goodRewards).add(1, greatRewards);

    public static final PoolDrawer legendaryCrate = new PoolDrawer().add(120, terribleRewards).add(300, normalRewards).add(80, goodRewards).add(15, greatRewards).add(1, amazingRewards);

    public static final PoolDrawer ultimateCrate = new PoolDrawer().add(120, terribleRewards).add(80, goodRewards).add(20, greatRewards).add(3, amazingRewards).add(1, hyperRewards);

    public static final PoolDrawer hyperCrate = new PoolDrawer().add(800, greatRewards).add(400, amazingRewards).add(50, hyperRewards).add(2, godlyRewards);

    public static final PoolDrawer infernalCrate = new PoolDrawer().add(300, greatRewards).add(400, amazingRewards).add(80, hyperRewards).add(5, godlyRewards);

    public static final LootPool<Item> goldenCratePool = new LootPool<>();

    static
    {
        goldenCratePool.add(Items.keys);
        goldenCratePool.add(Items.crateGolden);
        goldenCratePool.add(Items.trash);

        terribleRewards.add(Items.trash);

        badRewards.add(Items.pickaxeI);
        badRewards.add(Items.cardPackBasic);
        badRewards.add(Items.uranium);

        normalRewards.add(Items.cardPackNormal);
        normalRewards.add(Items.platinum);

        goodRewards.add(Items.pickaxeII);
        goodRewards.add(Items.cardPackNormal);
        goodRewards.add(Items.redGem);
        goodRewards.add(Items.greenGem);
        goodRewards.add(Items.toolBox);
        goodRewards.add(Items.scrollOfCombining);

        greatRewards.add(Items.cardPackGood);
        greatRewards.add(Items.purpleGem);
        greatRewards.add(Items.blueGem);
        greatRewards.add(Items.pickaxeIII);

        amazingRewards.add(Items.rainbowGem);
        amazingRewards.add(Items.blackGem);
        amazingRewards.add(Items.cardPackVoid);
        amazingRewards.add(Items.scrollOfIntelligence);
        amazingRewards.add(Items.diamond);

        hyperRewards.add(Items.pickaxeIV);

        ultraRewards.add(Items.pickaxeV);
        ultraRewards.add(Items.gemdiril);

        godlyRewards.add(Items.scrollOfIntelligenceII);
        godlyRewards.add(Items.scrollOfAbundance);

        leagueRewards.addAll(Items.leagueItems);
    }
}
