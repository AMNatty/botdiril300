package cz.tefek.botdiril.userdata.items.scrolls;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.xp.XPAdder;

public class ItemLesserScrollOfIntelligence extends Item implements IOpenable
{
    private static final long XP = 500;

    public ItemLesserScrollOfIntelligence()
    {
        super("lesserscrollofintelligence", Icons.SCROLL, "Lesser Scroll of Intelligence");
        this.setDescription(MessageFormat.format("Use to instantly gain **{0} XP**.", XP));
    }

    @Override
    public void open(CallObj co, long amount)
    {
        MR.send(co.textChannel, MessageFormat.format("You read the **{0}**... **[+{1} XP]**", this.inlineDescription(), amount * XP));
        XPAdder.addXP(co, XP * amount);

        co.po.close();
    }

}
