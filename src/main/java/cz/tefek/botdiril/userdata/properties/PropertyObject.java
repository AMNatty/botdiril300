package cz.tefek.botdiril.userdata.properties;

public class PropertyObject implements AutoCloseable
{
    private final long id;
    private boolean disableAutoclose;

    public PropertyObject(long id)
    {
        this.id = id;
        this.load();
    }

    public void addInt(String name, int amount)
    {
        this.setInt(name, this.getIntOrDefault(name, 0) + amount);
    }

    public void addLong(String name, long amount)
    {
        this.setLong(name, this.getLongOrDefault(name, 0) + amount);
    }

    @Override
    public void close()
    {
        this.free();
    }

    public native void free();

    public Byte getByte(String name)
    {
        try
        {
            return this.ngetByte(name);
        }
        catch (NValueNotPresentException e)
        {
            return null;
        }
    }

    public native byte getByteOrDefault(String name, byte defaultValue);

    public Integer getInt(String name)
    {
        try
        {
            return this.ngetInt(name);
        }
        catch (NValueNotPresentException e)
        {
            return null;
        }
    }

    public native int getIntOrDefault(String name, int defaultValue);

    public Long getLong(String name)
    {
        try
        {
            return this.ngetLong(name);
        }
        catch (NValueNotPresentException e)
        {
            return null;
        }
    }

    public native long getLongOrDefault(String name, long defaultValue);

    public String getString(String name)
    {
        try
        {
            return this.ngetString(name);
        }
        catch (NValueNotPresentException e)
        {
            return null;
        }
    }

    public native String getStringOrDefault(String name, String defaultValue);

    public native void incrementInt(String name);

    public native void incrementLong(String name);

    public boolean isAutocloseDisabled()
    {
        return this.disableAutoclose;
    }

    private native void load() throws RuntimeException;

    private native byte ngetByte(String name) throws NValueNotPresentException;

    private native int ngetInt(String name) throws NValueNotPresentException;

    private native long ngetLong(String name) throws NValueNotPresentException;

    private native String ngetString(String name) throws NValueNotPresentException;

    public native void setByte(String name, byte val);

    public void setAutocloseDisabled(boolean disableAutoclose)
    {
        this.disableAutoclose = disableAutoclose;
    }

    public native void setInt(String name, int val);

    public native void setLong(String name, long val);

    public native void setString(String name, String val);

    public native void zeroInt(String name);

    public native void zeroLong(String name);
}
