package cz.tefek.botdiril.framework.command.invoke;

public class CommandException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5783336194925451986L;

    public CommandException(String reason)
    {
        super(reason);
    }
}
