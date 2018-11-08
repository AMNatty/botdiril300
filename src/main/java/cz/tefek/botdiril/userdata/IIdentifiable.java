package cz.tefek.botdiril.userdata;

public interface IIdentifiable
{
    public String getDescription();

    public String getIcon();

    public int getID();

    public String getLocalizedName();

    public String getName();

    public default boolean hasIcon()
    {
        return this.getIcon() != null;
    }

    public String inlineDescription();

    /** Never ever ever call this method in standard code. */
    public void setID(int id);
}
