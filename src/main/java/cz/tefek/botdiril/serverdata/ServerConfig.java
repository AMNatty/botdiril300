package cz.tefek.botdiril.serverdata;

import java.sql.Types;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.ParamNull;

public class ServerConfig
{
    private long loggingChannel;
    private long guild;
    private String prefix;

    public ServerConfig(long guild)
    {
        this.guild = guild;
    }

    public long getGuild()
    {
        return guild;
    }

    public long getLoggingChannel()
    {
        return loggingChannel;
    }

    void loadLoggingChannel(long loggingChannel)
    {
        this.loggingChannel = loggingChannel;
    }

    public void setLoggingChannel(long loggingChannel)
    {
        this.loggingChannel = loggingChannel;

        BotMain.sql.exec("UPDATE " + ServerPreferences.PREF_TABLE + " SET sc_logchannel = ? WHERE sc_guild = ?", stat ->
        {
            return stat.executeUpdate();
        }, this.loggingChannel, this.guild);
    }

    public void loadPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;

        BotMain.sql.exec("UPDATE " + ServerPreferences.PREF_TABLE + " SET sc_prefix = ? WHERE sc_guild = ?", stat ->
        {
            return stat.executeUpdate();
        }, this.prefix == null ? new ParamNull(Types.VARCHAR) : this.prefix, this.guild);
    }

    public String getPrefix()
    {
        return prefix == null ? "botdiril." : prefix;
    }
}
