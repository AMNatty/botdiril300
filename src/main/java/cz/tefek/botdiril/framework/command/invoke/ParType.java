package cz.tefek.botdiril.framework.command.invoke;

public enum ParType
{
    /** Any parameter class type */
    BASIC,
    /** IIdentifiable only */
    ITEM_OR_CARD,
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
    /** Long.class/long.class only, the previous parameter must be ITEM or CARD */
    AMOUNT_ITEM_OR_CARD,
    /** Long.class/long.class only, the previous parameter must be ITEM */
    AMOUNT_ITEM_OR_CARD_BUY_COINS,
    /** Long.class/long.class only, the previous parameter must be ITEM */
    AMOUNT_ITEM_OR_CARD_BUY_TOKENS
}
