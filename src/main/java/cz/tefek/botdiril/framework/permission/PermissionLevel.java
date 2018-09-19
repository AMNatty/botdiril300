package cz.tefek.botdiril.framework.permission;

public enum PermissionLevel
{
    // NEVER EVER EVER USE 1 << 31 (signed comparison)
    EVERYONE(0),
    ELEVATED(28),
    SUPERUSER(29),
    SUPERUSER_OVERRIDE(30);

    private int permBit;

    private PermissionLevel(int permBit)
    {
        this.permBit = 1 << permBit;
    }

    public int getPermBit()
    {
        return permBit;
    }

    public boolean satisfies(int permLevel)
    {
        return permLevel >= permBit;
    }
}
