package cz.tefek.botdiril.command.inventory;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

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

        var emote = co.jda.getEmoteById(Long.parseLong(emID.group()));
        if (emote != null)
        {
            var imgUrl = emote.getImageUrl();
            eb.setThumbnail(imgUrl);
        }

        eb.addField("ID:", item.getName(), true);

        if (ShopEntries.canBeBought(item))
        {
            eb.addField(new Field("Buys for:", BotdirilFmt.format(ShopEntries.getCoinPrice(item)) + " " + EnumCurrency.COINS.getIcon(), true));
        }

        if (ShopEntries.canBeSold(item))
        {
            eb.addField(new Field("Sells for:", BotdirilFmt.format(ShopEntries.getSellValue(item)) + " " + EnumCurrency.COINS.getIcon(), true));
        }

        if (ShopEntries.canBeBoughtForTokens(item))
        {
            eb.addField(new Field("Exchanges for:", BotdirilFmt.format(ShopEntries.getTokenPrice(item)) + " " + EnumCurrency.TOKENS.getIcon(), true));
        }

        var recipe = CraftingEntries.search(item);

        if (recipe != null)
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%s %s**", BotdirilFmt.format(comp.getAmount()), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField("Crafts from", recipeParts + "\n*Recipe yields " + BotdirilFmt.format(recipe.getAmount()) + " item(s).*", false);
        }

        if (ShopEntries.canBeDisenchanted(item))
        {
            eb.addField(new Field("Disenchants for:", BotdirilFmt.format(ShopEntries.getDustForDisenchanting(item)) + " " + EnumCurrency.DUST.getIcon(), false));
        }

        eb.setFooter(item.getFootnote(co), null);

        MR.send(co.textChannel, eb.build());
    }
}
