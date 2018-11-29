package cz.tefek.botdiril.framework.command;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tefek.botdiril.framework.permission.EnumPowerLevel;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Command {
    public String[] aliases() default {};

    public CommandCategory category();

    public String description();

    public int levelLock() default 0;

    public EnumPowerLevel powerLevel() default EnumPowerLevel.EVERYONE;

    public EnumSpecialCommandProperty[] special() default {};

    public String value();
}
