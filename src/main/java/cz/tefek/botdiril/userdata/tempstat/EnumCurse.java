package cz.tefek.botdiril.userdata.tempstat;

public enum EnumCurse
{
    CURSE_OF_YASUO("curse_curseofyasuo", "Curse of Yasuo", "All cards drawn are now Yasuo.", 60 * 30), //
    DOUBLE_CHANCE_TO_LOSE_MEGA("curse_badmega", "Cursed Megakeks", "Double chance to lose everything.", 60 * 60), //
    DOUBLE_PICKAXE_BREAK_CHANCE("curse_badpickaxe", "Mining Fatigue", "Double chance to break your pickaxe while mining.", 60 * 45), //
    CANT_TAKE_DAILY("curse_nodaily", "Daily Lockout", "You can't take your daily loot.", 60 * 120), //
    HALVED_SELL_VALUE("curse_badsell", "Bad Negotiation", "You are really bad at selling goods.", 60 * 35), //
    CRAFTING_MAY_FAIL("curse_badcrafting", "Crafting Fatigue", "Crafting may fail, consuming all used items.", 60 * 25), //
    CANT_SEE_MINED_STUFF("curse_blidness", "Blindness", "You can't see what you mined.", 60 * 35), //
    CANT_WIN_JACKPOT("curse_nojackpot", "Rigged", "You can't win the jackpot.", 60 * 120), //
    MAGNETIC("curse_magnetic", "Magnetic", "There is a chance your gift ends up redirected to me.", 60 * 60), // Your gifts may end up redirected to the bot
    EASIER_TO_ROB("curse_easiertorob", "Loose Pockets", "You are an easier target to rob.", 60 * 30); //

    private String name;
    private String localizedName;
    private String description;
    private long durationInSeconds;

    private EnumCurse(String name, String localizedName, String description, long durationInSeconds)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.description = description;
        this.durationInSeconds = durationInSeconds;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getDurationInSeconds()
    {
        return this.durationInSeconds;
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
