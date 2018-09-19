package cz.tefek.botdiril;

import java.util.Timer;
import java.util.TimerTask;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.CommandParser;
import cz.tefek.botdiril.framework.util.BH;
import cz.tefek.botdiril.serverdata.ServerPreferences;
import cz.tefek.botdiril.userdata.UserInventory;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EventBus extends ListenerAdapter
{
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        var user = event.getAuthor();
        var message = event.getMessage();
        var jda = event.getJDA();
        var botUser = jda.getSelfUser();
        var guild = event.getGuild();
        var textChannel = event.getChannel();

        if (!user.isBot())
        {
            var co = new CallObj();
            co.caller = user;
            co.callerMember = BH.member(guild, user);
            co.guild = guild;
            co.message = message;
            co.sc = ServerPreferences.getConfigByGuild(co.guild.getIdLong());
            co.contents = message.getContentRaw();
            co.textChannel = textChannel;
            co.jda = jda;
            co.sc = ServerPreferences.getConfigByGuild(co.guild.getIdLong());
            co.bot = botUser;

            if (BH.findPrefix(guild, co))
            {
                co.ui = new UserInventory(co.caller.getIdLong());

                CommandParser.parse(co);
            }
        }
    }

    private static final String PLAYING = "www.tefek.cz";

    @Override
    public void onReady(ReadyEvent event)
    {
        event.getJDA().getPresence().setGame(Game.listening(PLAYING));

        event.getJDA().getGuilds().forEach(g ->
        {
            var sc = ServerPreferences.getConfigByGuild(g.getIdLong());
            if (sc == null)
            {
                ServerPreferences.addGuild(g);
            }
        });
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        var g = event.getGuild();

        BotMain.logger.info("Joining guild " + g);

        var sc = ServerPreferences.getConfigByGuild(g.getIdLong());
        if (sc == null)
        {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run()
                {
                    ServerPreferences.addGuild(g);
                }
            }, 5000);
        }
    }
}
