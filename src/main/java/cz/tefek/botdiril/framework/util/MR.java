package cz.tefek.botdiril.framework.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class MR
{
    public static void respond(Message mess, String msg)
    {
        send(mess.getTextChannel(), msg);
    }

    public static void send(TextChannel tc, String msg)
    {
        tc.sendMessage(msg).queue();
    }

    public static void send(TextChannel tc, MessageEmbed msg)
    {
        tc.sendMessage(msg).queue();
    }
}
