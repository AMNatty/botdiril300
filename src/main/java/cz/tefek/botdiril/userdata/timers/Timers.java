package cz.tefek.botdiril.userdata.timers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Timers
{
    public static Timer daily;
    public static Timer mine;
    public static Timer draw;
    public static Timer farm;
    public static Timer steal;
    public static Timer payout;
    public static Timer gambleXP;
    public static Timer nicknamechange;

    public static List<Timer> allTimers = new ArrayList<>();

    public static void load()
    {
        daily = new Timer("daily", "Daily", TimeUnit.HOURS.toMillis(22));
        mine = new Timer("mine", "Mine", TimeUnit.MINUTES.toMillis(2));
        draw = new Timer("draw", "Draw", TimeUnit.MINUTES.toMillis(6));
        farm = new Timer("farm", "Farm", TimeUnit.MINUTES.toMillis(9));
        steal = new Timer("steal", "Steal / Nuke", TimeUnit.HOURS.toMillis(1));
        payout = new Timer("payout", "Payout", TimeUnit.MINUTES.toMillis(1));
        gambleXP = new Timer("gamblexp", "Gambling XP", TimeUnit.SECONDS.toMillis(50));
        nicknamechange = new Timer("nicknamechange", "Nickname Change", TimeUnit.DAYS.toMillis(30));
    }
}
