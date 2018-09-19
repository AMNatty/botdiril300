package cz.tefek.botdiril.command.inventory;

import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.text.similarity.FuzzyScore;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.card.Card;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(value = "cardlist", aliases = {}, category = CommandCategory.ITEMS, description = "Shows a browsable list of cards")
public class CommandCardList
{
    @CmdInvoke
    public static void showFirstPage(CallObj co)
    {
        showPage(co, 1);
    }

    @CmdInvoke
    public static void showPage(CallObj co, @CmdPar("page") int page)
    {
        var items = Card.cards();

        var itemsPerPage = 21;

        var pages = items.size() / itemsPerPage + 1;

        var prefix = co.sc.getPrefix();

        CommandAssert.numberInBoundsInclusiveL(page, 1, pages, String.format("Select a page in the range 1..%d", pages));

        var eb = new EmbedBuilder();

        eb.setTitle("Card list");
        eb.setColor(0x008080);
        eb.setDescription(String.format("**Page %d/%d**\nUse `%scardlist <page>` to browse.", page, pages, prefix));

        items.stream().skip(itemsPerPage * (page - 1)).limit(itemsPerPage).forEach(it ->
        {
            eb.addField(it.inlineDescription(), "**ID: **" + it.getName(), true);
        });

        eb.setFooter("Use `" + prefix + "cardinfo <card id>` to show more information about a card.", null);

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void showPage(CallObj co, @CmdPar("search query") String search, @CmdPar("page") int page)
    {
        CommandAssert.stringNotTooLong(search, 50, "The search string can't be this long.");

        var items = Card.cards();

        var itemsPerPage = 21;

        var pages = items.size() / itemsPerPage + 1;

        var prefix = co.sc.getPrefix();

        CommandAssert.numberInBoundsInclusiveL(page, 1, pages, String.format("Select a page in the range 1..%d", pages));

        var eb = new EmbedBuilder();

        eb.setTitle("Card list");
        eb.setColor(0x008080);
        eb.setDescription(String.format("**Page %d/%d**\nSearch results for `%s`.\nUse `%scardlist <search> <page>` to browse.", page, pages, search, prefix));

        var fc = new FuzzyScore(Locale.getDefault());

        Comparator<? super Card> itemcp = Comparator.comparing((Card it) -> fc.fuzzyScore(it.getName(), search)).reversed();

        items.stream().sorted(itemcp).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).forEach(it ->
        {
            eb.addField(it.inlineDescription(), "**ID: **" + it.getName(), true);
        });

        eb.setFooter("Use `" + prefix + "cardinfo <card id>` to show more information about a card.", null);

        MR.send(co.textChannel, eb.build());
    }
}
