package cz.tefek.botdiril.userdata.items;

public class ItemPickaxe extends Item
{
    private int multiplier = 1;

    public ItemPickaxe(String name, String icon, String localizedName, int multiplier)
    {
        super(name, icon, localizedName);
        this.multiplier = multiplier;
    }

    public int getMultiplier()
    {
        return multiplier;
    }
}
