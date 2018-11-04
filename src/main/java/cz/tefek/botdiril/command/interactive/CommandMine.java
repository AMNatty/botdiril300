package cz.tefek.botdiril.command.interactive;

import java.util.stream.Collectors;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemDrops;
import cz.tefek.botdiril.userdata.items.ItemPickaxe;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPAdder;

@Command(value = "mine", aliases = {}, category = CommandCategory.INTERACTIVE, description = "Mine to get some sweet stuff.")
public class CommandMine
{
    @CmdInvoke
    public static void mine(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.mine, "You still need to wait **$** to mine.");

        var roll = Botdiril.RDG.nextExponential(Math.pow((co.ui.getLevel() + 600) / 150.0, 2));

        var loot = new ItemDrops();
        loot.addItem(Items.keks, Math.round(roll * 5));
        loot.addItem(Items.coins, Math.round(roll * 10));

        if (Botdiril.RDG.nextUniform(0, 1) > 0.9)
            loot.addItem(Items.keys, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > 0.999)
            loot.addItem(Items.gemdiril, 1);

        var xp = Math.round((roll + 1000) / 8);

        XPAdder.addXP(co, xp);

        loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));

        var lootStr = loot.stream().map(ip -> "**" + ip.getAmount() + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", ")) + " and **" + xp + " XP**";

        MR.send(co.textChannel, "You are mining without a pickaxe.\nYou found " + lootStr);
    }

    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item)
    {
        CommandAssert.assertTimer(co.ui, Timers.mine, "You still need to wait **$** to mine.");

        if (!(item instanceof ItemPickaxe))
        {
            co.ui.resetTimer(Timers.mine);
            throw new CommandException("That's not a valid pickaxe.");
        }

        if (co.ui.howManyOf(item) <= 0)
        {
            co.ui.resetTimer(Timers.mine);
            throw new CommandException("You don't have that pickaxe.");
        }

        co.ui.addItem(item, -1);

        var roll = Botdiril.RDG.nextExponential(Math.pow((co.ui.getLevel() + 600) / 150.0, 2));

        var pick = (ItemPickaxe) item;

        var loot = new ItemDrops();
        loot.addItem(Items.keks, Math.round(roll * 5) * pick.getMultiplier());
        loot.addItem(Items.coins, Math.round(roll * 10) * pick.getMultiplier());

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.9, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.keys, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.99, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.redGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.99, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.greenGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.999, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.blueGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.999, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.purpleGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.9998, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.rainbowGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.9998, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.blackGem, 1);

        if (Botdiril.RDG.nextUniform(0, 1) > Math.pow(0.99995, 0.5 + Math.log10(pick.getMultiplier())))
            loot.addItem(Items.gemdiril, 1);

        var xp = Math.round((roll / 1.5 + 300) * Math.pow(pick.getMultiplier(), 0.25));

        XPAdder.addXP(co, xp);

        loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));

        var lootStr = loot.stream().map(ip -> "**" + ip.getAmount() + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", ")) + " and **" + xp + " XP**";

        MR.send(co.textChannel, String.format("You are mining with a **%s**.\nYou found %s.", item.inlineDescription(), lootStr));
    }
}
