package cz.tefek.botdiril;

import java.util.Random;

import javax.security.auth.login.LoginException;

import org.apache.commons.math3.random.RandomDataGenerator;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

public class Botdiril
{
    public static final String AUTHOR = "<@263648016982867969>";
    public static final Random RANDOM = new Random();
    public static final RandomDataGenerator RDG = new RandomDataGenerator();
    public static final String BRANDING = "Botdiril";

    private EventBus eventBus;

    public Botdiril() throws LoginException
    {
        var jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.addEventListener(eventBus = new EventBus());
        jdaBuilder.setToken(BotMain.config.getApiKey());
        jdaBuilder.build();
    }

    public EventBus getEventBus()
    {
        return eventBus;
    }
}
