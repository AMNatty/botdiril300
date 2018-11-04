package cz.tefek.botdiril.framework.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.BigNumbers;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.serverdata.ChannelPreferences;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.achievement.Achievement;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ShopEntries;

public class CommandParser
{
    private static String cutOffAt(String input, int pos)
    {
        return input.substring(pos);
    }

    private static String cutOffTillNextWhitespace(String input)
    {
        if (!Pattern.compile("\\s+").matcher(input).find())
            return "";

        return input.replaceFirst("^.+?\\s+", "");
    }

    public static void parse(CallObj co)
    {
        var params = co.contents.split("\\s+");
        var cmdStr = params[0];

        if (ChannelPreferences.checkBit(co.textChannel.getIdLong(), ChannelPreferences.BIT_DISABLED) && !EnumPowerLevel.ELEVATED.check(co.callerMember, co.textChannel))
        {
            return;
        }

        var command = CommandStorage.search(cmdStr);

        if (command == null)
            return;

        if (!command.powerLevel().check(co.callerMember, co.textChannel))
        {
            MR.send(co.textChannel, String.format("You need to have the %s power level to use this command!", command.powerLevel().toString()));

            return;
        }

        if (co.ui.getLevel() < command.levelLock() && !(EnumPowerLevel.SUPERUSER_OVERRIDE.check(co.callerMember, co.textChannel) || EnumPowerLevel.VIP.check(co.callerMember, co.textChannel)))
        {
            MR.send(co.textChannel, String.format("You need at least level %d to do this.", command.levelLock()));

            return;
        }

        var cmdClass = CommandStorage.getAccordingClass(command);

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
            var parameters = Arrays.stream(meth.getParameters()).filter(param -> param.getDeclaredAnnotation(CmdPar.class) != null).collect(Collectors.toList());

            var cuts = parameters.size() - 1;

            cuts = cuts == -1 ? 0 : cuts;

            var input = cutOffTillNextWhitespace(co.contents).trim();

            int pos = 0;

            var failed = false;

            var args = new ArrayList<String>(cuts);

            while (cuts > 0)
            {
                if (input.isEmpty())
                {
                    failed = true;
                    break;
                }

                var hasQuotes = input.startsWith("\"");
                var lonelyQuote = input.lastIndexOf('\"') == 0;

                if (hasQuotes)
                {
                    if (!lonelyQuote)
                    {
                        var secondQuote = input.indexOf('"', 1);

                        for (pos = secondQuote; pos < input.length(); pos++)
                        {
                            if (Character.isWhitespace(input.charAt(pos)))
                                break;
                        }
                    }
                }
                else
                {
                    for (pos = 0; pos < input.length(); pos++)
                    {
                        if (Character.isWhitespace(input.charAt(pos)))
                            break;
                    }
                }

                var arg = input.substring(0, pos);

                if (hasQuotes && !lonelyQuote)
                    arg = arg.replaceAll("^\"", "").replaceAll("\"$", "").trim();

                args.add(arg);

                input = cutOffAt(input, pos).trim();

                cuts--;
            }

            if (!input.isEmpty())
            {
                var inp = input.trim();

                if (inp.startsWith("\"") && inp.endsWith("\""))
                    inp = inp.substring(1, input.length() - 1);

                args.add(inp);
            }

            if (failed)
                continue;

            if (args.size() != parameters.size())
                continue;

            try
            {
                var argArr = new Object[parameters.size() + 1];
                argArr[0] = co;

                for (int i = 1; i < argArr.length; i++)
                {
                    var parameter = parameters.get(i - 1);
                    var clazz = parameter.getType();
                    var arg = args.get(i - 1);
                    var ant = parameter.getAnnotation(CmdPar.class);
                    var type = ant.type();

                    if (clazz == int.class || clazz == Integer.class)
                    {
                        var num = CommandAssert.parseInt(arg, "Error: " + arg + " is not a valid number.");

                        argArr[i] = num;
                    }
                    else if (clazz == long.class || clazz == Long.class)
                    {
                        if (type == ParType.AMOUNT_COINS)
                        {
                            argArr[i] = CommandAssert.parseAmount(arg, co.ui.getCoins(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                        }
                        else if (type == ParType.AMOUNT_CLASSIC_KEKS)
                        {
                            argArr[i] = CommandAssert.parseAmount(arg, co.ui.getKeks(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                        }
                        else if (type == ParType.AMOUNT_ITEM_OR_CARD)
                        {
                            if (i == 0)
                                throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM**");

                            if (argArr[i - 1] instanceof Card)
                            {
                                argArr[i] = CommandAssert.parseAmount(arg, co.ui.howManyOf((Card) argArr[i - 1]), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                            }
                            else if (argArr[i - 1] instanceof Item)
                            {
                                argArr[i] = CommandAssert.parseAmount(arg, co.ui.howManyOf((Item) argArr[i - 1]), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                            }
                            else
                            {
                                throw new CommandException("Internal error. Please contact an administrator. Code: **PREV_PARAM_NEITHER_CARD_OR_ITEM**");
                            }
                        }
                        else if (type == ParType.AMOUNT_ITEM_OR_CARD_BUY_COINS)
                        {
                            if (i == 0)
                                throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM**");

                            if (!(argArr[i - 1] instanceof IIdentifiable))
                                throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM_NOT_ITEM**");

                            var item = (IIdentifiable) argArr[i - 1];

                            if (!ShopEntries.canBeBought(item))
                            {
                                throw new CommandException("That item / card cannot be bought.");
                            }

                            argArr[i] = CommandAssert.parseBuy(arg, ShopEntries.getCoinPrice(item), co.ui.getCoins(), "Could not parse the amount your are trying to buy.");
                        }
                        else if (type == ParType.AMOUNT_ITEM_OR_CARD_BUY_TOKENS)
                        {
                            if (i == 0)
                                throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM**");

                            if (!(argArr[i - 1] instanceof IIdentifiable))
                                throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM_NOT_ITEM**");

                            var item = (IIdentifiable) argArr[i - 1];

                            if (!ShopEntries.canBeBoughtForTokens(item))
                            {
                                throw new CommandException("That item / card cannot be bought.");
                            }

                            argArr[i] = CommandAssert.parseBuy(arg, ShopEntries.getTokenPrice(item), co.ui.getKekTokens(), "Could not parse the amount your are trying to buy.");
                        }
                        else if (type == ParType.AMOUNT_KEK_TOKENS)
                        {
                            argArr[i] = CommandAssert.parseAmount(arg, co.ui.getKekTokens(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                        }
                        else if (type == ParType.AMOUNT_KEYS)
                        {
                            argArr[i] = CommandAssert.parseAmount(arg, co.ui.getKeys(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                        }
                        else if (type == ParType.AMOUNT_DUST)
                        {
                            argArr[i] = CommandAssert.parseAmount(arg, co.ui.getDust(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half");
                        }
                        else
                        {
                            var num = CommandAssert.parseInt(arg, "Error: " + arg + " is not a valid number.");

                            argArr[i] = num;
                        }
                    }
                    else if (clazz == BigInteger.class)
                    {
                        if (type == ParType.AMOUNT_MEGA_KEKS)
                        {
                            argArr[i] = CommandAssert.parseBigAmount(arg, co.ui.getMegaKeks(), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%), scientific notation (5e+59) or everything/half.");
                        }
                        else
                        {
                            BigInteger amount;

                            var ds = arg.split(" ");

                            if (ds.length == 2)
                            {
                                var exp = BigNumbers.getExpForName(ds[1].toLowerCase());

                                if (exp == null)
                                    throw new CommandException("Number could not be parsed, invalid large number name.");

                                arg = ds[0] + "e" + exp;
                            }

                            try
                            {
                                amount = new BigDecimal(arg).toBigIntegerExact();

                                if (amount.signum() == -1)
                                    throw new CommandException(arg + "\nA negative number was entered, they are not supported here.");

                                argArr[i] = amount;
                            }
                            catch (NumberFormatException | ArithmeticException e)
                            {
                                throw new CommandException("Number could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...) or scientific notation (5e+59).\n*Please note that it must be a positive integer!*");
                            }
                        }
                    }
                    else if (clazz == IIdentifiable.class && type == ParType.ITEM_OR_CARD)
                    {
                        argArr[i] = CommandAssert.parseItemOrCard(arg);
                    }
                    else if (clazz == Item.class)
                    {
                        argArr[i] = CommandAssert.parseItem(arg);
                    }
                    else if (clazz == Card.class)
                    {
                        argArr[i] = CommandAssert.parseCard(arg);
                    }
                    else if (clazz == Achievement.class)
                    {
                        argArr[i] = CommandAssert.parseAchievement(arg);
                    }
                    else if (clazz == Command.class)
                    {
                        argArr[i] = CommandAssert.parseCommand(arg);
                    }
                    else if (clazz == CommandCategory.class)
                    {
                        argArr[i] = CommandAssert.parseCommandGroup(arg);
                    }
                    else if (clazz == User.class)
                    {
                        argArr[i] = CommandAssert.parseUser(co.jda, arg);
                    }
                    else if (clazz == Role.class)
                    {
                        argArr[i] = CommandAssert.parseRole(co.guild, arg);
                    }
                    else if (clazz == Member.class)
                    {
                        argArr[i] = CommandAssert.parseMember(co.guild, arg);
                    }
                    else if (clazz == TextChannel.class)
                    {
                        argArr[i] = CommandAssert.parseTextChannel(co.guild, arg);
                    }
                    else if (clazz == String.class)
                    {
                        argArr[i] = arg;
                    }
                    else if (clazz.isEnum())
                    {
                        var ec = clazz.getEnumConstants();
                        final var farg = arg;
                        var val = Arrays.stream(ec).filter(c -> c.toString().equalsIgnoreCase(farg)).findFirst().orElse(null);

                        if (val == null)
                            throw new CommandException(arg + " is not valid here. Try one of these: `" + Arrays.stream(ec).map(Object::toString).map(String::toLowerCase).collect(Collectors.joining(", ")) + "`");

                        argArr[i] = val;
                    }
                }

                try
                {
                    meth.invoke(null, argArr);
                }
                catch (IllegalAccessException e)
                {
                    BotMain.logger.fatal("An exception has occured while invoking a command.", e);
                }
                catch (IllegalArgumentException e)
                {
                    BotMain.logger.fatal("An exception has occured while invoking a command.", e);
                }
                catch (InvocationTargetException e)
                {
                    if (e.getCause() instanceof CommandException)
                    {
                        MR.send(co.textChannel, e.getCause().getMessage());
                    }
                    else
                    {
                        MR.send(co.textChannel, "**An error has occured:**\n" + e.getCause().toString());
                        BotMain.logger.fatal("An exception has occured while invoking a command.", e.getCause());
                    }
                }
            }
            catch (Exception e)
            {
                MR.send(co.textChannel, e.getMessage());
            }

            return;
        }

        var error = new StringBuilder();
        error.append("Error! Wrong arguments.\n**Usage:**\n");

        error.append(GenUsage.usage(command));

        MR.send(co.textChannel, error.toString());
    }
}
