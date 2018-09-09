package cz.tefek.botdiril.userdata;

public interface IIdentifiable
{
    public String getName();

    /** Never ever ever call this method in stardard code. */
    public void setID(int id);

    public int getID();

    public String getIcon();

    public String getDescription();

    public String getLocalizedName();

    public String inlineDescription();

    public default boolean hasIcon()
    {
        return this.getIcon() != null;
    }
}
