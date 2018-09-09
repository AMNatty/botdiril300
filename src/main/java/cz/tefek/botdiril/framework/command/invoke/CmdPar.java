package cz.tefek.botdiril.framework.command.invoke;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CmdPar
{
    public String value();

    public ParType type() default ParType.BASIC;
}
