package cz.tefek.botdiril.userdata.item;

import cz.tefek.botdiril.framework.command.CallObj;

public interface IOpenable
{
    public void open(CallObj co, long amount);

    public default boolean requiresKey()
    {
        return false;
    }
}
