package cz.tefek.botdiril.userdata.achievement;

import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.ItemLookup;

public class Achievement implements IIdentifiable
{
    private int id;

    public Achievement()
    {
        ItemLookup.make(this);
    }

    @Override
    public String getName()
    {
        return null;
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
        return null;
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getLocalizedName()
    {
        return null;
    }

    @Override
    public String inlineDescription()
    {
        return null;
    }

}
