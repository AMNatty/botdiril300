package cz.tefek.botdiril.userdata.items.pickaxe;

import com.google.gson.annotations.SerializedName;

public class PickaxeMetadata
{
    @SerializedName("roman_numeral")
    private String romanNumeral;

    @SerializedName("tier")
    private int tier;

    @SerializedName("mini_tier")
    private int miniTier;

    @SerializedName("budget")
    private long budget;

    @SerializedName("break_chance")
    private double breakChance;

    @SerializedName("multiplier")
    private double multiplier;

    public double getBreakChance()
    {
        return this.breakChance;
    }

    public long getBudget()
    {
        return this.budget;
    }

    public int getMiniTier()
    {
        return this.miniTier;
    }

    public String getRomanNumeral()
    {
        return this.romanNumeral;
    }

    public int getTier()
    {
        return this.tier;
    }

    public double getMultiplier()
    {
        return this.multiplier;
    }
}
