package cz.tefek.botdiril.userdata.timers;

import java.util.concurrent.TimeUnit;

public class Timers
{
    public static Timer daily;
    public static Timer mine;
    public static Timer draw;
    public static Timer farm;
    public static Timer steal;
    public static Timer nicknamechange;

    public static void load()
    {
        daily = new Timer("daily", TimeUnit.HOURS.toMillis(22));
        mine = new Timer("mine", TimeUnit.MINUTES.toMillis(8));
        draw = new Timer("draw", TimeUnit.MINUTES.toMillis(6));
        farm = new Timer("farm", TimeUnit.MINUTES.toMillis(20));
        steal = new Timer("steal", TimeUnit.HOURS.toMillis(1));
        nicknamechange = new Timer("nicknamechange", TimeUnit.DAYS.toMillis(30));
    }
}