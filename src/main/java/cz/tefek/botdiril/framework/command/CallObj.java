package cz.tefek.botdiril.framework.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.serverdata.ServerConfig;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

public class CallObj
{
    public User caller;
    public Member callerMember;
    public TextChannel textChannel;
    public Guild guild;
    public JDA jda;
    public Message message;
    public SelfUser bot;
    public PropertyObject po;

    public ServerConfig sc;

    public String contents;
    public UserInventory ui;
}
