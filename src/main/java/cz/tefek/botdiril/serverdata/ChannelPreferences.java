package cz.tefek.botdiril.serverdata;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class ChannelPreferences
{
    static final String TABLE_CHANNELPROPERTIES = "channelproperties";

    public static final int BIT_DISABLED = 1;

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
}
