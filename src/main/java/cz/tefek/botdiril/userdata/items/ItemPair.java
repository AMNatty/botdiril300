package cz.tefek.botdiril.userdata.items;

public class ItemPair
{
    private Item item;
    private long amount;

    public ItemPair(Item item, long amount)
    {
        this.item = item;
        this.amount = amount;
    }

    public ItemPair(Item item)
    {
        this.item = item;
        this.amount = 1;
    }

    public Item getItem()
    {
        return item;
    }

    public long getAmount()
    {
        return amount;
    }

    public void addAmount(long amt)
    {
        this.amount += amt;
    }

    public void increment()
    {
        this.amount++;
    }
}
