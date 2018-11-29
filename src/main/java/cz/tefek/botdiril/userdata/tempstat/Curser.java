package cz.tefek.botdiril.userdata.tempstat;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.timers.MiniTime;

public class Curser
{
    public static void bless(CallObj co)
    {
        var blessings = EnumBlessing.values();
        var blessing = blessings[Botdiril.RANDOM.nextInt(blessings.length)];

        var millis = blessing.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isBlessed(co, blessing))
        {
            co.po.addLong(blessing.getName(), millis);
        }
        else
        {
            co.po.setLong(blessing.getName(), System.currentTimeMillis() + millis);
        }

        MR.send(co.textChannel, String.format("You've been **blessed** with the **%s** for **%s**. **%s**", blessing.getLocalizedName(), strTime, blessing.getDescription()));
    }

    public static void curse(CallObj co)
    {
        var curses = EnumCurse.values();
        var curse = curses[Botdiril.RANDOM.nextInt(curses.length)];

        if (isBlessed(co, EnumBlessing.CANT_BE_CURSED))
        {
            MR.send(co.textChannel, String.format("***Your %s protected your from the %s.***", EnumBlessing.CANT_BE_CURSED.getLocalizedName(), curse.getLocalizedName()));
            return;
        }

        var millis = curse.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isCursed(co, curse))
        {
            co.po.addLong(curse.getName(), millis);
        }
        else
        {
            co.po.setLong(curse.getName(), System.currentTimeMillis() + millis);
        }

        MR.send(co.textChannel, String.format("You've been **cursed** with the **%s** for **%s**. **%s**", curse.getLocalizedName(), strTime, curse.getDescription()));
    }

    public static boolean isBlessed(CallObj co, EnumBlessing blessing)
    {
        return isBlessed(co.po, blessing);
    }

    public static boolean isBlessed(PropertyObject po, EnumBlessing blessing)
    {
        return po.getLongOrDefault(blessing.getName(), 0) > System.currentTimeMillis();
    }

    public static boolean isCursed(CallObj co, EnumCurse curse)
    {
        return isCursed(co.po, curse);
    }

    public static boolean isCursed(PropertyObject po, EnumCurse curse)
    {
        return po.getLongOrDefault(curse.getName(), 0) > System.currentTimeMillis();
    }
}
