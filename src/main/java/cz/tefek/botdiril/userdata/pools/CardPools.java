package cz.tefek.botdiril.userdata.pools;

import cz.tefek.botdiril.userdata.card.Card;

public class CardPools
{
    public static final LootPool<Card> basic = new LootPool<>();
    public static final LootPool<Card> common = new LootPool<>();
    public static final LootPool<Card> rare = new LootPool<>();
    public static final LootPool<Card> legacy = new LootPool<>();
    public static final LootPool<Card> legendary = new LootPool<>();
    public static final LootPool<Card> legacylegendary = new LootPool<>();
    public static final LootPool<Card> ultimate = new LootPool<>();
    public static final LootPool<Card> limited = new LootPool<>();
    public static final LootPool<Card> mythical = new LootPool<>();
    public static final LootPool<Card> unique = new LootPool<>();

    public static final PoolDrawer basicOrCommon = new PoolDrawer()
            .add(5, basic)
            .add(1, common);

    public static final PoolDrawer commonToLimited = new PoolDrawer()
            .add(532, basic)
            .add(216, common)
            .add(108, rare)
            .add(54, legacy)
            .add(27, legendary)
            .add(9, legacylegendary)
            .add(3, ultimate)
            .add(1, limited);

    public static final PoolDrawer rareOrBetter = new PoolDrawer()
            .add(1024, rare)
            .add(512, legacy)
            .add(256, legendary)
            .add(128, legacylegendary)
            .add(64, ultimate)
            .add(16, limited)
            .add(4, mythical)
            .add(1, unique);
}
