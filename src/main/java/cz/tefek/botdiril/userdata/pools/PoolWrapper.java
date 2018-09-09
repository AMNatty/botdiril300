package cz.tefek.botdiril.userdata.pools;

public class PoolWrapper
{
    private long weight;
    private LootPool<?> pool;

    public PoolWrapper(long weight, LootPool<?> pool)
    {
        this.weight = weight;
        this.pool = pool;
    }

    public long getWeight()
    {
        return weight;
    }

    public LootPool<?> getPool()
    {
        return pool;
    }
}
