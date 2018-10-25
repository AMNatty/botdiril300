package cz.tefek.botdiril;

import java.util.Random;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import org.apache.commons.math3.random.RandomDataGenerator;

import javax.security.auth.login.LoginException;

public class Botdiril
{
    public static final String AUTHOR = "<@263648016982867969>";
    public static final Random RANDOM = new Random();
    public static final RandomDataGenerator RDG = new RandomDataGenerator();
    public static final String BRANDING = "Botdiril";

    private EventBus eventBus;

    public Botdiril() throws LoginException, InterruptedException
    {
        var jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.addEventListener(this.eventBus = new EventBus());
        jdaBuilder.setToken(BotMain.config.getApiKey());
        jdaBuilder.build().awaitReady().setAutoReconnect(true);
    }

    public EventBus getEventBus()
    {
        return this.eventBus;
    }
}
