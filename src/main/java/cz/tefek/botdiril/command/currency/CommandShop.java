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

@Command(value = "shop", aliases = { "store",
        "market" }, category = CommandCategory.CURRENCY, description = "Opens the shops.")
public class CommandShop
{
    @CmdInvoke
    public static void shop(CallObj co)
    {
        var prefix = co.sc.getPrefix();
        var eb = new EmbedBuilder();
        eb.setTitle("Botdiril's Shop");
        eb.setDescription(String.format("You have **%s** %s.", BotdirilFmt.format(co.ui.getCoins()), Icons.COIN));
        eb.setColor(0x008080);

        Item.items().forEach(item -> addItems(eb, item));
        Card.cards().forEach(card -> addItems(eb, card));

        eb.setFooter("Tip: Use `%sbuy <item> [amount]`, `%ssell <item> [amount]` or `%siteminfo <item>`.".replace("%s", prefix), null);

        MR.send(co.textChannel, eb.build());
    }

    private static void addItems(EmbedBuilder eb, IIdentifiable item)
    {
        if (!ShopEntries.canBeBought(item))
        {
            return;
        }

        StringBuilder title = new StringBuilder();

        title.append(item.inlineDescription());

        StringBuilder sub = new StringBuilder();

        sub.append("**ID:** ");
        sub.append(item.getName());
        sub.append("\n**Price:** ");
        sub.append(BotdirilFmt.format(ShopEntries.getCoinPrice(item)));
        sub.append(Icons.COIN);
        sub.append("\n");
        if (ShopEntries.canBeSold(item))
        {
            sub.append("**Sells back for:** ");
            sub.append(BotdirilFmt.format(ShopEntries.getSellValue(item)));
            sub.append(Icons.COIN);
        }
        else
        {
            sub.append("*Cannot be sold.*");
        }

        eb.addField(title.toString(), sub.toString(), true);
    }
}
