package cz.tefek.botdiril.userdata.items;

import cz.tefek.botdiril.userdata.card.Card;

public class CardPair
{
    private Card card;
    private long amount;
    private int level;
    private long xp;

    public CardPair(Card item)
    {
        this.card = item;
        this.amount = 1;
    }

    public CardPair(Card item, long amount)
    {
        this.card = item;
        this.amount = amount;
    }

    public void addAmount(long amt)
    {
        this.amount += amt;
    }

    public long getAmount()
    {
        return this.amount;
    }

    public Card getCard()
    {
        return this.card;
    }

    public int getLevel()
    {
        return this.level;
    }

    public long getXP()
    {
        return this.xp;
    }

    public void increment()
    {
        this.amount++;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setXP(long xp)
    {
        this.xp = xp;
    }
}
