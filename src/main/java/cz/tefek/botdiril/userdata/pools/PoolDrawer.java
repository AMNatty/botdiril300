package cz.tefek.botdiril.userdata.pools;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.Botdiril;

public class PoolDrawer
{
    private List<PoolWrapper> pools = new ArrayList<>();
    private long weightSum = 0;

    public PoolDrawer add(long weight, LootPool<?> pool)
    {
        this.pools.add(new PoolWrapper(weight, pool));
        this.weightSum += weight;

        return this;
    }

    public LootPool<?> draw()
    {
        var rd = Botdiril.RDG.nextLong(0, this.weightSum);
        var ptr = 0;

        for (var pool : this.pools)
        {
            if (ptr >= rd)
            {
                return pool.getPool();
            }

            ptr += pool.getWeight();
        }

        return null;
    }
}
