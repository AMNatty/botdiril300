package cz.tefek.botdiril.userdata.card;

public enum EnumCardModifiers
{
    UNRANKED(1, "unranked", "Unranked", 1, 1000),
    CARDBOARD(2, "cardboard", "Cardboard", 1.1, 2000),
    WOODEN(3, "wooden", "Wooden", 1.2, 4000),
    BRONZE(4, "bronze", "Bronze", 1.3, 8000),
    SILVER(5, "silver", "Silver", 1.4, 16000),
    GOLD(6, "gold", "Gold", 1.48, 32000),
    PLATINUM(7, "platinum", "Platinum", 1.55, 64000),
    DIAMOND(8, "diamond", "Diamond", 1.61, 128000),
    MASTER(9, "master", "Master", 1.66, 256000),
    CHALLENGER(10, "challenger", "Challenger", 1.7, 1024000),
    CHALLENGER_PLUS(11, "challengerplus", "Challenger+", 1.7, 2048000),
    ASCENDED(12, "ascended", "Ascended", 1.75, 4096000),
    ASCENDED_PLUS(13, "ascendedplus", "Ascended+", 1.75, 0);

    private int level;
    private String name;
    private String localizedName;
    private double skillMod;
    private long cumulativeXP;

    private EnumCardModifiers(int level, String name, String localizedName, double skillMod, long cumulativeXP)
    {
        this.level = level;
        this.name = name;
        this.localizedName = localizedName;
        this.skillMod = skillMod;
        this.cumulativeXP = cumulativeXP;
    }

    public int getLevel()
    {
        return level;
    }

    public String getLocalizedName()
    {
        return localizedName;
    }

    public String getName()
    {
        return name;
    }

    public double getSkillMod()
    {
        return skillMod;
    }

    public long getCumulativeXP()
    {
        return cumulativeXP;
    }

    public static EnumCardModifiers getByLevel(int level)
    {
        var vs = values();

        for (int i = 0; i < vs.length; i++)
        {
            if (vs[i].level == level)
                return vs[i];
        }

        return null;
    }
}
