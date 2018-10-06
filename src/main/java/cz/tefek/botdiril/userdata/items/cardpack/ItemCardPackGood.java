package cz.tefek.botdiril.userdata.items.cardpack;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.CardDrops;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CardPools;

public class ItemCardPackGood extends ItemCardPack
{
    private static final int CONTENTS = 8;

    private static final int DISPLAY_LIMIT = 15;

    public ItemCardPackGood()
    {
        super("goodcardpack", Icons.CARDPACK_GOOD, "Good Card Pack");
        this.setDescription("For the true collectors, drops legacy/rare card or better.");
        ShopEntries.addCoinSell(this, 100000);
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("You open %d %s and get the following cards:", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var cp = new CardDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
            cp.addItem((Card) CardPools.rareOrBetter.draw().draw(), 1);

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
