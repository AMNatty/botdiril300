package cz.tefek.botdiril.userdata.items.cardpack;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.CardDrops;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CardPools;

public class ItemCardPackBasic extends ItemCardPack
{
    private static final int CONTENTS = 10;

    private static final int DISPLAY_LIMIT = 15;

    public ItemCardPackBasic()
    {
        super("basiccardpack", Icons.CARDPACK_BASIC, "Basic Card Pack");
        this.setDescription("Contains all the essential cards for your collections.");
        ShopEntries.addCoinSell(this, 1000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("You open %d %s and get the following cards:", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var cp = new CardDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
            cp.addItem((Card) CardPools.basicOrCommon.draw().draw(), 1);

        var i = 0;

        for (var cardPair : cp)
        {
            var card = cardPair.getCard();
            var amt = cardPair.getAmount();

            co.ui.addCard(card, amt);

            if (i <= DISPLAY_LIMIT)
                sb.append(String.format("\n%dx %s", amt, card.inlineDescription()));

            i++;
        }

        var dc = cp.distintCount();

        if (dc > DISPLAY_LIMIT)
            sb.append(String.format("\nand %d more different cards...", dc - DISPLAY_LIMIT));

        var dustVal = cp.stream().mapToLong(cardPair -> ShopEntries.getDustForDisenchanting(cardPair.getCard()) * cardPair.getAmount()).sum();

        sb.append(String.format("\nTotal %d cards. Approximate value: %d%s", cp.totalCount(), dustVal, Icons.DUST));

        MR.send(co.textChannel, sb.toString());
    }

}
