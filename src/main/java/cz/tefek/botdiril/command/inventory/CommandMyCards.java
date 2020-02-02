package cz.tefek.botdiril.command.inventory;

import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.item.CardPair;

@Command(value = "collection", aliases = { "mycards",
        "cardcollection" }, category = CommandCategory.ITEMS, description = "Displays your card collection.")
public class CommandMyCards
{
    private static final int CARDS_PER_PAGE = 18;

    @CmdInvoke
    public static void show(CallObj co)
    {
        show(co, co.callerMember);
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("target") Member user)
    {
        var ui = new UserInventory(user.getUser().getIdLong());

        var cps = new ArrayList<CardPair>();

        BotMain.sql.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount>0", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotMain.logger.warn(String.format("User %d has a null item in their inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                var cp = new CardPair(item, eq.getLong("cr_amount"));
                cp.setLevel(eq.getInt("cr_level"));
                cp.setLevel(eq.getInt("cr_xp"));

                cps.add(cp);
            }

            return true;
        }, ui.getFID());

        if (cps.isEmpty())
        {
            MR.send(co.textChannel, "The card collection is empty.");
            return;
        }

        var eb = new EmbedBuilder();

        eb.setTitle("This user has " + cps.size() + " different cards.");
        eb.setDescription(user.getAsMention() + "'s card collection.");
        eb.setColor(0x008080);
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());
        var isc = cps.stream();

        var pages = 1 + (cps.size() - 1) / CARDS_PER_PAGE;

        eb.appendDescription("\nPage 1/" + pages);

        isc.sorted((i1, i2) ->
        {
            return Long.compare(Card.getPrice(i2.getCard(), i2.getLevel()), Card.getPrice(i1.getCard(), i1.getLevel()));
        }).limit(CARDS_PER_PAGE).forEach(ip ->
        {
            eb.addField(ip.getCard().inlineDescription(), String.format("Count: **%d**\nID: **%s**", ip.getAmount(), ip.getCard().getName()), true);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "collection " + user.getUser().getIdLong() + " <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("target") Member user, @CmdPar("page") int page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var ui = new UserInventory(user.getUser().getIdLong());

        var cps = new ArrayList<CardPair>();

        BotMain.sql.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount>0", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotMain.logger.warn(String.format("User %d has a null item in their inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                var cp = new CardPair(item, eq.getLong("cr_amount"));
                cp.setLevel(eq.getInt("cr_level"));
                cp.setLevel(eq.getInt("cr_xp"));

                cps.add(cp);
            }

            return true;
        }, ui.getFID());

        if (cps.isEmpty())
        {
            MR.send(co.textChannel, "The card collection is empty.");
            return;
        }

        var eb = new EmbedBuilder();

        eb.setTitle("This user has " + cps.size() + " different cards.");
        eb.setDescription(user.getAsMention() + "'s card collection.");
        eb.setColor(0x008080);
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());
        var isc = cps.stream();

        var pageCount = 1 + (cps.size() - 1) / CARDS_PER_PAGE;

        if (page > pageCount)
        {
            page = pageCount;
        }

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted((i1, i2) ->
        {
            return Long.compare(Card.getPrice(i2.getCard(), i2.getLevel()), Card.getPrice(i1.getCard(), i1.getLevel()));
        }).skip((page - 1) * CARDS_PER_PAGE).limit(CARDS_PER_PAGE).forEach(ip ->
        {
            eb.addField(ip.getCard().inlineDescription(), String.format("Count: **%d**\nID: **%s**", ip.getAmount(), ip.getCard().getName()), true);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "collection " + user.getUser().getIdLong() + " <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }
}
