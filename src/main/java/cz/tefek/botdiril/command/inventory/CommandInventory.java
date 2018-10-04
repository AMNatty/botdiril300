package cz.tefek.botdiril.command.inventory;

import java.util.ArrayList;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemCurrency;
import cz.tefek.botdiril.userdata.items.ItemPair;
import cz.tefek.botdiril.userdata.items.ShopEntries;

@Command(value = "inventory", aliases = { "inv",
        "i" }, category = CommandCategory.ITEMS, description = "Shows your/someone's inventory.")
public class CommandInventory
{
    private static final int ITEMS_PER_PAGE = 21;

    @CmdInvoke
    public static void show(CallObj co)
    {
        show(co, co.callerMember);
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("target") Member user)
    {
        var ui = new UserInventory(user.getUser().getIdLong());

        var ips = new ArrayList<ItemPair>();

        BotMain.sql.exec("SELECT * FROM " + UserInventory.TABLE_INVENTORY + " WHERE fk_us_id=? AND it_amount>0", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Item.getItemByID(ilID);

                if (item == null)
                {
                    BotMain.logger.warn(String.format("User %d has a null item in their inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                if (item instanceof ItemCurrency)
                    continue;

                ips.add(new ItemPair(item, eq.getLong("it_amount")));
            }

            return true;
        }, ui.getFID());

        if (ips.isEmpty())
        {
            MR.send(co.textChannel, "The inventory is empty.");
            return;
        }

        var eb = new EmbedBuilder();

        eb.setTitle("This user has " + ips.size() + " different types of items.");
        eb.setDescription(user.getAsMention() + "'s inventory.");
        eb.setColor(0x008080);
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());
        var isc = ips.stream();

        var pages = 1 + (ips.size() - 1) / ITEMS_PER_PAGE;

        eb.appendDescription("\nPage 1/" + pages);

        // Sort by value
        isc.sorted((i1, i2) ->
        {
            if (!ShopEntries.canBeBought(i2.getItem()) && !ShopEntries.canBeBought(i1.getItem()))
                return Integer.MIN_VALUE;

            if (!ShopEntries.canBeBought(i2.getItem()))
                return Integer.MIN_VALUE;

            if (!ShopEntries.canBeBought(i1.getItem()))
                return Integer.MAX_VALUE;

            return Long.compare(ShopEntries.getCoinPrice(i2.getItem()), ShopEntries.getCoinPrice(i1.getItem()));
        }).limit(ITEMS_PER_PAGE).forEach(ip ->
        {
            eb.addField(ip.getItem().inlineDescription(), "You have: " + ip.getAmount(), true);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "i " + user.getUser().getIdLong() + " <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("target") Member user, @CmdPar("page") int page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var ui = new UserInventory(user.getUser().getIdLong());

        var ips = new ArrayList<ItemPair>();

        BotMain.sql.exec("SELECT * FROM " + UserInventory.TABLE_INVENTORY + " WHERE fk_us_id=? AND it_amount>0", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Item.getItemByID(ilID);

                if (item == null)
                {
                    BotMain.logger.warn(String.format("User %d has a null item in their inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                if (item instanceof ItemCurrency)
                    continue;

                ips.add(new ItemPair(item, eq.getLong("it_amount")));
            }

            return true;
        }, ui.getFID());

        if (ips.isEmpty())
        {
            MR.send(co.textChannel, "The inventory is empty.");
            return;
        }

        var eb = new EmbedBuilder();

        eb.setTitle("This user has " + ips.size() + " different types of items.");
        eb.setDescription(user.getAsMention() + "'s inventory.");
        eb.setColor(0x008080);
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());

        var isc = ips.stream();

        var pageCount = 1 + (ips.size() - 1) / ITEMS_PER_PAGE;

        if (page > pageCount)
            page = pageCount;

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted((i1, i2) ->
        {
            if (!ShopEntries.canBeBought(i2.getItem()) && !ShopEntries.canBeBought(i1.getItem()))
                return Integer.MIN_VALUE;

            if (!ShopEntries.canBeBought(i2.getItem()))
                return Integer.MIN_VALUE;

            if (!ShopEntries.canBeBought(i1.getItem()))
                return Integer.MAX_VALUE;

            return Long.compare(ShopEntries.getCoinPrice(i2.getItem()), ShopEntries.getCoinPrice(i1.getItem()));
        }).skip((page - 1) * ITEMS_PER_PAGE).limit(ITEMS_PER_PAGE).forEach(ip ->
        {
            eb.addField(ip.getItem().inlineDescription(), String.format("You have: **%d**", ip.getAmount()), true);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "i " + user.getUser().getIdLong() + " <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }
}
