package cz.tefek.botdiril.userdata.timers;

import cz.tefek.botdiril.userdata.ItemLookup;

public class Timer
{
    private int id;
    private String name;
    private long timeMs;

    public Timer(String name, long timeMs)
    {
        this.name = "timer_" + name;
        this.timeMs = timeMs;
        this.id = ItemLookup.make(this.name);
    }

    public String getName()
    {
        return name;
    }

    public long getTimeOffset()
    {
        return timeMs;
    }

    public int getID()
    {
        return this.id;
    }
}
