package cz.tefek.botdiril.command.debug;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;

@Command(value = "addf", aliases = {}, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OWNER, description = "Add a huge amount of cards to users.")
public class CommandLargeAddCards
{
    @CmdInvoke
    public static void addf(CallObj co)
    {
        var at = co.message.getAttachments();

        CommandAssert.assertIdentity(at.size(), 1, "Attach a source file please.");

        var file = at.get(0);

        var fileUrl = file.getUrl();

        try (var r = new BufferedReader(new InputStreamReader(new URL(fileUrl).openStream())))
        {
            MR.send(co.textChannel, "Opening file...");

            String line;

            while ((line = r.readLine()) != null)
            {
                var p = line.split(":");

                if (p.length != 2)
                {
                    continue;
                }

                try
                {
                    var strID = p[0];

                    var id = Long.parseLong(strID.trim());

                    var cardsStr = p[1].split(",");

                    var ui = new UserInventory(id);

                    var user = co.jda.getUserById(id);

                    if (user != null)
                        MR.send(co.textChannel, "Processing " + user.getName() + "...");
                    else
                        MR.send(co.textChannel, "Processing " + id + "...");

                    for (var pair : cardsStr)
                    {
                        var paiParts = pair.trim().split("\\*");

                        if (paiParts.length != 2)
                            continue;

                        var name = paiParts[1];
                        var count = Long.parseLong(paiParts[0]);

                        var card = Card.getCardByName(name);

                        if (card != null)
                        {
                            ui.addCard(card, count);
                        }
                    }
                }
                catch (Exception e)
                {
                    BotMain.logger.error("Error while parsing line...", e);
                }
            }

            MR.send(co.textChannel, "I am done...");
        }
        catch (Exception e)
        {
            BotMain.logger.error("Error in addf: ", e);
        }
    }
}
