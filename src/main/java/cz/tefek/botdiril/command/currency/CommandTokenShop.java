package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "illegalkekmarket", aliases = { "kekmarket", "blackmarket",
        "kekshop" }, category = CommandCategory.CURRENCY, description = "Visit the illegal kek market.")
public class CommandTokenShop
{
    private static void addItems(EmbedBuilder eb, IIdentifiable item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
        {
            return;
        }

        StringBuilder title = new StringBuilder();

        title.append(item.inlineDescription());

        StringBuilder sub = new StringBuilder();

        sub.append("**ID:** ");
        sub.append(item.getName());
        sub.append("\n**Price:** ");
        sub.append(BotdirilFmt.format(ShopEntries.getTokenPrice(item)));
        sub.append(Icons.TOKEN);
        sub.append("\n");

        eb.addField(title.toString(), sub.toString(), true);
    }

    @CmdInvoke
    public static void shop(CallObj co)
    {
        var prefix = co.sc.getPrefix();
        var eb = new EmbedBuilder();
        eb.setTitle("Black Market");
        eb.setDescription(String.format("Use your %s to buy some items.\nYou have **%s** %s.", Icons.TOKEN, BotdirilFmt.format(co.ui.getKekTokens()), Icons.TOKEN));
        eb.setColor(0x008080);

        Item.items().forEach(item -> addItems(eb, item));
        Card.cards().forEach(card -> addItems(eb, card));

        eb.setFooter("Tip: Use `%sexchange <item> [amount]` or `%siteminfo <item>`.".replace("%s", prefix), null);

        MR.send(co.textChannel, eb.build());
    }
}
