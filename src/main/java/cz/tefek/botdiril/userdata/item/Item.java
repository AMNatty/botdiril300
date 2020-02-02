package cz.tefek.botdiril.userdata.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.ItemLookup;

public class Item implements IIdentifiable
{
    private static final List<Item> items = new ArrayList<Item>();

    public static Item getItemByID(int id)
    {
        return items.stream().filter(i -> i.getID() == id).findAny().orElse(null);
    }

    public static Item getItemByName(String name)
    {
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static List<Item> items()
    {
        return Collections.unmodifiableList(items);
    }

    private String icon;

    private String name;

    private String localizedName;

    private String description = "";

    private int id;

    private String inlineDetails = "";

    public Item(String name, String icon, String localizedName)
    {
        this.name = name;
        this.icon = icon;
        this.localizedName = localizedName;

        this.id = ItemLookup.make(this.name);
        items.add(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Item)
        {
            Item it = (Item) obj;

            return it.getID() == this.getID();
        }

        return false;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    public String getFootnote(CallObj co)
    {
        return "";
    }

    @Override
    public String getIcon()
    {
        if (this.icon == null)
        {
            BotMain.logger.error("Every Item MUST have an icon. " + this.localizedName);
        }

        return this.icon;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public String getLocalizedName()
    {
        return this.localizedName;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public int hashCode()
    {
        return this.getID();
    }

    @Override
    public String inlineDescription()
    {
        return this.getIcon() + " " + this.getLocalizedName() + (this.inlineDetails.isEmpty() ? ""
                : " " + this.inlineDetails);
    }

    public Item setDescription(String description)
    {
        this.description = description;

        return this;
    }

    @Override
    public void setID(int id)
    {
        this.id = id;
    }
}
