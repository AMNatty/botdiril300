package cz.tefek.botdiril.userdata.mechanic.mine;

public enum EnumMineMultiplier
{
    MLT_RANDOM("random"),
    MLT_EXPERIENCE("level"),
    MLT_KITLESS("no repair kit");

    private String localizedName;

    private EnumMineMultiplier(String localizedName)
    {
        this.localizedName = localizedName;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }
}
