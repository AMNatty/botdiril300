package cz.tefek.botdiril.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;

import cz.tefek.botdiril.BotMain;

public class BotdirilConfig
{
    private String apiKey;
    private String sqlHost;
    private String sqlKey;
    private String sqlPass;

    private final List<Long> superusers = new ArrayList<>();

    public static final String UNIVERSAL_PREFIX = "botdiril.";

    public static BotdirilConfig load()
    {
        var cfgFile = new File("settings.json");

        if (!cfgFile.exists())
        {
            BotMain.logger.fatal("ERROR: Could not find " + cfgFile.getName() + ", aborting.");
            return null;
        }

        try
        {
            var bc = new BotdirilConfig();

            var cfgPlainText = Files.readAllLines(cfgFile.toPath()).stream().collect(Collectors.joining()).trim();

            var jo = new JSONObject(cfgPlainText);

            bc.apiKey = jo.getString("key");

            bc.sqlHost = jo.getString("mysql_host");
            bc.sqlKey = jo.getString("mysql_user");
            bc.sqlPass = jo.getString("mysql_pass");

            var sus = jo.optJSONArray("superusers_override");

            if (sus != null)
            {
                for (int i = 0; i < sus.length(); i++)
                {
                    bc.superusers.add(sus.getLong(i));
                }
            }

            return bc;
        } catch (Exception e)
        {
            BotMain.logger.fatal("ERROR: An exception has occured and the initialization cannot continue.", e);
            return null;
        }
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getSqlHost()
    {
        return sqlHost;
    }

    public String getSqlKey()
    {
        return sqlKey;
    }

    public String getSqlPass()
    {
        return sqlPass;
    }

    public List<Long> getSuperuserOverrideIDs()
    {
        return superusers;
    }
}
