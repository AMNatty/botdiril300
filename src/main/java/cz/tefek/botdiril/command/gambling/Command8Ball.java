package cz.tefek.botdiril.command.gambling;

import net.dv8tion.jda.core.EmbedBuilder;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;

@Command(value = "8ball", category = CommandCategory.GAMBLING, aliases = {}, description = "Ask the 8ball.")
public class Command8Ball
{
    private static final String[] answers = { "It is certain.", "It is decidedly so.", "Without a doubt.",
            "Yes - definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.",
            "Signs point to yes.", "Reply hazy, try again", "Ask again later.", "Better not tell you now.",
            "Cannot predict now.", "Concentrate and ask again.", "Don't count on it.", "My reply is no.",
            "My sources say no.", "Outlook not so good.", "Very doubtful." };

    @CmdInvoke
    public static void roll(CallObj co, @CmdPar("question") String question)
    {
        CommandAssert.stringNotTooLong(question, 300, "Your question is too long, please make it shorter.");

        var eb = new EmbedBuilder();
        eb.setAuthor(co.callerMember.getEffectiveName() + "'s question");
        eb.setDescription(question);
        eb.setColor(0x008080);
        eb.addField("Answer:", ":8ball: | " + answers[Botdiril.RDG.nextInt(0, answers.length)], false);

        MR.send(co.textChannel, eb.build());
    }
}
