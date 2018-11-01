package cz.tefek.botdiril.userdata.card;

public class CardSet
{
    public static CardSet league;
    public static CardSet terraria;
    public static CardSet csgo;

    private String setName;
    private String setLocalizedName;
    private String setPrefix;

    private boolean drops;

    private String description;

    public CardSet(String collectionName, String collectionLocalizedName, String collectionPrefix)
    {
        this.setName = collectionName;
        this.setLocalizedName = collectionLocalizedName;
        this.setPrefix = collectionPrefix;
    }

    public boolean canDrop()
    {
        return this.drops;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getSetLocalizedName()
    {
        return this.setLocalizedName;
    }

    public String getSetName()
    {
        return this.setName;
    }

    public String getSetPrefix()
    {
        return this.setPrefix;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDrops(boolean drops)
    {
        this.drops = drops;
    }
}
