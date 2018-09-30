package cz.tefek.botdiril.userdata.items.cardpack;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Icons;

public class ItemCardPackBasic extends ItemCardPack
{

    public ItemCardPackBasic()
    {
        super("basiccardpack", Icons.CARDPACK_BASIC, "Basic Card Pack");
        this.setDescription("Contains all the essential cards for your collections.");
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("You open %d %s and get the following cards:", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        // TODO        

        MR.send(co.textChannel, sb.toString());
    }

}
