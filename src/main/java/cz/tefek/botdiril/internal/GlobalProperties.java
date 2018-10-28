package cz.tefek.botdiril.internal;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class GlobalProperties
{
    static final String TABLE_GLOBALPROPERTIES = "globalproperties";

    public static final String JACKPOT = "jackpot";
    public static final String JACKPOT_RESET = "jackpot_reset";

    public static void load()
    {
        var tabExists = SqlFoundation.checkTableExists(BotMain.sql, TABLE_GLOBALPROPERTIES);

        if (!tabExists)
        {
            BotMain.sql.exec("CREATE TABLE " + TABLE_GLOBALPROPERTIES + " ( gp_key VARCHAR(64) PRIMARY KEY, gp_value BIGINT );", stat ->
            {
                return stat.execute();
            });
        }
    }

    public static void set(String key, long value)
    {
        BotMain.sql.exec("INSERT INTO " + TABLE_GLOBALPROPERTIES + "(gp_key, gp_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE gp_value=?;", stat ->
        {
            return stat.execute();
        }, key, value, value);
    }

    /**
     * WARNING: Returns 0 if there is no such row.
     */
    public static long get(String key)
    {
        return BotMain.sql.exec("SELECT gp_value FROM " + TABLE_GLOBALPROPERTIES + " WHERE gp_key=?", stat ->
        {
            var rs = stat.executeQuery();

            if (rs.next())
            {
                return rs.getLong("gp_value");
            }
            else
            {
                return 0L;
            }
        }, key);
    }

    public static void add(String key, long value)
    {
        BotMain.sql.exec("INSERT INTO " + TABLE_GLOBALPROPERTIES + "(gp_key, gp_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE gp_value=gp_value+?;", stat ->
        {
            return stat.execute();
        }, key, value, value);
    }

    public static void subtract(String key, long value)
    {
        add(key, -value);
    }
}
