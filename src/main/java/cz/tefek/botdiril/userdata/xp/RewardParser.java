package cz.tefek.botdiril.userdata.xp;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemPair;

public class RewardParser
{
    public static List<ItemPair> parse(String data)
    {
        var list = new ArrayList<ItemPair>();

        try
        {
            var pairs = data.split(",");

            for (var pair : pairs)
            {
                var ps = pair.split("\\*");

                if (ps.length == 1)
                {
                    var item = Item.getItemByName(ps[0]);

                    if (item == null)
                    {
                        BotMain.logger.warn("Errorneous reward: " + ps[0]);
                        continue;
                    }

                    list.add(new ItemPair(item, 1));
                }
                else if (ps.length == 2)
                {
                    var amount = Integer.parseInt(ps[0]);

                    var item = Item.getItemByName(ps[1]);

                    if (item == null)
                    {
                        BotMain.logger.warn("Errorneous reward: " + ps[1]);
                        continue;
                    }

                    list.add(new ItemPair(item, amount));
                }
            }
        }
        catch (Exception e)
        {
            BotMain.logger.error("Broker reward string: " + data, e);
        }

        return list;
    }
}
