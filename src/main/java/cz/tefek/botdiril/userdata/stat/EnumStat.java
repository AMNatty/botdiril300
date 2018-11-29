package cz.tefek.botdiril.userdata.stat;

public enum EnumStat
{
    COMMANDS_USED("stat_commandsused", "Commands used"),
    CRATES_OPENED("stat_cratesopened", "Crates opened"),
    CARD_PACKS_OPENED("stat_cardpacksopened", "Card packs opened"),
    TIMES_ROBBED("stat_robbed", "Times you were robbed"),
    TIMES_NUKED("stat_nuked", "Times you were nuked"),
    TIMES_MINED("stat_mined", "Times mined"),
    TIMES_FARMED("stat_farmed", "Times farmed"),
    TIMES_DAILY("stat_daily", "Times daily used"),
    TIMES_LOST_ALL_MEGAKEKS("stat_megakek0", "Times megakeks lost"),
    ITEMS_CRAFTED("stat_crafted", "Items crafted"),
    GIFTS_SENT("stat_giftssent", "Gifts sent"),
    BIGGEST_PAYOUT("stat_maxpayout", "Most keks paid out"),
    BIGGEST_STEAL("stat_maxsteal", "Most coins stolen"),
    BIGGEST_NUKE("stat_maxnuke", "Most keks nuked away"),
    PICKAXES_BROKEN("stat_pickaxesbroken", "Pickaxes broken");

    private String name;
    private String localizedName;

    private EnumStat(String name, String localizedName)
    {
        this.name = name;
        this.localizedName = localizedName;
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
