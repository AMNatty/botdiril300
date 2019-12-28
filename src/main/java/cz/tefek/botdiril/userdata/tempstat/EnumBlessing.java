package cz.tefek.botdiril.userdata.tempstat;

public enum EnumBlessing
{
    UNBREAKABLE_PICKAXE("blessing_unbreakablepickaxe", "Unbreakable Pickaxe", "Your pickaxe cannot break.", 60 * 30), //
    MEGAKEK_LOSS_IMMUNITY("blessing_megakeks", "Blessed Megakeks", "The chance to lose megakeks is much lower.", 60 * 3), //
    CANT_BE_CURSED("blessing_curseimmunity", "Curse Immunity", "You cannot be cursed while this blessing is active.", 60 * 30), //
    BETTER_SELL_PRICES("blessing_bettersell", "Negotiation Skill", "You have much better sell values.", 60 * 5), //
    CHANCE_NOT_TO_CONSUME_KEY("blessing_skeletonkey", "Skeleton Key", "Chance not to consume a key on use.", 60 * 12), //
    STEAL_IMMUNE("blessing_stealimmunity", "Arcane Safe", "You cannot be robbed, under any circumstances.", 60 * 30), //
    MINE_SURGE("blessing_minesurge", "Mining Surge", "Mining has a chance not to go on cooldown.", 60 * 12), //
    PICKPOCKET("blessing_pickpocket100", "Pickpocket 100", "You have a chance to steal much more than you normally could.", 60 * 30), //
    CRAFTING_SURGE("blessing_craftingsurge", "Crafting Surge", "Chance to not consume ingredients while crafting.", 60 * 4), //
    NUKE_IMMUNE("blessing_nukeimmunity", "Tactical anti-ballistic missile system", "You cannot be nuked.", 60 * 60); //

    private String name;
    private String localizedName;
    private String description;
    private long durationInSeconds;

    private EnumBlessing(String name, String localizedName, String description, long durationInSeconds)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.description = description;
        this.durationInSeconds = durationInSeconds;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getDurationInSeconds()
    {
        return this.durationInSeconds;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    public String getName()
    {
        return this.name;
    }
}
