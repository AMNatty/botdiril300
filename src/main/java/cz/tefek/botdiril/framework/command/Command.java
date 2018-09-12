package cz.tefek.botdiril.framework.command;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tefek.botdiril.framework.permission.PermissionLevel;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Command
{
    public String value();

    public String[] aliases();

    public PermissionLevel powerLevel() default PermissionLevel.EVERYONE;

    public String description();

    public CommandCategory category();

    public int levelLock() default 0;
}