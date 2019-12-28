package cz.tefek.botdiril.command.inventory;

import java.util.Comparator;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.CraftingEntries;
import cz.tefek.botdiril.userdata.items.CraftingEntries.Recipe;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.ShopEntries;

@Command(value = "recipes", aliases = { "recipelist",
        "rl" }, category = CommandCategory.ITEMS, description = "Shows all craftable items including their recipes.")
public class CommandRecipes
{
    private static final int ITEMS_PER_PAGE = 12;
    private static final Comparator<Recipe> recipeSorter = (i1, i2) ->
    {
        if (i1.getResult() instanceof Card)
            return Integer.MAX_VALUE;

        if (i2.getResult() instanceof Card)
            return Integer.MIN_VALUE;

        if (Items.leagueItems.contains(i1.getResult()))
            return Integer.MAX_VALUE;

        if (Items.leagueItems.contains(i2.getResult()))
            return Integer.MIN_VALUE;

        if (!ShopEntries.canBeBought(i2.getResult()) && !ShopEntries.canBeBought(i1.getResult()))
            return Integer.MIN_VALUE + 1;

        if (!ShopEntries.canBeBought(i2.getResult()))
            return Integer.MIN_VALUE + 1;

        if (!ShopEntries.canBeBought(i1.getResult()))
            return Integer.MAX_VALUE - 1;

        return Long.compare(ShopEntries.getCoinPrice(i2.getResult()), ShopEntries.getCoinPrice(i1.getResult()));
    };

    @CmdInvoke
    public static void show(CallObj co)
    {
        var recipes = CraftingEntries.getRecipes();

        var eb = new EmbedBuilder();

        eb.setTitle("Total " + recipes.size() + " recipes.");
        eb.setDescription("Showing " + ITEMS_PER_PAGE + " recipes per page.");
        eb.setColor(0x008080);

        var isc = recipes.stream();

        var pages = 1 + (recipes.size() - 1) / ITEMS_PER_PAGE;

        eb.appendDescription("\nPage 1/" + pages);

        // Sort by value
        isc.sorted(recipeSorter).limit(ITEMS_PER_PAGE).forEach(recipe ->
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%d %s**", comp.getAmount(), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField(recipe.getResult().inlineDescription(), recipeParts, false);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "recipes <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("page") int page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var recipes = CraftingEntries.getRecipes();

        var eb = new EmbedBuilder();

        eb.setTitle("Total " + recipes.size() + " recipes.");
        eb.setDescription("Showing " + ITEMS_PER_PAGE + " recipes per page.");
        eb.setColor(0x008080);

        var isc = recipes.stream();

        var pageCount = 1 + (recipes.size() - 1) / ITEMS_PER_PAGE;

        if (page > pageCount)
            page = pageCount;

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted(recipeSorter).skip((page - 1) * ITEMS_PER_PAGE).limit(ITEMS_PER_PAGE).forEach(recipe ->
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%d %s**", comp.getAmount(), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField(recipe.getResult().inlineDescription(), recipeParts, false);
        });

        eb.setFooter("Use `" + co.sc.getPrefix() + "recipes <page>` to go to another page.", null);

        MR.send(co.textChannel, eb.build());
    }
}
