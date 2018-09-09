package cz.tefek.botdiril.framework.command;

import cz.tefek.botdiril.serverdata.ServerConfig;
import cz.tefek.botdiril.userdata.UserInventory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CallObj
{
    public User caller;
    public Member callerMember;
    public TextChannel textChannel;
    public Guild guild;
    public JDA jda;
    public Message message;
    public SelfUser bot;

    public ServerConfig sc;

    public String contents;
    public UserInventory ui;
}
