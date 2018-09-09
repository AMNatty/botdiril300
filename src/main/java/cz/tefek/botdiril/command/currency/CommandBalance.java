package cz.tefek.botdiril.command.currency;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.BigNumbers;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.User;

@Command(value = "balance", aliases = { "money",
                                        "coins",
                                        "bal" }, category = CommandCategory.CURRENCY, description = "Shows your/someone's balance.")
public class CommandBalance
{
    @CmdInvoke
    public static void show(CallObj co)
    {
        show(co, co.caller);
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("user") User u)
    {
        var sameGuy = u.getIdLong() == co.caller.getIdLong();

        var ui = sameGuy ? co.ui : new UserInventory(u.getIdLong());

        var uo = ui.getUserDataObj();

        var eb = new EmbedBuilder();
        eb.setTitle(u.getName() + "'s balance");
        eb.setThumbnail(u.getEffectiveAvatarUrl());

        if (uo.level != XPRewards.getMaxLevel())
            eb.setDescription(String.format("Level %d\n%d/%d xp (%.2f%%)", uo.level, uo.xp, XPRewards
                    .getXPAtLevel(uo.level), (double) uo.xp / XPRewards.getXPAtLevel(uo.level) * 100));
        else
            eb.setDescription(String.format("Level %d", uo.level));

        eb.setColor(0x008080);

        eb.addField(new Field(EnumCurrency.COINS.getLocalizedName(), String
                .format("%d %s\n", uo.coins, EnumCurrency.COINS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.KEKS.getLocalizedName(), String
                .format("%d %s\n", uo.keks, EnumCurrency.KEKS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.MEGAKEKS.getLocalizedName(), String
                .format("%s %s\n", BigNumbers.stringifyBoth(uo.megakeks), EnumCurrency.MEGAKEKS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.TOKENS.getLocalizedName(), String
                .format("%d %s\n", uo.tokens, EnumCurrency.TOKENS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.KEYS.getLocalizedName(), String
                .format("%d %s\n", uo.keys, EnumCurrency.KEYS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.DUST.getLocalizedName(), String
                .format("%d %s\n", uo.dust, EnumCurrency.DUST.getIcon()), true));
        eb.addField(new Field("Cards", String.format("%d %s\n", uo.cards, Icons.CARDS), true));

        MR.send(co.textChannel, eb.build());
    }
}
