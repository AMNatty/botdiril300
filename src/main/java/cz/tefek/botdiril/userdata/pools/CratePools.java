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

    public static final PoolDrawer basicCrate = new PoolDrawer().add(10, terribleRewards).add(50, badRewards).add(60, leagueRewards).add(8, normalRewards).add(1, goodRewards);

    public static final PoolDrawer uncommonCrate = new PoolDrawer().add(5, terribleRewards).add(60, badRewards).add(30, leagueRewards).add(36, normalRewards).add(4, goodRewards).add(1, greatRewards);

    public static final PoolDrawer epicCrate = new PoolDrawer().add(5, terribleRewards).add(30, badRewards).add(36, normalRewards).add(4, goodRewards).add(1, greatRewards);

    public static final PoolDrawer legendaryCrate = new PoolDrawer().add(120, terribleRewards).add(360, normalRewards).add(80, goodRewards).add(30, greatRewards).add(1, amazingRewards);

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
        goodRewards.add(Items.scrollOfCombining);

        greatRewards.add(Items.cardPackGood);
        greatRewards.add(Items.purpleGem);
        greatRewards.add(Items.blueGem);

        amazingRewards.add(Items.pickaxeIII);
        amazingRewards.add(Items.rainbowGem);
        amazingRewards.add(Items.blackGem);
        amazingRewards.add(Items.cardPackVoid);
        amazingRewards.add(Items.scrollOfIntelligence);

        hyperRewards.add(Items.pickaxeIV);

        ultraRewards.add(Items.pickaxeV);
        ultraRewards.add(Items.gemdiril);
        ultraRewards.add(Items.scrollOfIntelligenceII);
        ultraRewards.add(Items.scrollOfAbundance);

        leagueRewards.addAll(Items.leagueItems);
    }
}
