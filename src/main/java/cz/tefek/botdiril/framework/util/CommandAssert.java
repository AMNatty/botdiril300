package cz.tefek.botdiril.framework.util;

import java.util.Arrays;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.math.BigDecimal;
import java.math.BigInteger;

import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.CommandStorage;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.timers.MiniTime;
import cz.tefek.botdiril.userdata.timers.Timer;

public class CommandAssert
{
    public static void assertTrue(boolean b, String errorMessage) throws CommandException
    {
        if (!b)
            throw new CommandException(errorMessage);
    }

    public static void assertEquals(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (!o1.equals(o2))
            throw new CommandException(errorMessage);
    }

    public static void assertNotEquals(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1.equals(o2))
            throw new CommandException(errorMessage);
    }

    public static void assertIdentity(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1 != o2)
            throw new CommandException(errorMessage);
    }

    public static void assertNotIdentity(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1 == o2)
            throw new CommandException(errorMessage);
    }

    // INTS

    /**
     * Hard failing
     */
    public static int parseInt(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    // LONGS

    /**
     * Hard failing
     */
    public static long parseLong(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Long.parseLong(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberMoreThanZeroL(long number, String errorMessage) throws CommandException
    {
        if (!(number > 0))
            throw new CommandException(errorMessage);
    }

    public static void numberNotBelowL(long number, long level, String errorMessage) throws CommandException
    {
        if (number < level)
            throw new CommandException(errorMessage);
    }

    public static void numberNotAboveL(long number, long level, String errorMessage) throws CommandException
    {
        if (number > level)
            throw new CommandException(errorMessage);
    }

    public static void numberInBoundsInclusiveL(long number, long levelMin, long levelMax, String errorMessage) throws CommandException
    {
        if (number < levelMin || number > levelMax)
            throw new CommandException(errorMessage);
    }

    public static void numberInBoundsExclusiveL(long number, long levelMin, long levelMax, String errorMessage) throws CommandException
    {
        if (number <= levelMin || number >= levelMax)
            throw new CommandException(errorMessage);
    }

    // DOUBLES

    /**
     * Hard failing
     */
    public static double parseDouble(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Double.parseDouble(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberMoreThanZeroD(double number, String errorMessage) throws CommandException
    {
        if (!(number > 0))
            throw new CommandException(errorMessage);
    }

    public static void numberNotBelowD(double number, double level, String errorMessage) throws CommandException
    {
        if (number < level)
            throw new CommandException(errorMessage);
    }

    public static void numberNotAboveD(double number, double level, String errorMessage) throws CommandException
    {
        if (number > level)
            throw new CommandException(errorMessage);
    }

    public static void numberInBoundsInclusiveD(double number, double levelMin, double levelMax, String errorMessage) throws CommandException
    {
        if (number < levelMin || number > levelMax)
            throw new CommandException(errorMessage);
    }

    public static void numberInBoundsExclusiveD(double number, double levelMin, double levelMax, String errorMessage) throws CommandException
    {
        if (number <= levelMin || number >= levelMax)
            throw new CommandException(errorMessage);
    }

    // STRINGS

    public static void matchesRegex(String s, String regex, String errorMessage) throws CommandException
    {
        if (s.matches(regex))
            throw new CommandException(errorMessage);
    }

    public static void stringNotEmptyOrNull(String s, String errorMessage) throws CommandException
    {
        if (s == null)
            throw new CommandException(errorMessage);

        if (s.isEmpty())
            throw new CommandException(errorMessage);

    }

    public static void stringNotTooLong(String s, int length, String errorMessage) throws CommandException
    {
        if (s.length() > length)
            throw new CommandException(errorMessage);
    }

    // PARSERS

    public static long parseAmount(String amountString, long base, String errorMessage) throws CommandException
    {
        long amount;

        try
        {
            amount = Long.parseUnsignedLong(amountString);

            if (amount > base)
            {
                throw new CommandException("That's more than you have!");
            }
        }
        catch (NumberFormatException e)
        {
            if (amountString.endsWith("%"))
            {
                try
                {
                    double f = Float.parseFloat(amountString.substring(0, amountString.length() - 1));

                    if (f < 0 || f > 100)
                    {
                        throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                    }
                    else
                    {
                        amount = Math.round((f / 100.0) * base);
                    }
                }
                catch (NumberFormatException e1)
                {
                    throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                }
            }
            else
            {
                if (amountString.equalsIgnoreCase("all") || amountString.equalsIgnoreCase("everything"))
                {
                    amount = base;
                }
                else if (amountString.equalsIgnoreCase("half"))
                {
                    amount = base / 2L;
                }
                else if (amountString.equalsIgnoreCase("keepone"))
                {
                    amount = base > 1 ? base - 1 : 0;
                }
                else
                {
                    throw new CommandException(errorMessage);
                }
            }
        }

        return amount;
    }

    public static TextChannel parseTextChannel(Guild g, String msg)
    {
        if (msg.isEmpty())
        {
            throw new CommandException("Text channel could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(msg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var tc = g.getTextChannelById(id);

                if (tc != null)
                {
                    return tc;
                }
                else
                {
                    throw new CommandException("Text channel could not be parsed: Could not find a channel with that snowflake ID.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Text channel could not be parsed: Could not parse the snowflake ID.");
        }

        throw new CommandException("Text channel could not be parsed: Could not locate the snowflake ID.");
    }

    public static Role parseRole(Guild g, String msg)
    {
        if (msg.isEmpty())
        {
            throw new CommandException("Role could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var rbn = g.getRolesByName(msg.trim(), true);

            if (rbn.size() == 1)
                return rbn.get(0);
            else if (rbn.size() > 1)
                throw new CommandException("There is too many roles with that name, try mentioning the role or using its id.");

            var m = Pattern.compile("[0-9]+").matcher(msg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var r = g.getRoleById(id);

                if (r != null)
                {
                    return r;
                }
                else
                {
                    throw new CommandException("Role could not be parsed: Could not find a role with that snowflake ID. The role has to be on **this** server.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Role could not be parsed: Could not parse the snowflake ID.");
        }

        throw new CommandException("Could not find a role with such ID or name. Try using a mention.");
    }

    public static Member parseMember(Guild g, String inputArg)
    {
        if (inputArg.isEmpty())
        {
            throw new CommandException("Member could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(inputArg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var member = g.getMemberById(id);

                if (member != null)
                {
                    return member;
                }
                else
                {
                    throw new CommandException("Member could not be parsed: Could not find a member with that ID.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Member could not be parsed: Could not parse the snowflake ID / mention.");
        }

        throw new CommandException("Member could not be parsed: Could not locate the snowflake ID / mention.");
    }

    public static User parseUser(JDA jda, String inputArg)
    {
        if (inputArg.isEmpty())
        {
            throw new CommandException("User could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(inputArg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var user = jda.getUserById(id);

                if (user != null)
                {
                    return user;
                }
                else
                {
                    throw new CommandException("User could not be parsed: Could not find a user with that ID.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("User could not be parsed: Could not parse the snowflake ID / mention.");
        }

        throw new CommandException("User could not be parsed: Could not locate the snowflake ID / mention.");
    }

    public static Command parseCommand(String arg)
    {
        var cmd = CommandStorage.search(arg);

        if (cmd == null)
            throw new CommandException("No such command.");
        else
            return cmd;
    }

    public static CommandCategory parseCommandGroup(String name)
    {
        var cg = Arrays.stream(CommandCategory.values()).filter(cc -> cc.toString().equalsIgnoreCase(name.trim())).findFirst();

        if (!cg.isPresent())
            throw new CommandException("No such command group.");

        return cg.get();
    }

    public static Item parseItem(String name)
    {
        var it = Item.getItemByName(name);

        if (it == null)
            throw new CommandException("Item not found: " + name);

        return it;
    }

    public static Card parseCard(String cname)
    {
        var card = Card.getCardByName(cname);

        if (card == null)
            throw new CommandException("Card not found: " + cname);

        return card;
    }

    private static final BigInteger oneHundred = BigInteger.valueOf(100);

    public static BigInteger parseBigAmount(String amountString, BigInteger base, String errorMessage) throws CommandException
    {
        BigInteger amount;

        var ds = amountString.split(" ");

        if (ds.length == 2)
        {
            var exp = BigNumbers.getExpForName(ds[1].toLowerCase());

            if (exp == null)
                throw new CommandException(errorMessage + "\nInvalid large number name.");

            amountString = ds[0] + "e" + exp;
        }

        try
        {
            amount = new BigDecimal(amountString).toBigIntegerExact();

            if (amount.signum() == -1)
                throw new CommandException(errorMessage + "\nA negative number was entered, they are not supported here.");

            return amount;
        }
        catch (NumberFormatException | ArithmeticException e)
        {
            if (amountString.endsWith("%"))
            {
                try
                {
                    int f = Integer.parseInt(amountString.substring(0, amountString.length() - 1));

                    if (f < 0 || f > 100)
                    {
                        throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                    }
                    else
                    {
                        return base.divide(oneHundred).multiply(BigInteger.valueOf(f));
                    }
                }
                catch (NumberFormatException e1)
                {
                    throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                }
            }
            else
            {
                if (amountString.equalsIgnoreCase("all") || amountString.equalsIgnoreCase("everything"))
                {
                    return base;
                }
                else if (amountString.equalsIgnoreCase("half"))
                {
                    return base.divide(BigInteger.TWO);
                }
                else if (amountString.equalsIgnoreCase("keepone"))
                {
                    return base.equals(BigInteger.ZERO) ? BigInteger.ZERO : base.subtract(BigInteger.ONE);
                }
                else
                {
                    throw new CommandException(errorMessage);
                }
            }
        }
    }

    public static BigInteger parseBigIntRadix10(String number, String errorMessage) throws CommandException
    {
        try
        {
            return new BigInteger(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    // TIMERS

    /**
     * Put a $ somewhere in the message to print the time there
     */
    public static void assertTimer(UserInventory ui, Timer timer, String errorMessage)
    {
        var tm = ui.useTimer(timer);

        if (tm == -1)
        {
            return;
        }
        else
        {
            throw new CommandException(errorMessage.replaceAll("\\$", MiniTime.formatDiff(tm)));
        }
    }
}
