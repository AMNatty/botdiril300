package cz.tefek.botdiril.userdata.pools;

import java.util.List;

import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.Items;
import cz.tefek.botdiril.userdata.items.pickaxe.Pickaxes;

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

    public static final LootPool<Item> staticRewards = new LootPool<>();

    public static final PoolDrawer basicCrate = new PoolDrawer().add(10, terribleRewards).add(50, badRewards).add(8, normalRewards).add(1, goodRewards).add(2, staticRewards).add(20, leagueRewards);

    public static final PoolDrawer uncommonCrate = new PoolDrawer().add(5, terribleRewards).add(60, badRewards).add(36, normalRewards).add(4, goodRewards).add(1, greatRewards).add(2, staticRewards);

    public static final PoolDrawer epicCrate = new PoolDrawer().add(40, terribleRewards).add(52, normalRewards).add(16, goodRewards).add(2, greatRewards).add(3, staticRewards);

    public static final PoolDrawer legendaryCrate = new PoolDrawer().add(120, terribleRewards).add(60, normalRewards).add(100, goodRewards).add(25, greatRewards).add(1, amazingRewards).add(7, staticRewards);

    public static final PoolDrawer ultimateCrate = new PoolDrawer().add(100, terribleRewards).add(80, goodRewards).add(20, greatRewards).add(4, amazingRewards).add(1, hyperRewards).add(8, staticRewards);

    public static final PoolDrawer hyperCrate = new PoolDrawer().add(800, greatRewards).add(400, amazingRewards).add(60, hyperRewards).add(10, ultraRewards).add(1, godlyRewards).add(20, staticRewards);

    public static final PoolDrawer infernalCrate = new PoolDrawer().add(300, greatRewards).add(400, amazingRewards).add(80, hyperRewards).add(20, ultraRewards).add(1, godlyRewards);

    static
    {
        terribleRewards.add(Items.trash);

        badRewards.add(Pickaxes.pickaxeI);
        badRewards.add(Items.cardPackBasic);
        badRewards.add(Items.uranium);

        normalRewards.add(Items.cardPackNormal);
        normalRewards.add(Items.scrollOfLesserIntelligence);
        normalRewards.add(Pickaxes.pickaxeII);

        goodRewards.add(Items.redGem);
        goodRewards.add(Items.greenGem);
        goodRewards.add(Items.toolBox);
        goodRewards.add(Items.scrollOfCombining);
        goodRewards.add(Pickaxes.pickaxeIV);

        greatRewards.add(Items.cardPackGood);
        greatRewards.add(Items.purpleGem);
        greatRewards.add(Items.blueGem);
        greatRewards.add(Items.emerald);
        greatRewards.add(Pickaxes.pickaxeV);

        amazingRewards.add(Items.scrollOfAbundance);
        amazingRewards.add(Items.cardPackVoid);
        amazingRewards.add(Items.scrollOfIntelligence);
        amazingRewards.add(Items.diamond);
        amazingRewards.add(Pickaxes.pickaxeX);
        amazingRewards.add(Items.rainbowGem);
        amazingRewards.add(Items.blackGem);

        hyperRewards.add(Pickaxes.pickaxeXV);

        ultraRewards.add(Items.scrollOfIntelligenceII);

        godlyRewards.add(Pickaxes.pickaxeXX);
        godlyRewards.add(Items.gemdiril);

        leagueRewards.addAll(Items.leagueItems);

        staticRewards.addAll(List.of(Items.keys, Items.keys, Items.goldenOil));
    }
}
