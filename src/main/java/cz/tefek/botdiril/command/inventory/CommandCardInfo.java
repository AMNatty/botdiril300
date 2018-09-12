package cz.tefek.botdiril.command.inventory;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;

@Command(value = "cardinfo", aliases = { "ci" }, category = CommandCategory.ITEMS, description = "Shows important information about a card")
public class CommandCardInfo
{
    @CmdInvoke
    public static void show(CallObj co, @CmdPar("card") Card card)
    {
        var eb = new EmbedBuilder();
        eb.setTitle(card.getLocalizedName());
        eb.setDescription(card.getDescription());
        eb.setColor(0x008080);
        var emID = Pattern.compile("[0-9]+").matcher(card.getIcon());
        emID.find();
        var imgUrl = co.guild.getEmoteById(Long.parseLong(emID.group())).getImageUrl();
        eb.setThumbnail(imgUrl);

        eb.setDescription(card.getDescription());

        eb.addField("ID:", card.getName(), true);

        eb.addField("Set:", card.getCardSet().getSetLocalizedName(), true);

        eb.addField("Rarity:", card.getCardRarity().getCardIcon() + " " + card.getCardRarity().getRarityName(), true);

        if (card.hasCollection())
            eb.addField("Collection:", card.getCollectionName(), true);

        if (ShopEntries.canBeBought(card))
            eb.addField(new Field("Buys for:", ShopEntries.getCoinPrice(card) + " " + EnumCurrency.COINS
                    .getIcon(), true));

        if (ShopEntries.canBeSold(card))
            eb.addField(new Field("Sells for:", ShopEntries.getSellValue(card) + " " + EnumCurrency.COINS
                    .getIcon(), true));

        if (ShopEntries.canBeBoughtForTokens(card))
            eb.addField(new Field("Exchanges for:", ShopEntries.getTokenPrice(card) + " " + EnumCurrency.TOKENS
                    .getIcon(), true));

        var ce = CraftingEntries.search(card);

        if (ce != null)
        {
            var craft = ce.getComponents()
                    .stream()
                    .map(ip -> {
                        return ip.getAmount() + " " + ip.getItem().inlineDescription();
                    })
                    .collect(Collectors.joining(", "));

            if (ShopEntries.canBeDisenchanted(card))
                eb.addField(new Field("Crafts from:", craft, true));
        }

        if (ShopEntries.canBeDisenchanted(card))
            eb.addField(new Field("Disenchants for:", ShopEntries
                    .getDustForDisenchanting(card) + " " + EnumCurrency.DUST.getIcon(), true));

        eb.setFooter(card.getFootnote(co), null);

        if (card.hasCustomImage())
            eb.setImage(card.getCustomImage());

        MR.send(co.textChannel, eb.build());
    }
}