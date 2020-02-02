package cz.tefek.botdiril.userdata.item;

public class ItemPickaxe extends Item
{
    private double multiplier = 1;
    private long pickaxeWorth;
    private double chanceToBreak;

    public ItemPickaxe(String name, String icon, String localizedName, double multiplier, long pickaxeWorth, double chanceToBreak)
    {
        super(name, icon, localizedName);
        this.multiplier = multiplier;
        this.pickaxeWorth = pickaxeWorth;
        this.chanceToBreak = chanceToBreak;
    }

    public double getChanceToBreak()
    {
        return this.chanceToBreak;
    }

    public double getRareDropMultiplier()
    {
        return this.multiplier;
    }

    public long getPickaxeValue()
    {
        return this.pickaxeWorth;
    }
}
