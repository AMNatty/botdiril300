package cz.tefek.botdiril;

import java.util.Locale;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import cz.tefek.botdiril.framework.command.CommandIntitializer;
import cz.tefek.botdiril.framework.sql.SqlCon;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.framework.util.BigNumbers;
import cz.tefek.botdiril.internal.BotdirilConfig;
import cz.tefek.botdiril.internal.GlobalProperties;
import cz.tefek.botdiril.serverdata.ChannelPreferences;
import cz.tefek.botdiril.serverdata.RolePreferences;
import cz.tefek.botdiril.serverdata.ServerPreferences;
import cz.tefek.botdiril.userdata.ItemLookup;
import cz.tefek.botdiril.userdata.achievement.Achievements;
import cz.tefek.botdiril.userdata.card.Cards;
import cz.tefek.botdiril.userdata.item.Items;
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
            if (SystemUtils.IS_OS_WINDOWS)
            {
                System.load(new File("assets/native/libbotdiril-uss.dll").getAbsolutePath());
            }
            else if (SystemUtils.IS_OS_LINUX)
            {
                System.load(new File("assets/native/libbotdiril-uss.so").getAbsolutePath());
            }
            else
            {
                BotMain.logger.fatal("This OS does not seem to be supported.");
                System.exit(101);
            }
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An error has occured while loading native libraries for property managager.", e);
            System.exit(100);
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

        try
        {
            BigNumbers.load();

            ItemLookup.prepare();

            Items.load();

            Cards.load();

            Timers.load();

            Achievements.load();

            XPRewards.populate();
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An error has occured while loading user features.", e);
            System.exit(12);
        }

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
            GlobalProperties.load();
            ChannelPreferences.load();
            botdiril = new Botdiril();
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("An error has occured while loading server data or setting up the bot.", e);
            System.exit(3);
        }
    }
}
