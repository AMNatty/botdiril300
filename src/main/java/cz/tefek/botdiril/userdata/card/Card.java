package cz.tefek.botdiril.userdata.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.ItemLookup;
import cz.tefek.botdiril.userdata.pools.CardPools;

public class Card implements IIdentifiable
{
    private String name;
    private String localizedName;
    private String description = "";
    private CardSet cardSet;
    private EnumCardRarity cardRarity;
    private String customImage = null;;

    private String collection;
    private String collectionName;

    private int id;

    private static final List<Card> cards = new ArrayList<Card>();

    public Card(CardSet cardCollection, EnumCardRarity cardRarity, String name, String localizedName)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.cardSet = cardCollection;
        this.cardRarity = cardRarity;

        this.id = ItemLookup.make(this.name);
        cards.add(this);

        switch (this.cardRarity)
        {
            case BASIC:
                CardPools.basic.add(this);
                break;
            case COMMON:
                CardPools.common.add(this);
                break;
            case RARE:
                CardPools.rare.add(this);
                break;
            case LEGACY:
                CardPools.legacy.add(this);
                break;
            case LEGENDARY:
                CardPools.legendary.add(this);
                break;
            case LEGACY_LEGENDARY:
                CardPools.legacylegendary.add(this);
                break;
            case ULTIMATE:
                CardPools.ultimate.add(this);
                break;
            case LIMITED:
                CardPools.limited.add(this);
                break;
            case MYTHIC:
                CardPools.mythical.add(this);
                break;
            case UNIQUE:
                CardPools.unique.add(this);
                break;
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setID(int id)
    {
        this.id = id;
    }

    public Card setCustomImage(String customImage)
    {
        this.customImage = customImage;

        return this;
    }

    public String getCustomImage()
    {
        return customImage;
    }

    public boolean hasCustomImage()
    {
        return this.customImage != null;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public String getIcon()
    {
        return cardRarity.getCardIcon();
    }

    public EnumCardRarity getCardRarity()
    {
        return cardRarity;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    public Card setDescription(String description)
    {
        this.description = description;

        return this;
    }

    @Override
    public String getLocalizedName()
    {
        return this.localizedName;
    }

    @Override
    public String inlineDescription()
    {
        return this.getIcon() + " " + this.getLocalizedName();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Card)
        {
            Card it = (Card) obj;

            return it.getID() == this.getID();
        }

        return false;
    }

    public static Card getCardByName(String name)
    {
        return cards.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static Card getCardByID(int id)
    {
        return cards.stream().filter(i -> i.getID() == id).findAny().orElse(null);
    }

    public static List<Card> cards()
    {
        return Collections.unmodifiableList(cards);
    }

    public String getFootnote(CallObj co)
    {
        return "";
    }

    public CardSet getCardSet()
    {
        return cardSet;
    }

    public boolean hasCollection()
    {
        return collection != null;
    }

    public String getCollection()
    {
        return collection;
    }

    public Card setCollection(String collection)
    {
        this.collection = collection;

        return this;
    }

    public String getCollectionName()
    {
        return collectionName;
    }

    public Card setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;

        return this;
    }

    public static List<Card> getByCollection(String collection)
    {
        return cards.stream().filter(card -> card.collection.equalsIgnoreCase(collection)).collect(Collectors.toList());
    }
}
