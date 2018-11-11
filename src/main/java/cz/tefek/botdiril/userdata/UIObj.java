package cz.tefek.botdiril.userdata;

import java.math.BigInteger;

public class UIObj
{
    public int level;
    public long xp;
    public long coins;
    public long keks;
    public long dust;
    public BigInteger megakeks;
    public long keys;
    public long tokens;
    public long cards;

    public UIObj(int level, long xp, long coins, long keks, long dust, BigInteger megakeks, long keys, long tokens, long cards)
    {
        this.level = level;
        this.xp = xp;
        this.coins = coins;
        this.keks = keks;
        this.dust = dust;
        this.megakeks = megakeks;
        this.keys = keys;
        this.tokens = tokens;
        this.cards = cards;
    }
}
