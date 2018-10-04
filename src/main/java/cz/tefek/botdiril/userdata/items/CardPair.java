package cz.tefek.botdiril.userdata.items;

import cz.tefek.botdiril.userdata.card.Card;

public class CardPair
{
    private Card card;
    private long amount;

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

    public void increment()
    {
        this.amount++;
    }
}
