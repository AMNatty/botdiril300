package cz.tefek.botdiril.framework.sql;

import java.beans.PropertyVetoException;
import java.sql.DriverManager;
import java.sql.SQLException;

import cz.tefek.botdiril.BotMain;

public class SqlFoundation
{
    public static final String SCHEMA = "botdiril2";

    public static void build()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (var c = DriverManager.getConnection("jdbc:mysql://" + BotMain.config.getSqlHost() + "/?useUnicode=true" + "&autoReconnect=true" + "&useJDBCCompliantTimezoneShift=true" + "&useLegacyDatetimeCode=false" + "&serverTimezone=UTC", BotMain.config.getSqlKey(), BotMain.config.getSqlPass()))
            {
                try (var stat = c.prepareStatement("SHOW DATABASES LIKE ?"))
                {
                    stat.setString(1, SCHEMA);

                    if (!stat.executeQuery().next())
                    {
                        BotMain.logger.info("Database needs to be reconstructed.");

                        c.prepareStatement("CREATE DATABASE " + SCHEMA).execute();
                    }

                    BotMain.sql = new SqlCon();
                }
            }
        }
        catch (SQLException | ClassNotFoundException | PropertyVetoException e)
        {
            BotMain.logger.error("An error has occured while preparing the database structure.", e);
        }
    }

    public static boolean checkTableExists(SqlCon con, String name)
    {
        return con.exec("SHOW TABLES LIKE ?", stat -> {
            return stat.executeQuery().next();
        }, name);
    }
}
