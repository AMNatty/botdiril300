package cz.tefek.botdiril.userdata.properties;

public class NValueNotPresentException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NValueNotPresentException(String reason)
    {
        super(reason);
    }
}
