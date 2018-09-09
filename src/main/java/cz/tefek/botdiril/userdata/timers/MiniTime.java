package cz.tefek.botdiril.userdata.timers;

import java.util.concurrent.TimeUnit;

public class MiniTime
{
    private static class MiniTimeCouldNotBeParsedException extends RuntimeException
    {
        /**
         * 
         */
        private static final long serialVersionUID = -5403949842120041373L;

        public MiniTimeCouldNotBeParsedException()
        {
            super("Time period could not be parsed. Correct format: _w_d_h_m_s **without spaces** between the units. You can skip a time unit. Example: 1h15m");
        }
    }

    private static final TimeUnit miliseconds = TimeUnit.MILLISECONDS;

    private static final int DAYS_IN_WEEK = 7;
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLIS_IN_MINUTE = 1000;

    public static long parse(String input)
    {
        if (input.equalsIgnoreCase("forever"))
        {
            return Long.MAX_VALUE;
        }

        // Nothing to parse
        if (input.isEmpty())
            throw new MiniTimeCouldNotBeParsedException();

        // Follow the scheme
        if (!input.matches("[0-9]*w?[0-9]*d?[0-9]*h?[0-9]*m?[0-9]*s?"))
            throw new MiniTimeCouldNotBeParsedException();

        // 4584 of what? Potatoes?
        if (input.matches("[0-9]+"))
            throw new MiniTimeCouldNotBeParsedException();

        // Where are the numbers?
        if (input.matches("[a-zA-Z]+"))
            throw new MiniTimeCouldNotBeParsedException();

        // It shouldn't start with a letter
        if (input.matches("^[a-zA-Z].+"))
            throw new MiniTimeCouldNotBeParsedException();

        var nrs = input.split("[a-zA-Z]");
        var letters = input.split("[0-9]+");

        if (nrs.length != letters.length)
            throw new MiniTimeCouldNotBeParsedException();

        long time = 0;

        for (int i = 1; i < nrs.length; i++)
        {
            var type = letters[i - 1];
            int number = 0;

            try
            {
                // The only time this fail is when the number is too long
                number = Integer.parseUnsignedInt(nrs[i]);
            }
            catch (NumberFormatException nfe)
            {
                throw new MiniTimeCouldNotBeParsedException();
            }

            var allow = 0;
            var multiplier = 0;

            switch (type.toLowerCase())
            {
                case "w":
                    allow = 9999;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_WEEK * MILLIS_IN_MINUTE;
                case "d":
                    allow = DAYS_IN_WEEK;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * MILLIS_IN_MINUTE;
                    break;
                case "h":
                    allow = HOURS_IN_DAY;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * MILLIS_IN_MINUTE;
                    break;
                case "m":
                    allow = MINUTES_IN_HOUR;
                    multiplier = SECONDS_IN_MINUTE * MILLIS_IN_MINUTE;
                    break;
                case "s":
                    allow = SECONDS_IN_MINUTE;
                    multiplier = MILLIS_IN_MINUTE;
                    break;
                default:
                    break;
            }

            // The top one can be more than it normally could have, for example you can
            // issue a ban for 48h but not 46h120m (it looks dumb)
            if (i == 1)
            {
                allow = 1000;
            }

            if (number > allow)
            {
                throw new MiniTimeCouldNotBeParsedException();
            }

            time += multiplier * number;
        }

        return System.currentTimeMillis() + time;
    }

    public static String fromMillisDiffNow(long after, long before)
    {
        if (after == Long.MAX_VALUE)
        {
            return "forever";
        }

        return formatDiff(after - before);
    }

    public static String fromMillisDiffNow(long future)
    {
        var diff = future - System.currentTimeMillis();

        return formatDiff(diff);
    }

    public static String formatDiff(long diff)
    {
        var xweeks = miliseconds.toDays(diff) / DAYS_IN_WEEK;
        var xdays = miliseconds.toDays(diff) % DAYS_IN_WEEK;
        var xhours = miliseconds.toHours(diff) % HOURS_IN_DAY;
        var xminutes = miliseconds.toMinutes(diff) % MINUTES_IN_HOUR;
        var xseconds = miliseconds.toSeconds(diff) % SECONDS_IN_MINUTE;

        return formatTime(xweeks, xdays, xhours, xminutes, xseconds);
    }

    private static String formatTime(long weeks, long days, long hours, long minutes, long seconds)
    {
        var sb = new StringBuilder();

        if (weeks > 0)
        {
            sb.append(weeks);
            sb.append('w');
        }
        if (days > 0)
        {
            sb.append(days);
            sb.append('d');
        }
        if (hours > 0)
        {
            sb.append(hours);
            sb.append('h');
        }
        if (minutes > 0)
        {
            sb.append(minutes);
            sb.append('m');
        }
        if (seconds > 0)
        {
            sb.append(seconds);
            sb.append('s');
        }

        var timeStr = sb.toString();

        return timeStr.isEmpty() ? "an instant" : timeStr;
    }
}
