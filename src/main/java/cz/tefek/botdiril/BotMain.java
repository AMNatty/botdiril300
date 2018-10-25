package cz.tefek.botdiril;

import java.util.Locale;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.tefek.botdiril.framework.command.CommandIntitializer;
import cz.tefek.botdiril.framework.sql.SqlCon;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.framework.util.BigNumbers;
import cz.tefek.botdiril.internal.BotdirilConfig;
import cz.tefek.botdiril.serverdata.RolePreferences;
import cz.tefek.botdiril.serverdata.ServerPreferences;
import cz.tefek.botdiril.userdata.ItemLookup;
import cz.tefek.botdiril.userdata.card.Cards;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPRewards;

public class BotMain
{
    public static BotdirilConfig config;
    public static Botdiril botdiril;
    public static SqlCon sql;
    public static Logger logger;

    public static void main(String[] args)
    {
        logger = Logger.getLogger("Botdiril Global Log");
        logger.setLevel(Level.DEBUG);

        logger.info("=====================================");
        logger.info("#####           BOTDIRIL");
        logger.info("=====================================");

        Locale.setDefault(Locale.US);

        config = BotdirilConfig.load();

        if (config == null)
        {
            logger.fatal("Error while loading config. Aborting.");
            System.exit(1);
        }

        try
        {
            SqlFoundation.build();
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An exception has occuring while building the SQL interface.", e);
            System.exit(2);
        }

        BigNumbers.load();

        ItemLookup.prepare();

        Items.load();

        Cards.load();

        Timers.load();

        XPRewards.populate();

        try
        {
            CommandIntitializer.load();
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An error has occured while loading commands.", e);
            System.exit(4);
        }

        try
        {
            ServerPreferences.load();
            RolePreferences.load();
            botdiril = new Botdiril();
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An error has occured while loading server data or setting up the bot.", e);
            System.exit(3);
        }
    }
}
