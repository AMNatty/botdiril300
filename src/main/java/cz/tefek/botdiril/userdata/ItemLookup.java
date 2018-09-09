package cz.tefek.botdiril.userdata;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

/**
 * Don't get confused by the name, it's for legacy reasons.
 * This class can map any {@link IIdentifiable}.
 */
public class ItemLookup
{
    public static final String TABLE_ITEMLOOKUP = "itemlookup";

    private static final int AVAILABLE_IDS = 2 << 16;
    private static final BiMap<String, Integer> mappings = HashBiMap.create();
    private static final String[] iarr = new String[AVAILABLE_IDS];

    public static void prepare()
    {
        BotMain.logger.info("Initializing item lookup.");

        var tabExistsIL = SqlFoundation.checkTableExists(BotMain.sql, TABLE_ITEMLOOKUP);

        if (!tabExistsIL)
        {
            BotMain.sql.exec("CREATE TABLE " + TABLE_ITEMLOOKUP + " ( il_id INT UNIQUE NOT NULL, il_itemname VARCHAR(64) NOT NULL);", stat -> {
                return stat.execute();
            });
        }
        else
        {
            BotMain.sql.exec("SELECT * FROM " + TABLE_ITEMLOOKUP, stat -> {
                var rs = stat.executeQuery();

                while (rs.next())
                {
                    var itemname = rs.getString("il_itemname");
                    var itemid = rs.getInt("il_id");

                    mappings.put(itemname, itemid);
                    iarr[itemid] = itemname;
                }

                rs.close();

                return null;
            });
        }
    }

    public static String getName(int id)
    {
        return iarr[id];
    }

    public static void make(IIdentifiable thing)
    {
        thing.setID(make(thing.getName()));
    }

    public static int make(String name)
    {
        if (mappings.containsKey(name))
        {
            return mappings.get(name);
        }

        for (int i = 0; i < iarr.length; i++)
        {
            if (iarr[i] == null)
            {
                iarr[i] = name;
                mappings.put(name, i);

                BotMain.sql.exec("INSERT INTO " + TABLE_ITEMLOOKUP + " (il_id, il_itemname) VALUES (?, ?)", stat -> {
                    return stat.executeUpdate();
                }, i, name);

                return i;
            }
        }

        throw new IllegalStateException("ItemLookup: I think we are out of space for IDs.");
    }

    public static int get(IIdentifiable thing)
    {
        return get(thing.getName());
    }

    public static int get(String name)
    {
        if (name == null)
            throw new IllegalStateException("ItemLookup: Name is null!");

        try
        {
            return mappings.get("name");
        }
        catch (NullPointerException e)
        {
            BotMain.logger.warn("Looks like the item wasn't in the lookup: " + name, e);
            return make(name);
        }
    }
}
