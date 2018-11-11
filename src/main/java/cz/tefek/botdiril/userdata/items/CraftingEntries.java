package cz.tefek.botdiril.userdata.items;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.userdata.IIdentifiable;

public class CraftingEntries
{
    public static class Recipe
    {
        private List<ItemPair> components = new ArrayList<>();
        private long yields;
        private IIdentifiable result;

        public Recipe(List<ItemPair> components, long yields, IIdentifiable result)
        {
            this.components = components;
            this.yields = yields;
            this.result = result;
        }

        public List<ItemPair> getComponents()
        {
            return components;
        }

        public long getAmount()
        {
            return yields;
        }

        public IIdentifiable getResult()
        {
            return result;
        }
    }

    private static List<Recipe> recipes = new ArrayList<Recipe>();

    public static void add(Recipe recipe)
    {
        recipes.add(recipe);
    }

    public static Recipe search(IIdentifiable result)
    {
        return recipes.stream().filter(r -> r.getResult().equals(result)).findAny().orElse(null);
    }
}
