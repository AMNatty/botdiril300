package cz.tefek.botdiril.userdata.items.cardpack;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.CardDrops;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import cz.tefek.botdiril.userdata.pools.CardPools;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;

public class ItemCardPackVoid extends ItemCardPack
{

    private static final int CONTENTS = 12;

    private static final int DISPLAY_LIMIT = 15;

    private static final double CURSE_CHANCE = 0.2;

    public ItemCardPackVoid()
    {
        super("voidcardpack", Icons.CARDPACK_VOID, "Void Card Pack");
        this.setDescription("For a very dangerous place Void is, it contains some *awesome* loot. Open this pack at your own risk. It may turn into a Pandora's box.");
    }

    @Override
    public void open(CallObj co, long amount)
    {

        var fm = String.format("You open %d %s and get the following cards:", amount, this.getIcon());
        var sb = new StringBuilder();

        sb.append(fm);

        var cp = new CardDrops();

        for (int i = 0; i < CONTENTS * amount; i++)
        {
            if (Curser.isCursed(co, EnumCurse.CURSE_OF_YASUO))
            {
                cp.addItem(Card.getCardByName("yasuo"));
                continue;
            }

            cp.addItem((Card) CardPools.rareOrBetterV.draw().draw(), 1);
        }

        var i = 0;

        for (var cardPair : cp)
        {

            var card = cardPair.getCard();
            var amt = cardPair.getAmount();

            co.ui.addCard(card, amt);

            if (i <= DISPLAY_LIMIT)
            {
                sb.append(String.format("\n%dx %s", amt, card.inlineDescription()));
            }

            i++;
        }

        for (int j = 0; j < amount; j++)
        {
            if (Botdiril.RDG.nextUniform(0, 1) < CURSE_CHANCE)
            {
                Curser.curse(co);
            }
        }

        var dc = cp.distintCount();

        if (dc > DISPLAY_LIMIT)
        {
            sb.append(String.format("\nand %d more different cards...", dc - DISPLAY_LIMIT));
        }

        var dustVal = cp.stream().mapToLong(cardPair -> ShopEntries.getDustForDisenchanting(cardPair.getCard()) * cardPair.getAmount()).sum();

        sb.append(String.format("\nTotal %d cards. Approximate value: %d%s", cp.totalCount(), dustVal, Icons.DUST));

        co.po.addLong(EnumStat.CARD_PACKS_OPENED.getName(), amount);

        MR.send(co.textChannel, sb.toString());
    }

}
