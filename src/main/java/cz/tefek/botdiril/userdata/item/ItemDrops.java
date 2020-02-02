package cz.tefek.botdiril.userdata.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Iterable storage for item loot.
 * 
 */
public class ItemDrops implements Iterable<ItemPair>
{
    private Map<Item, Long> lootMap = new HashMap<>();

    public void addItem(Item item)
    {
        this.addItem(item, 1);
    }

    public void addItem(Item item, long amount)
    {
        if (amount == 0)
        {
            return;
        }

        var cVal = this.lootMap.get(item);

        if (cVal != null)
        {
            this.lootMap.put(item, cVal + amount);
        }
        else
        {
            this.lootMap.put(item, amount);
        }
    }

    public int distintCount()
    {
        return this.lootMap.size();
    }

    public boolean hasItemDropped(Item item)
    {
        return this.lootMap.containsKey(item);
    }

    @Override
    public Iterator<ItemPair> iterator()
    {
        return this.lootMap.entrySet().stream().map(entry -> new ItemPair(entry.getKey(), entry.getValue())).iterator();
    }

    public long totalCount()
    {
        return this.lootMap.values().stream().collect(Collectors.reducing(Long::sum)).orElse(0L);
    }

    public Stream<ItemPair> stream()
    {
        return this.lootMap.entrySet().stream().map(entry -> new ItemPair(entry.getKey(), entry.getValue()));
    }
}
