package cz.tefek.botdiril.userdata.xp;

public class LevelData
{
    private long xp;
    private long cumulativeXP;
    private long dailyMin;
    private long dailyMax;
    private double gambleFalloff;
    private String loot;
    private int drawPotency;

    public LevelData(long xp, long cumulativeXp, long dailyMin, long dailyMax, double gambleFalloff, String loot, int drawPotency)
    {
        this.xp = xp;
        this.cumulativeXP = cumulativeXp;
        this.dailyMin = dailyMin;
        this.dailyMax = dailyMax;
        this.gambleFalloff = gambleFalloff;
        this.loot = loot;
        this.drawPotency = drawPotency;
    }

    public long getXP()
    {
        return xp;
    }

    public long getCumulativeXP()
    {
        return cumulativeXP;
    }

    public long getDailyMin()
    {
        return dailyMin;
    }

    public long getDailyMax()
    {
        return dailyMax;
    }

    public double getGambleFalloff()
    {
        return gambleFalloff;
    }

    public String getLoot()
    {
        return loot;
    }

    public int getDrawPotency()
    {
        return drawPotency;
    }
}
