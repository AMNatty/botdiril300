package cz.tefek.botdiril.userdata.card;

public class CardSet
{
    public static CardSet league;
    public static CardSet terraria;
    public static CardSet csgo;

    private String setName;
    private String setLocalizedName;
    private String setPrefix;

    private String description;

    public CardSet(String collectionName, String collectionLocalizedName, String collectionPrefix)
    {
        this.setName = collectionName;
        this.setLocalizedName = collectionLocalizedName;
        this.setPrefix = collectionPrefix;
    }

    public String getSetLocalizedName()
    {
        return setLocalizedName;
    }

    public String getSetName()
    {
        return setName;
    }

    public String getSetPrefix()
    {
        return setPrefix;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
