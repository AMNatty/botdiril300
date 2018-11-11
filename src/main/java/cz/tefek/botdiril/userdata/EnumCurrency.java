package cz.tefek.botdiril.userdata;

import cz.tefek.botdiril.userdata.items.Icons;

public enum EnumCurrency
{
    XP("xp", Icons.ACHIEVEMENT_BOT, "Experience", "The main currency of Botdiril! Collect experience to unlock more items, loot and commands."),
    COINS("coins", Icons.COIN, "Coins", "Used mostly to buy goods and exchange for other currencies."),
    KEKS("keks", Icons.KEK, "Keks", "The main gambling currency, can be exchanged for Kek Tokens."),
    TOKENS("kektokens", Icons.TOKEN, "Kek Tokens", "Allow you to get some sweet loot for your gambling activities."),
    MEGAKEKS("megakeks", Icons.MEGAKEK, "MegaKeks", "Keks but on steroids. Reach one googol MegaKeks in no time!"),
    DUST("dust", Icons.DUST, "Dust", "Used mostly for crafting, Dust allows you to recycle duplicate items for new goods."),
    KEYS("keys", Icons.KEY, "Keys", "Unlock even more loot paths.");

    private String localizedName;
    private String desc;
    private String name;
    private String icon;

    EnumCurrency(String name, String icon, String localizedName, String description)
    {
        this.localizedName = localizedName;
        this.icon = icon;
        this.desc = description;
        this.name = name;
    }

    public String getLocalizedName()
    {
        return localizedName;
    }

    public String getDescription()
    {
        return desc;
    }

    public String getName()
    {
        return name;
    }

    public String getIcon()
    {
        return icon;
    }
}
