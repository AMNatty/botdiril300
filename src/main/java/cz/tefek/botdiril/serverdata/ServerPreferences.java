package cz.tefek.botdiril.serverdata;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.internal.BotdirilConfig;

public class ServerPreferences
{
    static final String PREF_TABLE = "serverconfig";

    private static List<ServerConfig> cfgs;

    public static void load()
    {
        cfgs = new ArrayList<>();

        var tabExists = SqlFoundation.checkTableExists(BotMain.sql, PREF_TABLE);

        if (!tabExists)
        {
            BotMain.sql.exec("CREATE TABLE " + PREF_TABLE + " ( sc_guild BIGINT PRIMARY KEY , sc_logchannel BIGINT, sc_prefix VARCHAR(16) );", stat ->
            {
                return stat.execute();
            });

            return;
        }

        var result = BotMain.sql.exec("SELECT * FROM " + PREF_TABLE, stat ->
        {
            var rs = stat.executeQuery();

            while (rs.next())
            {
                var gid = rs.getLong("sc_guild");

                BotMain.logger.info("Loading guild " + gid);

                var config = new ServerConfig(gid);

                config.loadLoggingChannel(rs.getLong("sc_logchannel"));
                config.loadPrefix(rs.getString("sc_prefix"));

                cfgs.add(config);
            }

            return true;
        });

        if (result != true)
        {
            BotMain.logger.fatal("Error while loading config, aborting.");
            System.exit(4);
        }
    }

    public static ServerConfig getConfigByGuild(long id)
    {
        for (ServerConfig serverConfig : cfgs)
        {
            if (serverConfig.getGuild() == id)
                return serverConfig;
        }

        return null;
    }

    public static void addGuild(Guild g)
    {
        var gc = g.getController();
        var prem = g.getTextChannelsByName("botdiril", true);

        TextChannel pc;

        if (!prem.isEmpty())
        {
            pc = prem.get(0);
        }
        else
        {
            pc = (TextChannel) gc.createTextChannel("botdiril").complete();
        }

        var sc = new ServerConfig(g.getIdLong());
        sc.setLoggingChannel(pc.getIdLong());

        cfgs.add(sc);

        BotMain.sql.exec("INSERT INTO " + PREF_TABLE + "(sc_guild, sc_logchannel) VALUES (?, ?)", stat ->
        {
            return stat.executeUpdate();
        }, sc.getGuild(), sc.getLoggingChannel());

        pc.sendMessage("Hello! This is Botdiril300! Please set a prefix via `" + BotdirilConfig.UNIVERSAL_PREFIX + "prefix <prefix>`. You can change it later. To set a logging channel please use `" + BotdirilConfig.UNIVERSAL_PREFIX + "channel <log channel>`.").submit();
    }
}
