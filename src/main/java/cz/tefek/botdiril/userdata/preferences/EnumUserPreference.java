package cz.tefek.botdiril.userdata.preferences;

public enum EnumUserPreference
{
    REPAIR_KIT_DISABLED(1 << 0, "disablerepairkit", "Disable repair kits"),
    GOLDEN_OIL_DISABLED(1 << 1, "disablegoldenoil", "Disable golden oil");

    private long bit;
    private String id;
    private String localizedName;

    private EnumUserPreference(long bit, String id, String localizedName)
    {
        this.bit = bit;
        this.id = id;
        this.localizedName = localizedName;
    }

    public long getBit()
    {
        return this.bit;
    }

    public String getID()
    {
        return this.id;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    @Override
    public String toString()
    {
        return this.getID();
    }
}
