package cz.tefek.botdiril.userdata.pools;

import java.util.ArrayList;

import cz.tefek.botdiril.Botdiril;

public class LootPool<E> extends ArrayList<E>
{
    /**
     * 
     */
    private static final long serialVersionUID = -3152306088515769549L;

    public E draw()
    {
        return this.get(Botdiril.RANDOM.nextInt(this.size()));
    }
}
