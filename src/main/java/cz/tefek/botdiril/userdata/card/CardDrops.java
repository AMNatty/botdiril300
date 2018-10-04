package cz.tefek.botdiril.userdata.card;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.tefek.botdiril.userdata.items.CardPair;

/**
 * Iterable storage for card loot.
 * 
 */
public class CardDrops implements Iterable<CardPair>
{
    private Map<Card, Long> lootMap = new HashMap<>();

    public void addItem(Card card)
    {
        this.addItem(card, 1);
    }

    public void addItem(Card card, long amount)
    {
        var cVal = this.lootMap.get(card);

        if (cVal != null)
            this.lootMap.put(card, cVal + amount);
        else
            this.lootMap.put(card, amount);
    }

    public int distintCount()
    {
        return this.lootMap.size();
    }

    public boolean hasItemDropped(Card item)
    {
        return this.lootMap.containsKey(item);
    }

    @Override
    public Iterator<CardPair> iterator()
    {
        return this.lootMap.entrySet().stream().map(entry -> new CardPair(entry.getKey(), entry.getValue())).iterator();
    }

    public long totalCount()
    {
        return this.lootMap.values().stream().collect(Collectors.reducing(Long::sum)).orElse(0L);
    }

    public Stream<CardPair> stream()
    {
        return this.lootMap.entrySet().stream().map(entry -> new CardPair(entry.getKey(), entry.getValue()));
    }
}
