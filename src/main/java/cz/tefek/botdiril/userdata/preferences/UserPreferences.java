package cz.tefek.botdiril.userdata.preferences;

import cz.tefek.botdiril.userdata.properties.PropertyObject;

public class UserPreferences
{
    public static final String PREFERENCE_BITFIELD = "preferences_bool_bitfield";

    public static boolean isBitEnabled(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        return (po.getLongOrDefault(PREFERENCE_BITFIELD, 0) & bit) != 0;
    }

    public static boolean toggleBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        var newVal = po.getLongOrDefault(PREFERENCE_BITFIELD, 0) ^ bit;
        po.setLong(PREFERENCE_BITFIELD, newVal);

        return (newVal & bit) != 0; // The new, toggled value
    }

    public static void setBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        po.setLong(PREFERENCE_BITFIELD, po.getLongOrDefault(PREFERENCE_BITFIELD, 0) | bit);
    }

    public static void clearBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        po.setLong(PREFERENCE_BITFIELD, po.getLongOrDefault(PREFERENCE_BITFIELD, 0) & ~bit);
    }
}
