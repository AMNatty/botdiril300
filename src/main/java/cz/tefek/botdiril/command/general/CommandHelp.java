package cz.tefek.botdiril.command.general;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;

import java.awt.Color;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.CommandStorage;
import cz.tefek.botdiril.framework.command.GenUsage;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "help", aliases = {
        "usage" }, category = CommandCategory.GENERAL, description = "General help command.")
public class CommandHelp
{
    @CmdInvoke
    public static void show(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setColor(Color.CYAN.getRGB());
        eb.setTitle("Stuck? Here is your help:");

        Arrays.stream(CommandCategory.values()).forEach(cat ->
        {
            eb.addField(cat.getName() + " [" + CommandStorage.commandCountInCategory(cat) + "]", "Type ``" + co.sc.getPrefix() + "help " + cat.toString().toLowerCase() + "``", false);
        });

        long cmdCnt = CommandStorage.commandCount();
        int catCnt = CommandCategory.values().length;

        eb.setDescription("There are " + cmdCnt + " commands in " + catCnt + " categories total.");

        MR.send(co.textChannel, eb.build());
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("category or command") String tbp)
    {
        var prefix = co.sc.getPrefix();

        try
        {
            var command = CommandAssert.parseCommand(tbp);
            var sb = new StringBuilder("**Command `" + command.value() + "`**:");
            if (command.aliases().length != 0)
                sb.append("\n**Aliases:** " + Arrays.stream(command.aliases()).map(a -> "`" + a + "`").collect(Collectors.joining(", ")));
            sb.append("\n**Description:** " + command.description());
            sb.append("\n**Power level required:** " + command.powerLevel());
            sb.append("\n**Available from level:** " + (command.levelLock() == 0 ? "Always available"
                    : command.levelLock()));
            sb.append("\n**Usage:**\n");
            sb.append(GenUsage.usage(command));

            MR.send(co.textChannel, sb.toString());
        }
        catch (CommandException e)
        {
            var found = CommandAssert.parseCommandGroup(tbp);

            var eb = new EmbedBuilder();
            eb.setColor(Color.CYAN.getRGB());
            eb.setTitle("Help for the " + found.getName());

            CommandStorage.getCommandsByCategory(found).forEach(comm ->
            {
                eb.addField(new Field(comm.value(), comm.description(), false));
            });

            eb.setDescription("Type ``" + prefix + "help <command>`` to show more information for each command.");

            MR.send(co.textChannel, eb.build());
        }
    }
}
