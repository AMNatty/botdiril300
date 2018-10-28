package cz.tefek.botdiril.framework.command.invoke;

public enum ParType
{
    /** Any parameter class type */
    BASIC,
    /** Long.class/long.class only */
    AMOUNT_COINS,
    /** Long.class/long.class only */
    AMOUNT_CLASSIC_KEKS,
    /** BigInteger.class only */
    AMOUNT_MEGA_KEKS,
    /** Long.class/long.class only */
    AMOUNT_KEK_TOKENS,
    /** Long.class/long.class only */
    AMOUNT_DUST,
    /** Long.class/long.class only */
    AMOUNT_KEYS,
    /** Long.class/long.class only, the previous parameter must be ITEM */
    AMOUNT_ITEM,
    /** Long.class/long.class only, the previous parameter must be CARD */
    AMOUNT_CARD
}
