package cz.tefek.botdiril.serverdata;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class ChannelPreferences
{
    static final String TABLE_CHANNELPROPERTIES = "channelproperties";

    public static final int BIT_DISABLED = 1;

    public static void clearBit(long channel, int bit)
    {
        var data = BotMain.sql.executeValue("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, channel);

        if (data == null)
        {
            data = 0;
        }

        data = data & ~(1 << bit - 1);

        BotMain.sql.exec("INSERT INTO " + TABLE_CHANNELPROPERTIES + "(cp_channel, cp_data) VALUES (?, ?) ON DUPLICATE KEY UPDATE cp_data=?", stat ->
        {
            return stat.executeUpdate();
        }, channel, data, data);
    }

    public static boolean checkBit(long channel, int bit)
    {
        var val = BotMain.sql.executeValue("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, channel);

        if (val == null)
        {
            return false;
        }

        return (val & 1 << bit - 1) > 0;
    }

    public static void load()
    {
        var tabExists = SqlFoundation.checkTableExists(BotMain.sql, TABLE_CHANNELPROPERTIES);

        if (!tabExists)
        {
            BotMain.sql.exec("CREATE TABLE " + TABLE_CHANNELPROPERTIES + " (cp_channel BIGINT PRIMARY KEY, cp_data INT NOT NULL);", stat ->
            {
                return stat.execute();
            });
        }
    }

    public static void setBit(long channel, int bit)
    {
        var data = BotMain.sql.executeValue("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, channel);

        if (data == null)
        {
            data = 0;
        }

        data = data | 1 << bit - 1;

        BotMain.sql.exec("INSERT INTO " + TABLE_CHANNELPROPERTIES + "(cp_channel, cp_data) VALUES (?, ?) ON DUPLICATE KEY UPDATE cp_data=?", stat ->
        {
            return stat.executeUpdate();
        }, channel, data, data);
    }
}
