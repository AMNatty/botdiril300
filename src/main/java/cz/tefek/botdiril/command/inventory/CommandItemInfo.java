package cz.tefek.botdiril.command.inventory;

import java.util.regex.Pattern;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ShopEntries;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;

@Command(value = "iteminfo", aliases = {
        "ii" }, category = CommandCategory.ITEMS, description = "Shows important information about an item")
public class CommandItemInfo
{
    @CmdInvoke
    public static void show(CallObj co, @CmdPar("item") Item item)
    {
        var eb = new EmbedBuilder();
        eb.setTitle(item.inlineDescription());
        eb.setDescription(item.getDescription());
        eb.setColor(0x008080);
        var emID = Pattern.compile("[0-9]+").matcher(item.getIcon());
        emID.find();
        var imgUrl = co.guild.getEmoteById(Long.parseLong(emID.group())).getImageUrl();
        eb.setThumbnail(imgUrl);

        eb.addField("ID:", item.getName(), true);

        if (ShopEntries.canBeBought(item))
            eb.addField(new Field("Buys for:", ShopEntries.getCoinPrice(item) + " " + EnumCurrency.COINS.getIcon(), true));

        if (ShopEntries.canBeSold(item))
            eb.addField(new Field("Sells for:", ShopEntries.getSellValue(item) + " " + EnumCurrency.COINS.getIcon(), true));

        if (ShopEntries.canBeBoughtForTokens(item))
            eb.addField(new Field("Exchanges for:", ShopEntries.getTokenPrice(item) + " " + EnumCurrency.TOKENS.getIcon(), true));

        if (ShopEntries.canBeDisenchanted(item))
            eb.addField(new Field("Disenchants for:", ShopEntries.getDustForDisenchanting(item) + " " + EnumCurrency.DUST.getIcon(), false));

        eb.setFooter(item.getFootnote(co), null);

        MR.send(co.textChannel, eb.build());
    }
}
