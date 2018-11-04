package cz.tefek.botdiril.userdata.achievement;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.ItemLookup;

public class Achievement implements IIdentifiable
{
    private int id;
    private String name;
    private String localizedName;
    private String description;
    private String icon;

    private static List<Achievement> storage = new ArrayList<>();

    public Achievement(String name, String localizedName, String description, String icon)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.description = description;
        this.icon = icon;

        ItemLookup.make(this);

        storage.add(this);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setID(int id)
    {
        this.id = id;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public String getIcon()
    {
        return this.icon;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getLocalizedName()
    {
        return localizedName;
    }

    @Override
    public String inlineDescription()
    {
        return localizedName;
    }

    public static Achievement getByName(String name)
    {
        for (var achievement : storage)
        {
            if (achievement.name.equalsIgnoreCase(name))
                return achievement;
        }

        return null;
    }
}
