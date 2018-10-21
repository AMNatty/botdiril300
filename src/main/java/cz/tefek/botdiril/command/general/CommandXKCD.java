package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.core.EmbedBuilder;

import org.jsoup.Jsoup;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "xkcd", aliases = {}, category = CommandCategory.GENERAL, description = "Gets a random xkcd comic.")
public class CommandXKCD
{
    @CmdInvoke
    public static void roll(CallObj co)
    {
        final var tc = co.textChannel;
        new Thread(() ->
        {
            try
            {
                var con = Jsoup.connect("https://c.xkcd.com/random/comic/");
                var doc = con.get();

                var eb = new EmbedBuilder();
                eb.setAuthor(doc.getElementById("ctitle").text(), doc.location());
                eb.setTitle("Link: " + doc.location());
                eb.setImage(doc.selectFirst("#comic img").absUrl("src"));
                eb.setColor(0x008080);

                MR.send(tc, eb.build());
            }
            catch (Exception e)
            {
                tc.sendMessage("An error has happened while loading the XKCD comic. If this problems persists, contact the developer of Botdiril.").submit();
                BotMain.logger.error("An error has happened while loading the XKCD comic.", e);
            }
        }).start();
    }
}
