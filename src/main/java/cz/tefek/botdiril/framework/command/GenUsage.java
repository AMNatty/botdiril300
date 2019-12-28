package cz.tefek.botdiril.framework.command;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.lang.reflect.Method;

import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;

public class GenUsage
{
    public static String usage(Command cmd)
    {
        var sb = new StringBuilder();
        var cmdClass = CommandStorage.getAccordingClass(cmd);
        var methods = cmdClass.getDeclaredMethods();
        var commandFunc = Arrays.stream(methods).filter(meth ->
        {
            var hasInvoke = meth.getDeclaredAnnotation(CmdInvoke.class) != null;

            if (!hasInvoke)
                return false;

            if (meth.getParameterCount() < 1)
                return false;

            var pars = meth.getParameters();

            var first = pars[0].getType();

            if (first != CallObj.class)
                return false;

            int i = 0;

            for (var parameter : pars)
            {
                if (i > 0 && parameter.getDeclaredAnnotation(CmdPar.class) == null)
                    return false;
                i++;
            }

            return true;
        }).sorted(Comparator.comparing(Method::getParameterCount).reversed()).collect(Collectors.toList());

        for (var meth : commandFunc)
        {
            var parameters = Arrays.stream(meth.getParameters()).map(param -> param.getDeclaredAnnotation(CmdPar.class)).filter(a -> a != null).map(par -> "<" + par.value() + ">").collect(Collectors.joining(" "));
            sb.append('`');
            sb.append(cmd.value());
            sb.append(' ');
            sb.append(parameters);
            sb.append('`');
            sb.append("\n");
        }

        return sb.toString();
    }
}
