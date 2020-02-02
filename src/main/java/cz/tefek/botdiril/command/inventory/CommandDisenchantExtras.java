package cz.tefek.botdiril.command.inventory;

import java.util.concurrent.atomic.AtomicLong;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.ShopEntries;

@Command(value = "disenchantextras", aliases = { "disenchantduplicates", "disenchantdupes", "dustextras",
        "dustduplicates", "dustdupes",
        "dd" }, category = CommandCategory.ITEMS, description = "Disenchants your duplicate cards.")
public class CommandDisenchantExtras
{
    @CmdInvoke
    public static void dust(CallObj co)
    {
        var cards = new AtomicLong();
        var dust = new AtomicLong();

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

                var amountToDust = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToDust);
                dust.addAndGet(ShopEntries.getDustForDisenchanting(item) * amountToDust);
            }

            return true;
        }, co.ui.getFID());

        BotMain.sql.exec("UPDATE " + UserInventory.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", stat ->
        {
            return stat.executeUpdate();
        }, co.ui.getFID());

        co.ui.addDust(dust.get());

        MR.send(co.textChannel, String.format("*Disenchanted **%d %s cards** for **%d %s dust**.*", cards.get(), Icons.CARDS, dust.get(), Icons.DUST));
    }
}
