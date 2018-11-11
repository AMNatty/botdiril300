package cz.tefek.botdiril.userdata.items;

import cz.tefek.botdiril.userdata.EnumCurrency;

public class ItemCurrency extends Item
{
    private EnumCurrency currency;

    public ItemCurrency(EnumCurrency currency)
    {
        super(currency.getName(), currency.getIcon(), currency.getLocalizedName());

        this.currency = currency;
    }

    public EnumCurrency getCurrency()
    {
        return currency;
    }

    @Override
    public String getDescription()
    {
        return currency.getDescription();
    }
}
