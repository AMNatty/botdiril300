package cz.tefek.botdiril.command.currency;

import java.util.concurrent.atomic.AtomicLong;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;

@Command(value = "sellextras", aliases = { "sellduplicates", "selldupes",
        "sd" }, category = CommandCategory.ITEMS, description = "Sells your duplicate cards.")
public class CommandSellExtras
{
    @CmdInvoke
    public static void dust(CallObj co)
    {
        var cards = new AtomicLong();
        var coins = new AtomicLong();

        BotMain.sql.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount > 1", stat ->
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

                var amountToSell = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToSell);
                coins.addAndGet(ShopEntries.getSellValue(item) * amountToSell);
            }

            return true;
        }, co.ui.getFID());

        BotMain.sql.exec("UPDATE " + UserInventory.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", stat ->
        {
            return stat.executeUpdate();
        }, co.ui.getFID());

        co.ui.addCoins(coins.get());

        MR.send(co.textChannel, String.format("*Disenchanted **%d %s cards** for **%d %s coins**.*", cards.get(), Icons.CARDS, coins.get(), Icons.COIN));
    }
}
